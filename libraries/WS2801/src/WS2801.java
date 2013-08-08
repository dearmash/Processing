/****************************************************************************
 File        : WS2801.java

 Description : Library for addressing Adafruit Digital RGB LED pixels
               (product ID #322, specifically the newer WS2801-based type)
               from a computer.  This is the host PC-side code written in
               Java and intended for use with Processing sketches (see
               included examples for use); requires a USB-connected Arduino
               microcontroller running the accompanying LED streaming code.

 History     : 09/25/2011  Adapted from earlier p9813 C library.

 License     : Copyright 2011 Phillip Burgess.    www.PaintYourDragon.com

               This Program is free software: you can redistribute it and/or
               modify it under the terms of the GNU General Public License as
               published by the Free Software Foundation, either version 3 of
               the License, or (at your option) any later version.

               This Program is distributed in the hope that it will be
               useful, but WITHOUT ANY WARRANTY; without even the implied
               warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
               PURPOSE.  See the GNU General Public License for more details.

               You should have received a copy of the GNU General Public
               License along with this Program.  If not, see
               <http://www.gnu.org/licenses/>.
 ****************************************************************************/

package ws2801;

import processing.core.*;
import processing.serial.Serial;
import java.util.Arrays;

public class WS2801 {

	private Serial    myPort;       // Serial connection to Arduino
	private byte[]    buffer;       // Serial output buffer
	private int       nPixels;      // Total number of pixels in strand
	private short[][] rgbGamma;     // Gamma correction tables
	private double[]  pixelCurrent; // Estimated LED power usage

	public boolean
	  useShortWrites = false;       // See notes in refresh()

	// Statistical info: frame rates, throughput, power consumption, etc.
	// These are all public so that parent code can query them.
	public long
	  frames,         // Total number of frames output
	  bits,           // Bits output, current frame
	  bitsTotal,      // Total bits output, all frames
	  bps,            // Write speed, current frame
	  bpsAvg,         // Average bits/sec, all frames
	  msecIo,         // I/O time for current frame
	  msecFrame,      // Total time for current frame
	  msecIoTotal,    // Total time for I/O, all frames
	  msecFrameTotal; // Total time for all frames
	public double
	  fps,            // Frames/sec, current frame
	  fpsAvg,         // Average fps, all frames
	  ma,             // Current used by frame
	  maAvg,          // Average current use
	  maMax,          // Peak current use
	  mah,            // Charge used by prior frame
	  mahTotal;       // Total charge used thus far
	private long
	  priorTime;      // Used in frame-to-frame timing calc

	// Constants used for 'order' parameter of zigzag method:
	public static final int
	  START_TOP    = 0,
	  START_BOTTOM = 1,
	  START_LEFT   = 0,
	  START_RIGHT  = 2,
	  ROW_MAJOR    = 0,
	  COL_MAJOR    = 4;

	// Constants used in estimating LED current consumption, which
	// may be helpful in determining power supply and battery capacity
	// requirements.  A single 50 pixel strand of Adafruit 12mm
	// Diffused RGB LED Pixels was fed a regulated +5.0 Volts, then
	// current readings in various modes were taken with a multimeter.

	private static final int
		// Baseline current reading (in milliamps) with all LEDs off;
		// this establishes the power used by the driver chips alone:
		CAL_N_PIXELS    =   50, // Number of LED pixels in test
		CAL_CURRENT_OFF =   33, // Current (mA) with all LEDs off

		// "Raw" current readings (in milliamps) from the meter,
		// not yet factoring out the baseline usage:
		CAL_RAW_R       =  948, // All pixels 100% red
		CAL_RAW_G       =  944, // All pixels 100% green
		CAL_RAW_B       =  946, // All pixels 100% blue
		CAL_RAW_RG      = 1861, // All pixels 100% red+green
		CAL_RAW_GB      = 1858, // All pixels 100% green+blue
		CAL_RAW_RB      = 1863, // All pixels 100% red+blue
		CAL_RAW_RGB     = 2776, // All pixels 100% red+green+blue

