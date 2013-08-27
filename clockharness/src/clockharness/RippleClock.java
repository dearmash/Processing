
package clockharness;

import static clockharness.WordLib.*;
import clockharness.util.Strip;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.Date;

@SuppressWarnings("serial")
public class RippleClock extends PApplet {

    public static void main(String[] args) {
        PApplet.main(RippleClock.class.getName());
    }

    private static final int gWidth = 16;

    private static final int gHeight = 10;

    private static final int ledCount = gWidth * gHeight;

    private static final int dataPin = 2;

    private static final int clockPin = 3;

    float decay = 0.98f;

    Strip strip;

    int[] frame0 = new int[ledCount];
    int[] frame1 = new int[ledCount];

    @Override
    public void setup() {
        size(640, 640);
        background(0);

        strip = new Strip(this, ledCount, dataPin, clockPin);
    }

    @Override
    public void draw() {

        for (int i = 0; i < frame0.length; i++) {
            strip.setPixelColor(Strip.linearMapping[i], frame0[i]);

            frame0[i] = color(red(frame0[i]) * decay, green(frame0[i]) * decay, blue(frame0[i])
                    * decay);
        }
        strip.show();

        // End of draw();
        strip.draw();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event);

        frame0[(int) (map(event.getY(), 0, height, 0, gHeight-1) * gWidth + map(event.getX(), 0,
                width, 0, gWidth-1))] = 0xffffff;
    }
}
