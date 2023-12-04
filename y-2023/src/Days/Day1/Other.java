package Days.Day1;

import General.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is another person's code that I found on the internet and wanted to try out.
 */
public class Other {
    private static String input = "";

    public static void main(String[] args) {
        try {
            loadData();

            long p1_start = System.nanoTime();
            int p1_answer = part1();
            long p1_end = System.nanoTime();
            long p1_time = (p1_end - p1_start) / 1000000;
            Helper.printAnswer(1, p1_answer, p1_time);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void loadData() throws IOException {
        input = Files.readString(Path.of(Helper.filename(1)));
    }

    public static int part1() {
        return input.lines()
                .map(String::chars)
                .map(intStream -> intStream.filter(Character::isDigit).toArray())
                .map(ints -> (char) ints[0] + "" + (char) ints[ints.length - 1])
                .mapToInt(Integer::parseInt)
                .sum();
    }
}