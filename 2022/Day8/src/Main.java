import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static void mainMethod(String part) {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                // TODO: Do something
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}