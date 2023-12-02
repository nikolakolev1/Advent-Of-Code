package Day3;

import Helpers.Helper;

import java.io.File;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) {
        loadData(Helper.filename(3));
        System.out.println(part1());
        System.out.println(part2());
    }

    public static void loadData(String filename) {
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

    public static int part1() {
        return -1;
    }

    public static int part2() {
        return -1;
    }
}