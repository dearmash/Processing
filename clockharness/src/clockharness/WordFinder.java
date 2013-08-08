
package clockharness;

import clockharness.util.Strip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class WordFinder {

    public static void main(String[] args) {

        HashSet<String> lookup = new HashSet<String>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/data/sowpods.txt"))) {

            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                lookup.add(currentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("loaded");

        String[] words = Strip.wordgrid;
        int width = words[0].length();
        int height = words.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                for (int dx = -3; dx <= 3; dx++) {
                    for (int dy = -3; dy <= 3; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue;// not going anywhere...
                        }

                        // System.out.println("(" + x + ", " + y + ")");
                        // System.out.println("[" + dx + ", " + dy + "]");

                        StringBuilder word = new StringBuilder();

                        int cx = x;
                        int cy = y;
                        while (cx >= 0 && cx < width && cy >= 0 && cy < height) {

                            word.append(words[cy].charAt(cx));
                            if (word.length() >= 3
                                    && lookup.contains(word.toString().toUpperCase())) {
                                System.out.println(word + ": (" + x + " + " + dx + ", " + y + " + "
                                        + dy + ")");
                            }

                            cx += dx;
                            cy += dy;

                        }

                    }
                }

            }
        }

        System.out.println("Done");
    }
}
