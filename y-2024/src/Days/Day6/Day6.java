package Days.Day6;

import General.Day;

import java.io.File;
import java.util.Scanner;

public class Day6 implements Day {
    public static void main(String[] args) {
        Day day6 = new Day6();
        day6.loadData("data/day6/input_test.txt");
        System.out.println(day6.part1());
        System.out.println(day6.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                // TODO: Load data
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        return "Part 1";
    }

    @Override
    public String part2() {
        return "Part 2";
    }
}