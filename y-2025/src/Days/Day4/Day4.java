package Days.Day4;

import General.Day;

import java.io.File;
import java.util.Scanner;

public class Day4 implements Day {
    private static boolean[][] paperRolls;

    public static void main(String[] args) {
        Day day4 = new Day4();
        day4.loadData("data/day4/input.txt");
        System.out.println(day4.part1());
        System.out.println(day4.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int row = 0;

            String line = scanner.nextLine();

            paperRolls = new boolean[line.length()][line.length()];

            for (int i = 0; i < paperRolls.length; i++) {
                if (line.charAt(i) == '.') {
                    paperRolls[row][i] = false;
                } else if (line.charAt(i) == '@') {
                    paperRolls[row][i] = true;
                } else {
                    throw new Exception("Invalid character in input file");
                }
            }
            row++;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();

                for (int i = 0; i < paperRolls.length; i++) {
                    if (line.charAt(i) == '.') {
                        paperRolls[row][i] = false;
                    } else if (line.charAt(i) == '@') {
                        paperRolls[row][i] = true;
                    } else {
                        throw new Exception("Invalid character in input file");
                    }
                }
                row++;
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (int row = 0; row < paperRolls.length; row++) {
            for (int col = 0; col < paperRolls[0].length; col++) {
                if (paperRolls[row][col]) {
                    int adjacentCount = numberOfAdjacentPaperRolls(row, col);
                    if (adjacentCount < 4) {
                        sum++;
                    }
                }
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        int removedThisRound = 0;

        for (int row = 0; row < paperRolls.length; row++) {
            for (int col = 0; col < paperRolls[0].length; col++) {
                if (paperRolls[row][col]) {
                    int adjacentCount = numberOfAdjacentPaperRolls(row, col);
                    if (adjacentCount < 4) {
                        paperRolls[row][col] = false;
                        removedThisRound++;
                        sum++;
                    }
                }
            }

            if (row == paperRolls.length - 1 && removedThisRound > 0) {
                row = -1; // Reset to start from the beginning
                removedThisRound = 0;
            }
        }

        return String.valueOf(sum);
    }

    private static int numberOfAdjacentPaperRolls(int row, int col) {
        int count = 0;
        int[] directions = {-1, 0, 1};

        for (int dr : directions) {
            for (int dc : directions) {
                if (dr == 0 && dc == 0) continue; // Skip the current cell
                int newRow = row + dr;
                int newCol = col + dc;

                if (newRow >= 0 && newRow < paperRolls.length &&
                    newCol >= 0 && newCol < paperRolls[0].length &&
                    paperRolls[newRow][newCol]) {
                    count++;
                }
            }
        }

        return count;
    }
}