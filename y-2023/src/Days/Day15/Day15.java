package Days.Day15;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day15 implements Day {
    public static void main(String[] args) {
        Day day15 = new Day15();
        day15.loadData(Helper.filename(15));
        System.out.println(day15.part1());
        System.out.println(day15.part2());
    }

    @Override
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

    @Override
    public String part2() {
        return "to be implemented";
    }
}