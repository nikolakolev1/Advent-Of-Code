package Days.Day19;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day19 implements Day {
    public static void main(String[] args) {
        Day day19 = new Day19();
        day19.loadData(Helper.filename(19));
        System.out.println(day19.part1());
        System.out.println(day19.part2());
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