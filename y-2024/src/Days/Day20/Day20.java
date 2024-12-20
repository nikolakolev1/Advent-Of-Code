package Days.Day20;

import General.Day;

import java.io.File;
import java.util.Scanner;

public class Day20 implements Day {
    public static void main(String[] args) {
        Day day20 = new Day20();
        day20.loadData("data/day20/input_test.txt");
        System.out.println(day20.part1());
        System.out.println(day20.part2());
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