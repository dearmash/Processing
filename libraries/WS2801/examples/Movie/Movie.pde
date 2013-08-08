// "Movie" example to accompany the Adavision DIY LED video wall project.
// This is the host PC-side code written in Processing, intended for use
// with a USB-connected Arduino microcontroller running the accompanying
// LED streaming code.  As written, assumes a 15x10 array (150 pixels, six
// strands) of Digital RGB LED Pixels (Adafruit product ID #322,
// specifically the newer WS2801-based type) and a large 5 Volt power
// supply (such as an ATX computer power supply).  You may need to adapt
// the code and the hardware arrangement for your specific needs.

// Users of Processing prior to version 2.0 will need to install the
// GSVideo library, available at http://gsvideo.sourceforge.net/

import processing.serial.*;
import codeanticode.gsvideo.*;
import ws2801.*;

static final int arrayWidth  = 15, // Width of LED array
                 arrayHeight = 10, // Height of LED array
                 imgScale    = 40; // Size of displayed preview
GSMovie          myMovie;
PImage           scaled      = createImage(arrayWidth, arrayHeight, RGB);
Serial           port;
WS2801           myLEDs;
int[]            remap;
int              cropX, cropY, cropWidth, cropHeight;

void setup() {
  String filename;
  float  srcAspect, dstAspect;

  size(arrayWidth * imgScale, arrayHeight * imgScale, JAVA2D);

  // Open serial connection to the Arduino running LEDstream code.
  // As written, this assumes the Arduino is the first/only USB serial
  // device; you may need to change the second parameter here to a
  // different port index or an absolute port name (e.g. "COM6").
  port = new Serial(this, Serial.list()[0], 115200);
  // Alternately, here's a method for automatic port scanning, but it
  // doesn't yet work reliably on all systems.  Give it a try, and if it
  // doesn't work with your setup, use the regular code instead.
  // port = WS2801.scanForPort(this);

  // Init LED library.
  myLEDs = new WS2801(port, arrayWidth * arrayHeight);

  // If the sketch freezes after the first frame, reset the Arduino and
  // uncomment the following line, a temporary workaround for an obscure
  // combination of hardware and code.  Unfortunately may flicker a bit.
  // myLEDs.useShortWrites = true;

  // Adjust color balance; you may need to tweak this for best output.
  myLEDs.setGamma(0, 200, 2.3, 0, 255, 2.2, 0, 200, 2.4);

  // Generate zigzag remap array to reconstitute image into LED order.
  remap = myLEDs.zigzag(arrayWidth, arrayHeight,
    WS2801.START_TOP | WS2801.START_LEFT | WS2801.ROW_MAJOR);

  // Select media file and wait for successful load
  filename = selectInput("Select media file to play:");
  myMovie  = new GSMovie(this, filename);
  myMovie.loop();
  while(myMovie.ready() == false); // Can't query size until movie is playing

  // Maintain aspect ratio by cropping; don't letterbox, don't stretch.
  // Use all those pixels!
  srcAspect = float(myMovie.getSourceWidth()) / float(myMovie.getSourceHeight());
  dstAspect = float(arrayWidth) / float(arrayHeight);
  if(srcAspect >= dstAspect) {
    // Crop left/right off video
    cropHeight = myMovie.getSourceHeight();
    cropWidth  = int(float(cropHeight) * dstAspect);
    cropX      = (myMovie.getSourceWidth() - cropWidth) / 2;
    cropY      = 0;
  } else {
    // Crop top/bottom off video
    cropWidth  = myMovie.getSourceWidth();
    cropHeight = int(float(cropWidth) / dstAspect);
    cropX      = 0;
    cropY      = (myMovie.getSourceHeight() - cropHeight) / 2;
  }
}

void draw() {
  // Scale cropped section of movie down to display size
  scaled.copy(myMovie,
    cropX, cropY, cropWidth   , cropHeight,
    0    , 0    , scaled.width, scaled.height);

  // Preview LED data on computer display
  image(scaled, 0, 0, width, height);

  // Issue pixel data to the Arduino
  scaled.loadPixels();
  myLEDs.refresh(scaled.pixels, remap);
  myLEDs.printStats();  // Show frame rate, current, etc.
}

void movieEvent(GSMovie m) {
  m.read();
}

