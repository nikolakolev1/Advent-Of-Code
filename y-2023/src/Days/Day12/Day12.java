package Days.Day12;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day12 implements Day {
    public static void main(String[] args) {
        Day day12 = new Day12();
        day12.loadData(Helper.filename(12));
        System.out.println(day12.part1());
        System.out.println(day12.part2());
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

    @Override
    public String part1() {
        return "to be implemented";
    }

    public String part2() {
        return "to be implemented";
    }
}