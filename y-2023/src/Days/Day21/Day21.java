package Days.Day21;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day21 implements Day {
    public static void main(String[] args) {
        Day day21 = new Day21();
        day21.loadData(Helper.filename(21));
        System.out.println(day21.part1());
        System.out.println(day21.part2());
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