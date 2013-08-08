// "Plasma" example to accompany the Adavision DIY LED video wall project.
// This is the host PC-side code written in Processing, intended for use
// with a USB-connected Arduino microcontroller running the accompanying
// LED streaming code.  As written, assumes a 15x10 array (150 pixels, six
// strands) of Digital RGB LED Pixels (Adafruit product ID #322,
// specifically the newer WS2801-based type) and a large 5 Volt power
// supply (such as an ATX computer power supply).  You may need to adapt
// the code and the hardware arrangement for your specific needs.

// This is an abbreviated version of luis2048's Plasma Demo Effect code that's
// included with Processing.

import processing.serial.*;
import ws2801.*;

static final int arrayWidth  = 15, // Width of LED array
                 arrayHeight = 10, // Height of LED array
                 imgScale    = 40; // Size of displayed preview
PImage           img         = createImage(arrayWidth, arrayHeight, RGB);
Serial           port;
WS2801           myLEDs;
int[]            remap;

void setup() {

  size(arrayWidth * imgScale, arrayHeight * imgScale, JAVA2D);
  colorMode(HSB);

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

  // Generate zigzag remap array to reconstitute image into LED order.
  remap = myLEDs.zigzag(arrayWidth, arrayHeight,
    WS2801.START_TOP | WS2801.START_LEFT | WS2801.ROW_MAJOR);
}

void draw() {
  int   hue, sat, bri, i, x, xc, y, yc;
  float s, s1, s2, s3, calc1, calc2;

  calc1 = sin(radians(frameCount * 1.176470588235294));
  calc2 = sin(radians(frameCount * 1.818181818181818));
  
  img.loadPixels();
  for(i=y=0, yc=0; y<img.height; y++, yc += imgScale * 1.5) {
    s1 = 128 + 127 * sin(radians(yc) * calc2);
    for(x=0, xc=0; x<img.width; x++, xc += imgScale * 1.5) {
      s2 = 128 + 127 * sin(radians(xc) * calc1);
      s3 = 128 + 127 * sin(radians((xc + yc + frameCount * 5) / 2));  
      s  = (s1+ s2 + s3) / 3;
      if(s <= 100) {
        // Black to red
        hue = 0;
        sat = 255;
        bri = (int)map(s, 0, 100, 0, 255);
      } else if(s <= 200) {
        // Red to yellow
        hue = (int)map(s, 100, 200, 0, 42);
        sat = 255;
        bri = 255;
      } else {
        // Yellow to white
        hue = 42;
        sat = (int)map(s, 200, 255, 255, 0);
        bri = 255;
      }
      img.pixels[i++] = color(hue, sat, bri);
    }
  }
  img.updatePixels();

  // Preview LED data on computer display
  image(img, 0, 0, width, height);

  // Issue pixel data to the Arduino
  myLEDs.refresh(img.pixels, remap);
}

