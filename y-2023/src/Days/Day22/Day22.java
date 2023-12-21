package Days.Day22;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day22 implements Day {
    public static void main(String[] args) {
        Day day22 = new Day22();
        day22.loadData(Helper.filename(22));
        System.out.println(day22.part1());
        System.out.println(day22.part2());
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