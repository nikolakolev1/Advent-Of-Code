package Day3;

import Helpers.Helper;
import Helpers.Day;

import java.io.File;
import java.util.Scanner;

public class Day3 implements Day {
    public static void main(String[] args) {
        Day day3 = new Day3();
        day3.loadData(Helper.filename(3));
        System.out.println(day3.part1());
        System.out.println(day3.part2());
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