
package clockharness;

import static clockharness.WordLib.*;
import clockharness.util.Strip;
import processing.core.PApplet;

import java.util.Date;

@SuppressWarnings("serial")
public class ClockTakeOne extends PApplet {

    public static void main(String[] args) {
        PApplet.main(ClockTakeOne.class.getName());
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

    int prev = 0;

    int currentWord = 0;

    @Override
    public void draw() {

        Date now = new Date();

        int currentMillis = millis();
        if (prev + 1000 < currentMillis) {
            prev = currentMillis;

            strip.begin();

            showWord(strip, IT_IS);

            switch (abs(6 - now.getMinutes() / 5)) {
                case 0:
                    showWord(strip, HALF, 0x00ff00);
                    break;
                case 1:
                    showWord(strip, TWENTY_FIVE, 0x00ff00);
                    showWord(strip, MINUTES);
                    break;
                case 2:
                    showWord(strip, TWENTY, 0x00ff00);
                    showWord(strip, MINUTES);
                    break;
                case 3:
                    showWord(strip, QUARTER, 0x00ff00);
                    break;
                case 4:
                    showWord(strip, TEN_1, 0x00ff00);
                    showWord(strip, MINUTES);
                    break;
                case 5:
                    showWord(strip, FIVE_1, 0x00ff00);
                    showWord(strip, MINUTES);
                    break;
            }

            if (now.getMinutes() >= 35) {
                showWord(strip, TO);
                showWord(strip, (now.getHours() + 12) % 12 + ONE, 0x00ffff);
            } else if (now.getMinutes() >= 5) {
                showWord(strip, PAST);
                showWord(strip, (now.getHours() + 11) % 12 + ONE, 0x00ffff);
            } else {
                showWord(strip, (now.getHours() + 11) % 12 + ONE, 0x00ffff);
            }

            showWord(strip, OCLOCK);

            strip.show();
        }

        // End of draw();
        strip.draw();
    }
}
