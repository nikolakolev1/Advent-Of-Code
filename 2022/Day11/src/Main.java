import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        loadData();
    }

    private static void loadData() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();
                mainMethod(thisLine);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mainMethod(String thisLine) {
        // TODO
    }
}