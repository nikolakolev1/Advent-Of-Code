package Days.Day9;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.Scanner;

public class Day9  implements Day {
    public static void main(String[] args) {
        Day day9 = new Day9();
        day9.loadData(Helper.filename(9));
        System.out.println(day9.part1());
        System.out.println(day9.part2());
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
    public long part1() {
        Helper.printAnswer(1, -1);
        return 0;
    }

    @Override
    public long part2() {
        Helper.printAnswer(2, -1);
        return 0;
    }
}