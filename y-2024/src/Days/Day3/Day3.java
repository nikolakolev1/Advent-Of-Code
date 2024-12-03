package Days.Day3;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 implements Day {
    private List<String> input = new ArrayList<>();

    public static void main(String[] args) {
        Day day3 = new Day3();
        day3.loadData("data/day3/input.txt");
        System.out.println(day3.part1());
        System.out.println(day3.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());

        }
    }

    @Override
    public String part1() {
        return null;
    }

    @Override
    public String part2() {
        return null;
    }
}