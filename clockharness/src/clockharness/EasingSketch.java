
package clockharness;

import static clockharness.WordLib.*;
import clockharness.util.Strip;
import processing.core.PApplet;

import java.util.Date;

@SuppressWarnings("serial")
public class EasingSketch extends PApplet {

    public static void main(String[] args) {
        PApplet.main(EasingSketch.class.getName());
    }

    private static final int ledCount = 10 * 16;

    private static final int dataPin = 2;

    private static final int clockPin = 3;

    Strip strip;

    @Override
    public void setup() {
        size(640, 640);
        background(0);

        strip = new Strip(this, ledCount, dataPin, clockPin);
    }

    int currentWord = 0;

    boolean on = true;

    @Override
    public void draw() {

        Date now = new Date();

        if (on) {
            if (easeWord(strip, TWENTY_FIVE, 0xffffff))
                on = !on;
        } else if (easeWord(strip, TWENTY_FIVE, 0x000000)) {
            on = !on;
        }

        strip.show();

        // End of draw();
        strip.draw();
    }
}
