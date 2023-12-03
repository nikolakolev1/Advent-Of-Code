package Day8;

import Helpers.Day;
import Helpers.Helper;

import java.io.File;
import java.util.Scanner;

public class Day8 implements Day {
    public static void main(String[] args) {
        Day day8 = new Day8();
        day8.loadData(Helper.filename(8));
        System.out.println(day8.part1());
        System.out.println(day8.part2());
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
        Helper.printAnswer(1, -1);
        return 0;
    }

    @Override
    public int part2() {
        Helper.printAnswer(2, -1);
        return 0;
    }
}