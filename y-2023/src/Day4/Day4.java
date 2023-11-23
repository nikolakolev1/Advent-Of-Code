package Day4;

import Helpers.Helper;

import java.io.File;
import java.util.Scanner;

public class Day4 {
    public static void main(String[] args) {
//        loadData();
        part1();
        part2();
    }

    private static void loadData() {
        try {
            File input = new File(Helper.filename(4));
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