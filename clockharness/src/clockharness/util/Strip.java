
package clockharness.util;

import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PFont;

public class Strip {

    private final PApplet p;

    private final int ledcount;

    public Strip(PApplet p, int ledcount, int datapin, int clockpin) {
        assert ledcount == 160 : "ledcount unexpected value";
        assert datapin == 2 : "datapin unexpected value";
        assert clockpin == 3 : "clockpin unexpected value";
        this.p = p;
        this.ledcount = ledcount;
        buffer = new int[ledcount];
        display = new int[ledcount];
        Arrays.fill(display, 0);

        PFont loadFont = p.loadFont("New-64.vlw");
        p.textFont(loadFont, 64);
        p.colorMode(PApplet.RGB, 255, 255, 255);
    }

    // @formatter:off
    public static final int[][] mapping = {
            {0, 19, 20, 39, 40, 59, 60, 79, 80, 99, 100, 119, 120, 139, 140, 159, },
            {1, 18, 21, 38, 41, 58, 61, 78, 81, 98, 101, 118, 121, 138, 141, 158, },
            {2, 17, 22, 37, 42, 57, 62, 77, 82, 97, 102, 117, 122, 137, 142, 157, },
            {3, 16, 23, 36, 43, 56, 63, 76, 83, 96, 103, 116, 123, 136, 143, 156, },
            {4, 15, 24, 35, 44, 55, 64, 75, 84, 95, 104, 115, 124, 135, 144, 155, },
            {5, 14, 25, 34, 45, 54, 65, 74, 85, 94, 105, 114, 125, 134, 145, 154, },
            {6, 13, 26, 33, 46, 53, 66, 73, 86, 93, 106, 113, 126, 133, 146, 153, },
            {7, 12, 27, 32, 47, 52, 67, 72, 87, 92, 107, 112, 127, 132, 147, 152, },
            {8, 11, 28, 31, 48, 51, 68, 71, 88, 91, 108, 111, 128, 131, 148, 151, },
            {9, 10, 29, 30, 49, 50, 69, 70, 89, 90, 109, 110, 129, 130, 149, 150, },
    };

    public static final int[] linearMapping = {
        0, 19, 20, 39, 40, 59, 60, 79, 80, 99, 100, 119, 120, 139, 140, 159,
        1, 18, 21, 38, 41, 58, 61, 78, 81, 98, 101, 118, 121, 138, 141, 158,
        2, 17, 22, 37, 42, 57, 62, 77, 82, 97, 102, 117, 122, 137, 142, 157,
        3, 16, 23, 36, 43, 56, 63, 76, 83, 96, 103, 116, 123, 136, 143, 156,
        4, 15, 24, 35, 44, 55, 64, 75, 84, 95, 104, 115, 124, 135, 144, 155,
        5, 14, 25, 34, 45, 54, 65, 74, 85, 94, 105, 114, 125, 134, 145, 154,
        6, 13, 26, 33, 46, 53, 66, 73, 86, 93, 106, 113, 126, 133, 146, 153,
        7, 12, 27, 32, 47, 52, 67, 72, 87, 92, 107, 112, 127, 132, 147, 152,
        8, 11, 28, 31, 48, 51, 68, 71, 88, 91, 108, 111, 128, 131, 148, 151,
        9, 10, 29, 30, 49, 50, 69, 70, 89, 90, 109, 110, 129, 130, 149, 150,
    };

    public static final String[] wordgrid = {
            "itsjisaredalmost",
            "tenpztwentyphalf",
            "iquartermxofived",
            "minutescjaupasto",
            "eoxtwelvebkfiveb",
            "bstfrshirtlessyu",
            "eewozeightthrees",
            "evouneaaeleventy",
            "rezrkerdrtninego",
            "qnigsixoytoclock",
    };
    // @formatter:on

    private final int[] buffer;

    private int[] display;

    public void begin() {
        Arrays.fill(buffer, 0);
    }

    public void setPixelColor(int i, int color) {
        buffer[i] = color;
    }

    private static float easeAmount = 0.15f;

    private static int easeThreshold = 1;

    public boolean easePixelColor(int i, int colorTarget) {
        int r0 = (int) p.red(buffer[i]);
        int g0 = (int) p.green(buffer[i]);
        int b0 = (int) p.blue(buffer[i]);

        int r1 = (int) p.red(colorTarget);
        int g1 = (int) p.green(colorTarget);
        int b1 = (int) p.blue(colorTarget);

        int dr = r1 - r0;
        int dg = g1 - g0;
        int db = b1 - b0;

        r0 = easeImp(r0, r1, dr);
        g0 = easeImp(g0, g1, dg);
        b0 = easeImp(b0, b1, db);

        int c;
        c = (r0);
        c <<= 8;
        c |= (g0);
        c <<= 8;
        c |= (b0);

        buffer[i] = c;

        return r0 == r1 && g0 == g1 && b0 == b1;
    }

    private int easeImp(int v0, int v1, int dv) {
        int move = (int) (dv * easeAmount);
        if (p.abs(move) < easeThreshold) {
            return v1;
        } else {
            return v0 + move;
        }
    }

    public void show() {
        display = Arrays.copyOf(buffer, ledcount);
    }

    public void draw() {
        int gridHeight = wordgrid.length;
        int gridWidth = wordgrid[0].length();

        p.background(127);

        p.fill(255);
        p.textSize(48);
        p.textAlign(PApplet.CENTER);

        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                p.text(wordgrid[i].charAt(j), PApplet.map(j, -1, gridWidth, 0, p.width) + 2,
                        PApplet.map(i, -1, gridHeight, 0, p.height) + 14);
            }
        }

        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                int c = display[mapping[i][j]];
                p.fill(p.red(c), p.green(c), p.blue(c));
                p.text(wordgrid[i].charAt(j), PApplet.map(j, -1, gridWidth, 0, p.width),
                        PApplet.map(i, -1, gridHeight, 0, p.height) + 12);
            }
        }
    }

}
