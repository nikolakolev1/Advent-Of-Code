package Days.Day8;

import General.Day;

import java.io.File;
import java.util.Scanner;

public class Day8 implements Day {
    public static void main(String[] args) {
        Day day8 = new Day8();
        day8.loadData("data/day8/input_test.txt");
        System.out.println(day8.part1());
        System.out.println(day8.part2());
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
        int sum = 0;

        // TODO: Implement part 1

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        // TODO: Implement part 2

        return String.valueOf(sum);
    }
}