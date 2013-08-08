package me.qni.trigrid;
import processing.core.*;
import processing.serial.Serial;
import ws2801.*;

public class TriGrid {

	WS2801 myLEDs = null;

	public final int[] diagonalRemap = {
		3, 4, 12, 13, 21, 22, 30, 31, 39, 
		55, 47, 46, 38, 37, 29, 28, 20, 19, 11, 10, 
		17, 18, 26, 27, 35, 36, 44, 45, 53, 54, 62, 63, 71, 
		87, 79, 78, 70, 69, 61, 60, 52, 51, 43, 42, 34, 33, 25, 24, 
		32, 40, 41, 49, 50, 58, 59, 67, 68, 76, 77, 85, 86, 94, 95, 
		102, 101, 93, 92, 84, 83, 75, 74, 66, 65, 57, 56, 48, 
		64, 72, 73, 81, 82, 90, 91, 99, 100, 108, 109, 
		116, 115, 107, 106, 98, 97, 89, 88, 80,
	};

	public final int[] remap = {
		3,   4,   5,   6,   7,   8,   9,  10,  11, 
		27,  26,  25,  24,  23,  22,  21,  20,  19,  18,  17, 
		31,  32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43, 
		59,  58,  57,  56,  55,  54,  53,  52,  51,  50,  49,  48,  47,  46,  45, 
		60,  61,  62,  63,  64,  65,  66,  67,  68,  69,  70,  71,  72,  73,  74, 
		88,  87,  86,  85,  84,  83,  82,  81,  80,  79,  78,  77,  76, 
		92,  93,  94,  95,  96,  97,  98,  99, 100, 101, 102, 
		116, 115, 114, 113, 112, 111, 110, 109, 108};

	public final int[] debugRemap = {
	 	 0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,
		29,  28,  27,  26,  25,  24,  23,  22,  21,  20,  19,  18,  17,  16,  15,  
		30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44, 
		59,  58,  57,  56,  55,  54,  53,  52,  51,  50,  49,  48,  47,  46,  45, 
		60,  61,  62,  63,  64,  65,  66,  67,  68,  69,  70,  71,  72,  73,  74, 
		89,  88,  87,  86,  85,  84,  83,  82,  81,  80,  79,  78,  77,  76,  75,
		90,  91,  92,  93,  94,  95,  96,  97,  98,  99, 100, 101, 102, 103, 104,
	   119, 118, 117, 116, 115, 114, 113, 112, 111, 110, 109, 108, 107, 106, 105};

	private final int sideNum = 4;

	private final int _Height = sideNum*2;
	private final int _Width = sideNum*4-1;

	private int clientX, clientY;
	private int clientW, clientH;

	public TriGrid() {
		this(null);
	}

	public TriGrid(WS2801 myLEDs) {
		this.myLEDs = myLEDs;
	}

	public void setPosition(int x, int y) {
		clientX = x;
		clientY = y;
	}

	public void setSize(int w, int h) {
		clientW = w;
		clientH = h;
	}

	public void draw(PApplet pa, int[] pixels, int[] remap) {

		pa.pushMatrix();
		pa.resetMatrix();

		pa.translate(clientX, clientY);

		pa.stroke(0);

		int a = 69;

		int i = 0;

		for (int y = 0; y < _Height; y++) {

			int rowSkip = pa.abs((int)(_Height/2 - 0.5 - y));

			if (y%2==0) {
				for (int x=0; x<_Width; x++) {
					if (x < rowSkip || _Width - x - 1 < rowSkip) continue;

					pa.fill(pixels[remap[i++]]);

					pa.triangle(
							a/2*(x  ), (pa.sqrt(3)/2*a)*((x+y+sideNum  )%2+y), 
							a/2*(x+1), (pa.sqrt(3)/2*a)*((x+y+sideNum+1)%2+y), 
							a/2*(x+2), (pa.sqrt(3)/2*a)*((x+y+sideNum+2)%2+y));
				}
			} 
			else {
				for (int x=_Width-1; x>=0; x--) {
					if (x < rowSkip || _Width - x - 1 < rowSkip) continue;

					pa.fill(pixels[remap[i++]]);

					pa.triangle(
							a/2*(x  ), (pa.sqrt(3)/2*a)*((x+y+sideNum  )%2+y), 
							a/2*(x+1), (pa.sqrt(3)/2*a)*((x+y+sideNum+1)%2+y), 
							a/2*(x+2), (pa.sqrt(3)/2*a)*((x+y+sideNum+2)%2+y));
				}
			}
		}

		pa.popMatrix();

		if(myLEDs != null) {
			myLEDs.refresh(pixels, remap);			
		}
	}

	public void debugDraw(PApplet pa, int[] pixels, int[] remap) {

		pa.pushMatrix();
		pa.resetMatrix();

		pa.translate(clientX, clientY);

		pa.stroke(0);

		int a = 69;

		int i = 0;

		for (int y = 0; y < _Height; y++) {

			int rowSkip = pa.abs((int)(_Height/2 - 0.5 - y));

			if (y%2==0) {
				for (int x=0; x<_Width; x++) {
					//if (x < rowSkip || _Width - x - 1 < rowSkip) continue;

					pa.fill(pixels[remap[i++]]);

					pa.triangle(
							a/2*(x  ), (pa.sqrt(3)/2*a)*((x+y+sideNum  )%2+y), 
							a/2*(x+1), (pa.sqrt(3)/2*a)*((x+y+sideNum+1)%2+y), 
							a/2*(x+2), (pa.sqrt(3)/2*a)*((x+y+sideNum+2)%2+y));
				}
			} 
			else {
				for (int x=_Width-1; x>=0; x--) {
					//if (x < rowSkip || _Width - x - 1 < rowSkip) continue;

					pa.fill(pixels[remap[i++]]);

					pa.triangle(
							a/2*(x  ), (pa.sqrt(3)/2*a)*((x+y+sideNum  )%2+y), 
							a/2*(x+1), (pa.sqrt(3)/2*a)*((x+y+sideNum+1)%2+y), 
							a/2*(x+2), (pa.sqrt(3)/2*a)*((x+y+sideNum+2)%2+y));
				}
			}
		}

		pa.stroke(255);
		pa.line(4*(a/2), 0, 0, pa.sqrt(3)/2*a*4);
		pa.line(0, pa.sqrt(3)/2*a*4, 4*(a/2), pa.sqrt(3)/2*a*8);
		pa.line(12*(a/2), 0, 16*(a/2), pa.sqrt(3)/2*a*4);
		pa.line(16*(a/2), pa.sqrt(3)/2*a*4, 12*(a/2), pa.sqrt(3)/2*a*8);

		pa.popMatrix();

		if(myLEDs != null) {
			myLEDs.refresh(pixels, remap);			
		}
	}
}