		// Subtract the baseline usage to isolate actual LED current:
		CAL_CURRENT_R   = (CAL_RAW_R  - CAL_CURRENT_OFF),
		CAL_CURRENT_G   = (CAL_RAW_G  - CAL_CURRENT_OFF),
		CAL_CURRENT_B   = (CAL_RAW_B  - CAL_CURRENT_OFF),
		CAL_CURRENT_RG  = (CAL_RAW_RG - CAL_CURRENT_OFF),
		CAL_CURRENT_GB  = (CAL_RAW_GB - CAL_CURRENT_OFF),
		CAL_CURRENT_RB  = (CAL_RAW_RB - CAL_CURRENT_OFF);

	// Current consumption of the LEDs scales very near linearly with
	// the PWM duty cycle -- close enough that it can reasonably be
	// left out of the model.  However...

	// "Combination" currents when measured don't equal the sum of the
	// component currents.  For example, a pixel with either red or
	// green active will match the above readings, but a pixel with both
	// red AND green active will draw slightly less current than the
	// prior two figures combined.  This phenomenon generally occurs
	// when the LED strand is inadequately powered, it appears in all
	// combinations (R+G, G+B, R+B, R+G+B) and is compounded in the
	// R+G+B case.  The model takes this into account...in testing,
	// the estimated current figure is typically within 1% of an actual
	// metered result.  However however...

	// You may sometimes get meter readings that are +/-3% off the
	// estimate.  Part of this is simply because the combination model
	// isn't perfect.  But it's also because the estimates are derived
	// from averages of the whole LED strand, but manufacturing
	// tolerances are such that each pixel is in reality slightly
	// different, and this will be most apparent with certain lighting
	// combintations.  Given more frames over more time, the overall
	// measured average should converge closer to the code's estimate.

	private static final double
		CAL_COMBO_RG    = ((double)CAL_CURRENT_RG /
		                   (double)(CAL_CURRENT_R + CAL_CURRENT_G)),
		CAL_COMBO_GB    = ((double)CAL_CURRENT_GB /
		                   (double)(CAL_CURRENT_G + CAL_CURRENT_B)),
		CAL_COMBO_RB    = ((double)CAL_CURRENT_RB /
		                   (double)(CAL_CURRENT_R + CAL_CURRENT_B)),
		DEFAULT_GAMMA   = 2.5;

	// Constructor:
	public WS2801(Serial port, int n) {

		myPort       = port;
		nPixels      = n;
		buffer       = new byte[6 + nPixels * 3]; // +6 for header
		rgbGamma     = new short[256][3];
		pixelCurrent = new double[nPixels];
		setGamma();  // Initialize default gamma table
		initStats(); // Initialize statistics

		// A special header / magic word is expected by the
		// corresponding LED streaming code running on the
		// Arduino.  This only needs to be initialized once
		// because the number of LEDs remains constant:
		buffer[0] = 'A'; // Magic word
		buffer[1] = 'd';
		buffer[2] = 'a';
		buffer[3] = (byte)((nPixels - 1) >> 8);   // LED count high
		buffer[4] = (byte)((nPixels - 1) & 0xff); // LED count low
		buffer[5] = (byte)(buffer[3] ^ buffer[4] ^ 0x55); // Checksum
	}

	public void dispose() {
		if(myPort != null) myPort.dispose();
	}

	// Set default gamma correction:
	public void setGamma() {
		setGamma(DEFAULT_GAMMA);
	}

	// Simple gamma correction; full range, single exponent value:
	public void setGamma(double gamma) {
		setGamma(
		  0, 255, gamma,
		  0, 255, gamma,
		  0, 255, gamma);
	}

