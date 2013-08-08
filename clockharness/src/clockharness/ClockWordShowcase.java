
package clockharness;

import clockharness.util.Strip;
import processing.core.PApplet;

import java.util.Date;
import java.util.Random;

@SuppressWarnings("serial")
public class ClockWordShowcase extends PApplet {

    public static void main(String[] args) {
        PApplet.main(ClockWordShowcase.class.getName());
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

            // for (int i = 0; i < ClockTakeOne.words.length; i++) {
            // showWord(i);
            // }

            // for (int i = 0; i < strip.wordgrid.length; i++) {
            // for (int j = 0; j < strip.wordgrid[0].length(); j++) {
            // if (strip.wordgrid[i].charAt(j) == 'm') {
            // strip.setPixelColor(strip.mapping[i][j], 0xff0000);
            // } else if (strip.wordgrid[i].charAt(j) == 'c') {
            // strip.setPixelColor(strip.mapping[i][j], 0x0000ff);
            // }
            // }
            // }

            Random r = new Random();
            for (int i = 0; i < ClockTakeOne.words.length; i++) {
                int s = (int) map(i, 0, ClockTakeOne.words.length, 0, 255);
                s = r.nextInt(255);
                showWord(i, PlasmaSketch.getRGB(s, 255 - s / 2, 255));
            }

            currentWord = (currentWord + 1) % ClockTakeOne.words.length;

            strip.show();
        }

        // End of draw();
        strip.draw();
    }

    public void showSentence(int[] wordList) {
        for (int i = 0; i < wordList.length; i++) {
            showWord(wordList[i]);
        }
    }

    public void showWord(int wordIdx) {
        showWord(wordIdx, 0xffffff);
    }

    public void showWord(int wordIdx, int color) {
        int[] word = ClockTakeOne.words[wordIdx];
        for (int i = 0; i < word.length; i++) {
            strip.setPixelColor(word[i], color);
        }
    }

}
