package Day11;

import Helpers.Helper;

import java.io.File;
import java.util.Scanner;

public class Day11 {
    public static void main(String[] args) {
//        loadData("data.txt");
        part1();
        part2();
    }

    private static void loadData(String filename) {
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

    private static void part1() {
        Helper.printAnswer(1, -1);
    }

    private static void part2() {
        Helper.printAnswer(2, -1);
    }
}