	// Fancy gamma correction; separate R,G,B ranges and exponents:
	public void setGamma(
	  int rMin, int rMax, double rGamma,
	  int gMin, int gMax, double gGamma,
	  int bMin, int bMax, double bGamma) {
		short  i;
		double rRange, gRange, bRange, d;

		rRange = (double)(rMax - rMin);
		gRange = (double)(gMax - gMin);
		bRange = (double)(bMax - bMin);

		for(i=0; i<256; i++) {
			d = (double)i / 255.0;
			rgbGamma[i][0] = (short)(rMin +
			  (int)Math.floor(rRange * Math.pow(d,rGamma) + 0.5));
			rgbGamma[i][1] = (short)(gMin +
			  (int)Math.floor(gRange * Math.pow(d,gGamma) + 0.5));
			rgbGamma[i][2] = (short)(bMin +
			  (int)Math.floor(bRange * Math.pow(d,bGamma) + 0.5));
		}
	}

	// Disable gamma; full-range linear ramp for R,G,B (no correction):
	public void disableGamma() {
		short i;

		for(i=0; i<256; i++) {
			rgbGamma[i][0] =
			rgbGamma[i][1] =
			rgbGamma[i][2] = (short)i;
		}
	}

	// Initialize (or restart) statistical data:
	public void initStats() {
		frames = bits = bitsTotal = bps = bpsAvg = msecIo =
		  msecFrame = msecIoTotal = msecFrameTotal = priorTime = 0;
		fps = fpsAvg = ma = maAvg = maMax = mah = mahTotal = 0.0;
	}

	// Turn all LEDs off:
	public void refresh() {
		refresh(null,null);
	}

	// Issue data to LEDs with direct array-to-pixel relationship:
	public void refresh(int[] pixels) {
		refresh(pixels,null);
	}

	// Issue data to LEDs with array-to-pixel remapping table:
	public void refresh(int[] pixels, int[] remap) {
		short  r, g, b;
		int    p, i, j, rgb;
		long   time1, time2;
		double sum;

		for(p=0, i=6; p<nPixels; p++) {
			if(pixels == null) {
				r = g = b = 0;
			} else {
				if(remap == null) rgb = pixels[p];
				else              rgb = pixels[remap[p]];
				r = (short)((rgb >> 16) & 0xff);
				g = (short)((rgb >>  8) & 0xff);
				b = (short)((rgb      ) & 0xff);
			}
			// Filter R,G,B through gamma tables
			r = rgbGamma[r][0];
			g = rgbGamma[g][1];
			b = rgbGamma[b][2];
			// Store gamma-corrected values
			buffer[i++] = (byte)r;
			buffer[i++] = (byte)g;
			buffer[i++] = (byte)b;
			// Estimate current based on filtered values
			pixelCurrent[p] = estimateCurrent(r,g,b);
		}

		time1 = System.currentTimeMillis();
		if(myPort != null) {
			// I've encountered a situation where a system
			// would hang on large contiguous writes.  Unusure
			// at this point whether it's the hardware, OS,
			// version of Processing or the RXTX library, or
			// something specific to the Arduino board that's
			// causing this.  The current workaround until the
			// root cause is found and addressed is to issue
			// data in limited chunks, no more than 255 (not
			// 256) bytes at a time.  Have only encountered
			// the problem on one system so far, so the
			// default behavior is to use one large write.
			if(useShortWrites == true) {
				for(i=0; i<buffer.length; i=j) {
					j = i + 255;
					if(j > buffer.length)
						j = buffer.length;
					myPort.write(Arrays.copyOfRange(
					  buffer,i,j));
				}
			} else {
				myPort.write(buffer);
			}
		}
		time2 = System.currentTimeMillis();

		bits       = nPixels * 24;
		bitsTotal += bits;

		// Get I/O elapsed time and compute throughput for frame:
		if((msecIo = (time2 - time1)) > 0) {
			bps = (long)(((double)bits * 1000.0) / (double)msecIo);
			msecIoTotal += msecIo;
		} else {
			bps = 0; // Probably I/O error
		}

		// Compute average throughput from total bits output and
		// cumulative I/O time.  Avoid divide-by-zero first:
		if(msecIoTotal != 0) {
			bpsAvg = (long)(((double)bitsTotal * 1000.0) /
			  (double)msecIoTotal);
		} else {
			bpsAvg = bps;
		}

		// Some figures cannot be calculated until multiple
		// frames have been rendered and output.
		if(frames > 0) {
			msecFrame = time2 - priorTime;
			if(msecFrame != 0) {
				fps = 1000.0 / (double)msecFrame;
				msecFrameTotal += msecFrame;
			} else {
				fps = 0.0; // Probably I/O error
			}

			if(msecFrameTotal != 0) {
				fpsAvg = (double)frames *
				  1000.0 / (double)msecFrameTotal;
			}

			// Milliamp-hour calculations need to work from the
			// PRIOR frame, so don't calculate the mA value of
			// the new frame yet!  Use the old one...
			mah = ma * ((double)msecFrame) / (1000.0*60.0*60.0);
			mahTotal += mah;

			// Average current is back-calculated from total
			// mAH and total time, NOT simply total current
			// and total frames.  This gives an average-per-
			// unit-of-time (generally constant by the laws
			// of physics) rather than an average-per-frame
			// (variable by CPU power and frame complexity).
			maAvg = mahTotal * (1000.0 * 60.0 * 60.0) /
			  (double)msecFrameTotal;
		}

		// With mAH calculations done, the mA estimate
		// can now be updated for the new frame.
		for(sum=0.0, i=0; i<nPixels; i++) sum += pixelCurrent[i];
		ma = sum;
		if(ma > maMax) maMax = ma;

		priorTime = time2; // Save for next time
		frames++;
	}

