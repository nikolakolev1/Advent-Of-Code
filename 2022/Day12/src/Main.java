import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String part = "Part 1"; // "Part 1" || "Part 2"
        loadData(part);
    }

    private static void loadData(String part) {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                // TODO: Write some code here
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}