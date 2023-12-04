package Days.Day19;

import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day19 {
    public static void main(String[] args) {
//        loadData("data.txt");
        part1();
        part2();
    }

    public static void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public static int part1() {
        Helper.printAnswer(1, -1);
        return 0;
    }

    public static int part2() {
        Helper.printAnswer(2, -1);
        return 0;
    }
}