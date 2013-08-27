
package pebble;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class TestCanvas extends PApplet {

    public static void main(String[] args) {
        PApplet.main(TestCanvas.class.getName());
    }

    boolean[][] screen = new boolean[168][144];

    @Override
    public void setup() {
        size(screen[0].length, screen.length);

        background(0);

        colorMode(RGB, 1);
        ellipseMode(CORNER);
        noStroke();
        noSmooth();
    }

    @Override
    public void draw() {
        fill(1);
        int lWidth = 26;
        int lHeight = 47;
        rect(7, 60, lWidth, lHeight);
        drawLetter(7, 60);
        rect(38, 60, lWidth, lHeight);
        drawLetter(38, 60);

        rect(69, 73, 6, 2);
        rect(70, 72, 4, 4);
        rect(71, 71, 2, 6);

        rect(69, 92, 6, 2);
        rect(70, 91, 4, 4);
        rect(71, 90, 2, 6);

        rect(80, 60, lWidth, lHeight);
        drawLetter(80, 60);
        rect(111, 60, lWidth, lHeight);
        drawLetter(111, 60);
    }

    private void drawLetter(int x, int y) {

        fill(0, 0, 1);
        rect(x, y, 7, 7);
        rect(x + 19, y, 7, 7);
        rect(x, y + 40, 7, 7);
        rect(x + 19, y + 40, 7, 7);

        fill(0, 1, 0);
        rect(x, y + 19, 7, 9);
        rect(x + 19, y + 19, 7, 9);

        fill(1, 0, 0);
        rect(x + 7, y, 12, 7);
        rect(x + 7, y + 19, 12, 7);
        rect(x + 7, y + 40, 12, 7);
        rect(x, y + 7, 7, 12);
        rect(x + 19, y + 7, 7, 12);
        rect(x, y + 28, 7, 12);
        rect(x + 19, y + 28, 7, 12);

        // reset
        fill(1);
    }

}