	// Estimate current consumption for one pixel:
	private double estimateCurrent(short r, short g, short b) {
		return
		  // Base current...
		  ((double)CAL_CURRENT_OFF / (double)CAL_N_PIXELS) +
		  // ...plus RGB current...
		  ((((double)r*(double)CAL_CURRENT_R/(double)CAL_N_PIXELS) +
		    ((double)g*(double)CAL_CURRENT_G/(double)CAL_N_PIXELS) +
		    ((double)b*(double)CAL_CURRENT_B/(double)CAL_N_PIXELS)) /
		    255.0 *
		  // ...times combinational factors.
		  (1.0-((double)r*(double)g/(255.0*255.0)*(1.0-CAL_COMBO_RG))) *
		  (1.0-((double)g*(double)b/(255.0*255.0)*(1.0-CAL_COMBO_GB))) *
		  (1.0-((double)r*(double)b/(255.0*255.0)*(1.0-CAL_COMBO_RB))));
	}

	// Wanton dump of all statistical info to the console:
	public void printStats() {
		System.out.format(
		  "Total frames               : %d\n" +
		  "Bits in this frame         : %d\n" +
		  "Total bits output          : %d\n" +
		  "Write speed for this frame : %d bits/sec\n" +
		  "Average write speed        : %d bits/sec\n" +
		  "I/O time for this frame    : %d mS\n" +
		  "Total time for this frame  : %d mS\n" +
		  "Total I/O time, all frames : %d mS\n" +
		  "Total time, all frames     : %d mS (%d seconds)\n" +
		  "FPS for this frame         : %.1f\n" +
		  "Average frames/second      : %.1f\n" +
		  "Current use for this frame : %.3f mA (@5.0V)\n" +
		  "Average current            : %.3f mA (@5.0V)\n" +
		  "Peak current               : %.3f mA (@5.0V)\n" +
		  "Charge for prior frame     : %f mAH (@5.0V)\n" +
		  "Total charge, all frames   : %.3f mAH (@5.0V)\n\n",
		  frames,
		  bits,
		  bitsTotal,
		  bps,
		  bpsAvg,
		  msecIo,
		  msecFrame,
		  msecIoTotal,
		  msecFrameTotal, (msecFrameTotal + 500L) / 1000L,
		  fps,
		  fpsAvg,
		  ma,
		  maAvg,
		  maMax,
		  mah,
		  mahTotal);
	}

