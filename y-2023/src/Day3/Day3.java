package Day3;

import Helpers.Helper;

import java.io.File;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) {
        loadData();

        long p1_start = System.nanoTime();
        int p1_answer = part1();
        long p1_end = System.nanoTime();
        long p1_time = (p1_end - p1_start) / 1000000;

        long p2_start = System.nanoTime();
        int p2_answer = part2();
        long p2_end = System.nanoTime();
        long p2_time = (p2_end - p2_start) / 1000000;

        Helper.printAnswer(1, p1_answer, p1_time);
        Helper.printAnswer(2, p2_answer, p2_time);
    }

    private static void loadData() {
        try {
            File input = new File(Helper.filename(3));
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static int part1() {
        return -1;
    }

    private static int part2() {
        return -1;
    }
}