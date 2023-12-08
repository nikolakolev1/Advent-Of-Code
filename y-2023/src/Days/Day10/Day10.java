package Days.Day10;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day10 implements Day {
    public static void main(String[] args) {
        Day day10 = new Day10();
        day10.loadData(Helper.filename(10));
        System.out.println(day10.part1());
        System.out.println(day10.part2());
    }

    public void loadData(String filename) {
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

    public String part1() {
        return "to be implemented";
    }

    public String part2() {
        return "to be implemented";
    }
}