	// Generate a zigzag remap array suitable for refresh():
	public int[] zigzag(int width, int height, int order) {
		int i, major, minor, incMajor, incMinor, mulMajor, mulMinor,
		    limitMajor, limitMinor;
		int remap[] = new int[width * height];

		// Determine initial position, incs, muls and limits
		if((order & COL_MAJOR) != 0) {
			mulMajor = 1;
			mulMinor = width;
			if((order & START_RIGHT) != 0) {
				major      = width - 1;
				limitMajor = -1;
			} else {
				major      = 0;
				limitMajor = width;
			}
			minor = ((order & START_BOTTOM) != 0) ? height - 1 : 0;
			limitMinor = height;
		} else { // Row major
			mulMajor = width;
			mulMinor = 1;
			if((order & START_BOTTOM) != 0) {
				major      = height - 1;
				limitMajor = -1;
			} else {
				major      = 0;
				limitMajor = height;
			}
			minor = ((order & START_RIGHT) != 0) ? width - 1 : 0;
			limitMinor = width;
		}
		incMajor = (major > 0) ? -1 : 1;
		incMinor = (minor > 0) ? -1 : 1;

		// Iterate though each position in grid, reversing
		// row/column directions as suited to the given order.
		for(i=0; major != limitMajor; i++) {
			remap[i] = major * mulMajor + minor * mulMinor;
			minor   += incMinor;
			if((minor == -1) || (minor == limitMinor)) {
				incMinor = -incMinor;
				minor   +=  incMinor;
				major   +=  incMajor;
			}
		}

		return remap;
	}

	private static final int timeout = 5000;

	// This method scans all serial ports on the system until one is
	// found with an Arduino running the LEDstream code.
	// IMPORTANT NOTE: this FAILS when using a 32u4 or Leonardo board
	// and Processing 1.5.1.  There are a few interim workarounds:
	// 1) Don't use the auto-detect method; call new Serial() with a
	//    hardcoded serial port name for the time being.
	// 2) Use a "classic" Arduino; Uno, Duemilanove, etc.
	//    Unfortunately the serial speed is then only
	//    good for up to 100 pixels or so.
	// 3) Use an alpha version of Processing 2.0.
	// 4) FOR TECHNICAL USERS ONLY: replace the RXTX library in
	//    Processing 1.5.1 with a version pulled from 2.0a or from
	//    the Arduino IDE.  Usual precautions about backups apply;
	//    you're on your own.  This is hairy stuff and we really
	//    can't provide support for this case; if this is too "out
	//    there," please try one of the other fixes for now.
	//    Eventually a finished release of Processing 2.0 will ship.

	public static Serial scanForPort(PApplet parent) {
		String[] ports;
		String   ack;
		int      i;
		long     start;
		Serial   s;

		ports = Serial.list(); // List of all serial ports

		for(i=0; i<ports.length; i++) { // For each port...
			System.out.format("Trying serial port %s\n", ports[i]);
			try {
				s = new Serial(parent, ports[i], 115200);
			}
			catch(Exception e) {
				// Fail...probably in use by other software.
				continue;
			}
			// Port opened...watch for acknowledgement string...
			start = System.currentTimeMillis();
			while((System.currentTimeMillis() - start) < timeout) {
				if((s.available() >= 4) &&
				  ((ack = s.readString()) != null) &&
				  ack.contains("Ada\n")) {
					return s; // Got it!
				}
			}
			// Connection timed out.  Close port, try next.
			s.stop();
		}

		// Didn't locate a device returning the acknowledgment
		// string.  Maybe it's out there but running the old
		// LEDstream code, which didn't have the ACK.  Can't say
		// for sure, so we'll take our changes with the first/only
		// serial device out there...
		System.out.format("Using default serial port %s\n", ports[0]);
		return new Serial(parent, ports[0], 115200);
	}
}

