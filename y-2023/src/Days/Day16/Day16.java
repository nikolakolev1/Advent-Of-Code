package Days.Day16;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day16 implements Day {
    public static void main(String[] args) {
        Day day16 = new Day16();
        day16.loadData(Helper.filename(16));
        System.out.println(day16.part1());
        System.out.println(day16.part2());
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