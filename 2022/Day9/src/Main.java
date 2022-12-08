import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static void mainMethod() {

    }

    public static void loadData() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisRow = myScanner.nextLine();

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}