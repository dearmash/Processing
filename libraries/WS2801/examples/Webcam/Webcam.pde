// "Webcam" example to accompany the Adavision DIY LED video wall project.
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

static final int arrayWidth  = 15,  // Width of LED array
                 arrayHeight = 10,  // Height of LED array
                 imgScale    = 40,  // Size of displayed preview
                 camWidth    = 160, // Width of camera input
                 camHeight   = 120; // Height of camera input
GSCapture        myCapture;
PImage           scaled      = createImage(arrayWidth, arrayHeight, RGB);
Serial           port;
WS2801           myLEDs;
int[]            remap;
int              cropX, cropY, cropWidth, cropHeight;

void setup() {
  float srcAspect, dstAspect;

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
  // Here's a little trick: even though the LED array starts at the top
  // left, we specify top right so that the image is flipped when
  // displayed on the LEDs, acting like a mirror.  (The onscreen
  // preview uses the camera data directly, and isn't flipped.)
  remap = myLEDs.zigzag(arrayWidth, arrayHeight,
    WS2801.START_TOP | WS2801.START_RIGHT | WS2801.ROW_MAJOR);

  // Invoke first available capture device.
  myCapture = new GSCapture(this, camWidth, camHeight);
  myCapture.play();     // GSVideo 0.9 stable lib requires this method
  // myCapture.start(); // Later GSVideo versions require this method

  // Maintain aspect ratio by cropping; don't letterbox, don't stretch.
  // Use all those pixels!
  srcAspect = float(camWidth) / float(camHeight);
  dstAspect = float(arrayWidth) / float(arrayHeight);
  if(srcAspect >= dstAspect) {
    // Crop left/right off video
    cropHeight = camHeight;
    cropWidth  = int(float(cropHeight) * dstAspect);
    cropX      = (camWidth - cropWidth) / 2;
    cropY      = 0;
  } else {
    // Crop top/bottom off video
    cropWidth  = camWidth;
    cropHeight = int(float(cropWidth) / dstAspect);
    cropX      = 0;
    cropY      = (camHeight - cropHeight) / 2;
  }
}

void draw() {
  // Scale cropped section of camera input down to display size
  scaled.copy(myCapture,
    cropX, cropY, cropWidth   , cropHeight,
    0    , 0    , scaled.width, scaled.height);

  // Preview LED data on computer display (un-mirrored)
  image(scaled, 0, 0, width, height);

  // Issue pixel data to the Arduino
  scaled.loadPixels();
  myLEDs.refresh(scaled.pixels, remap);
}

void captureEvent(GSCapture c) {
  c.read();
}

