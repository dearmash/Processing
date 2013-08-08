// "Magnify" example to accompany the Adavision DIY LED video wall project.
// This is the host PC-side code written in Processing, intended for use
// with a USB-connected Arduino microcontroller running the accompanying
// LED streaming code.  As written, assumes a 15x10 array (150 pixels, six
// strands) of Digital RGB LED Pixels (Adafruit product ID #322,
// specifically the newer WS2801-based type) and a large 5 Volt power
// supply (such as an ATX computer power supply).  You may need to adapt
// the code and the hardware arrangement for your specific needs.

import java.awt.*;
import java.awt.image.*;
import java.awt.MouseInfo;
import processing.serial.*;
import ws2801.*;

static final int arrayWidth  = 15, // Width of LED array, in pixels
                 arrayHeight = 10, // Height of LED array, in pixels
                 imgScale    = 20; // Size of displayed preview on screen
Robot            bot;              // For screen capture
Serial           port;
WS2801           myLEDs;
DisposeHandler   dh;               // For disabling LEDs on exit
int[]            remap;

void setup() {
  GraphicsEnvironment ge;
  GraphicsDevice[]    gd;

  dh = new DisposeHandler(this);

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

  // Generate zigzag remap array to reconstitute image into LED order.
  remap = myLEDs.zigzag(arrayWidth, arrayHeight,
    WS2801.START_TOP | WS2801.START_LEFT | WS2801.ROW_MAJOR);

  // Initialize capture code:
  ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  gd = ge.getScreenDevices();
  try {
    bot = new Robot(gd[0]);
  }
  catch(AWTException e) {
    System.err.println("new Robot() failed.");
  }
}

void draw () {
  int       x, y;
  Rectangle r;
  PImage    img;

  // Get absolute mouse coordinates on screen, offset to center on LED array,
  // and constrain result so it doesn't extend offscreen in any direction.
  x = constrain(MouseInfo.getPointerInfo().getLocation().x - arrayWidth  / 2,
      0, screen.width  - arrayWidth);
  y = constrain(MouseInfo.getPointerInfo().getLocation().y - arrayHeight / 2,
      0, screen.height - arrayHeight);
  r = new Rectangle(x, y, arrayWidth, arrayHeight);

  // Capture rectangle from screen, convert BufferedImage to PImage
  img = new PImage(bot.createScreenCapture(r));
  img.loadPixels(); // Make pixel array readable

  // Display captured image and issue to LED array as well
  scale(imgScale);
  image(img, 0, 0);
  myLEDs.refresh(img.pixels, remap);
}

// The DisposeHandler is called on program exit (but before the Serial
// library is shutdown), in order to turn off the LEDs (reportedly more
// reliable than stop()).  Seems to work for the window close box and
// escape key exit, but not the 'Quit' menu option.
// Thanks to phi.lho in the Processing forums.

public class DisposeHandler {
  DisposeHandler(PApplet pa) {
    pa.registerDispose(this);
  }
  public void dispose() {
    myLEDs.refresh(); // No parameters = off
  }
}

