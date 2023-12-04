package Days.Day5;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day5 implements Day {
    public static void main(String[] args) {
        Day day5 = new Day5();
        day5.loadData(Helper.filename(5));
        System.out.println(day5.part1());
        System.out.println(day5.part2());
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
    public int part1() {
        return -1;
    }

    @Override
    public int part2() {
        return -1;
    }
}