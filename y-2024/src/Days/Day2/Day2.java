package Days.Day2;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 implements Day {
    private List<Report> reports = new ArrayList<>();

    public static void main(String[] args) {
        Day2 day2 = new Day2();
        day2.loadData("data/day2/input.txt");
        System.out.println(day2.part1());
        System.out.println(day2.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                reports.add(new Report(scanner.nextLine()));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;
        for (Report report : reports) {
            if (report.isValid_Part1()) {
                sum++;
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;
        for (Report report : reports) {
            if (report.isValid_Part2()) {
                sum++;
            }
        }

        return String.valueOf(sum);
    }
}