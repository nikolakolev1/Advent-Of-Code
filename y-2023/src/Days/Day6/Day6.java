package Days.Day6;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day6 implements Day {
    public static void main(String[] args) {
        Day day6 = new Day6();
        day6.loadData(Helper.filename(6));
        System.out.println(day6.part1());
        System.out.println(day6.part2());
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