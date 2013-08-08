
package clockharness;

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

    public static final int ITS = 0;
    public static final int IT_IS = 1;
    public static final int ALMOST = 2;
    public static final int FIVE_1 = 3;
    public static final int TEN_1 = 4;
    public static final int QUARTER = 5;
    public static final int TWENTY = 6;
    public static final int TWENTY_FIVE = 7;
    public static final int HALF = 8;
    public static final int MINUTES = 9;
    public static final int PAST = 10;
    public static final int TO = 11;
    public static final int ZERO = 12;
    public static final int ONE = 13;
    public static final int TWO = 14;
    public static final int THREE = 15;
    public static final int FOUR = 16;
    public static final int FIVE = 17;
    public static final int SIX = 18;
    public static final int SEVEN = 19;
    public static final int EIGHT = 20;
    public static final int NINE = 21;
    public static final int TEN = 22;
    public static final int ELEVEN = 23;
    public static final int TWELVE = 24;
    public static final int OCLOCK = 25;
    public static final int ELEVENTY = 26;
    public static final int BEER = 27;
    public static final int SHIRTLESS = 28;
    public static final int MOTO = 29;
    public static final int MAKER = 30;
    public static final int QNI = 31;
    public static final int TIME = 32;
    public static final int WEEEE = 33;
    public static final int ARE = 34;
    public static final int YOU = 35;
    public static final int READY = 36;
    public static final int YET = 37;
    public static final int SET = 38;
    public static final int LETS = 39;
    public static final int GO = 40;
    public static final int ART = 41;
    public static final int GET = 42;
    public static final int BUSY = 43;
    public static final int OLD = 44;

    // @formatter:off
    public static final int words[][] = {
            { 0, 19, 20 }, // its
            { 0, 19, 40, 59 }, // it is
            { 100, 119, 120, 139, 140, 159 }, // almost
            { 117, 122, 137, 142 }, // five
            { 1, 18, 21 }, // ten
            { 17, 22, 37, 42, 57, 62, 77 }, // quarter
            { 58, 61, 78, 81, 98, 101 }, // twenty
            { 58, 61, 78, 81, 98, 101, 117, 122, 137, 142 }, // twenty five
            { 121, 138, 141, 158 }, // half
            { 3, 16, 23, 36, 43, 56, 63 }, // minutes
            { 116, 123, 136, 143 }, // past
            { 143, 156 }, // to
            { 46, 52, 68, 70 }, // zero
            { 33, 47, 51 }, // one
            { 25, 26, 27 }, // two
            { 106, 113, 126, 133, 146 }, // three
            { 34, 33, 32, 31 }, // four
            { 115, 124, 135, 144 }, // five
            { 49, 50, 69 }, // six
            { 14, 13, 12, 11, 10 }, // seven
            { 53, 66, 73, 86, 93 }, // eight
            { 108, 111, 128, 131 }, // nine
            { 106, 107, 108 }, // ten
            { 87, 92, 107, 112, 127, 132 }, // eleven
            { 35, 44, 55, 64, 75, 84 }, // twelve
            { 109, 110, 129, 130, 149, 150 }, // oclock
            { 87, 92, 107, 112, 127, 132, 147, 152 }, // eleventy
            { 5, 6, 7, 8 }, // beer
            { 54, 65, 74, 85, 94, 105, 114, 125, 134 }, // shirtless
            { 3, 15, 25, 33}, // moto
            { 82, 96, 104, 114, 126 }, // maker
            { 9, 10, 29 }, // qni
            { 1, 2, 3, 4 }, // time
            { 44, 56, 62, 78, 80 }, // weeee
            { 60, 79, 80 }, // are
            { 101, 102, 103 }, // you
            { 45, 53, 67, 71, 89 }, // ready
            { 145, 146, 147 }, // yet
            { 125, 133, 147 }, // set
            { 129, 131, 147, 153 }, // lets
            { 148, 151 }, // go
            { 72, 88, 90 }, // art
            { 73, 87, 91 }, // get
            { 155, 154, 153, 152 }, // busy
            { 139, 141, 157 }, // old
    };
    // @formatter:on

    int prev = 0;

    int currentWord = 0;

    @Override
    public void draw() {

        Date now = new Date();

        int currentMillis = millis();
        if (prev + 1000 < currentMillis) {
            prev = currentMillis;

            strip.begin();

            showWord(IT_IS);

            switch (abs(6 - now.getMinutes() / 5)) {
                case 0:
                    showWord(HALF, 0x00ff00);
                    break;
                case 1:
                    showWord(TWENTY_FIVE, 0x00ff00);
                    showWord(MINUTES);
                    break;
                case 2:
                    showWord(TWENTY, 0x00ff00);
                    showWord(MINUTES);
                    break;
                case 3:
                    showWord(QUARTER, 0x00ff00);
                    break;
                case 4:
                    showWord(TEN_1, 0x00ff00);
                    showWord(MINUTES);
                    break;
                case 5:
                    showWord(FIVE_1, 0x00ff00);
                    showWord(MINUTES);
                    break;
            }

            if (now.getMinutes() >= 35) {
                showWord(TO);
                showWord((now.getHours() + 12) % 12 + ONE, 0x00ffff);
            } else if (now.getMinutes() >= 5) {
                showWord(PAST);
                showWord((now.getHours() + 11) % 12 + ONE, 0x00ffff);
            } else {
                showWord((now.getHours() + 11) % 12 + ONE, 0x00ffff);
            }

            showWord(OCLOCK);

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
        int[] word = words[wordIdx];
        for (int i = 0; i < word.length; i++) {
            strip.setPixelColor(word[i], color);
        }
    }

}
