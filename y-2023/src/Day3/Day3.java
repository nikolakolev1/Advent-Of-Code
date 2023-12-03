package Day3;

import Helpers.Day;
import Helpers.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 implements Day {
    private final ArrayList<char[]> lines = new ArrayList<>();
    private static final int NUMBER = 0, NUM_END = 1;

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
                lines.add(scanner.nextLine().toCharArray());
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    /*
     * Pseudocode (part 1):
     * 1) go through each line
     * 2) go through each character in the line
     * 3) if the character is a symbol (not a dot or a number)
     *     3.1) get adjacent numbers on top, current, and bottom lines
     * 4) if the character is a number or a dot - ignore
     *
     * Notes:
     * - this doesn't work for numbers that are covered more than once by a symbol
     */
    @Override
    public int part1() {
        int sum = 0;

        List<Integer> nums = findAllNums();
        for (Integer num : nums) {
            sum += num;
        }

        return sum;
    }

    @Override
    public int part2() {
        int sum = 0;

        List<Integer> gears = findAllGears();
        for (Integer gear : gears) {
            sum += gear;
        }

        return sum;
    }

    private List<Integer> findAllNums() {
        List<Integer> nums = new ArrayList<>();

        int allLines = lines.size(); // Avoid calling lines.size() in the loop

        for (int y = 0; y < allLines; y++) {
            char[] line = lines.get(y); // Avoid calling lines.get() in the loop

            for (int x = 0; x < line.length; x++) {
                char character = line[x];

                if (character != '.' && !Character.isDigit(character)) {
                    List<Integer> adjacentNums = getAdjacentNums(y, x);
                    if (!adjacentNums.isEmpty()) nums.addAll(adjacentNums);
                }
            }
        }

        return nums;
    }

    private List<Integer> findAllGears() {
        List<Integer> gears = new ArrayList<>();

        int allLines = lines.size(); // Avoid calling lines.size() in the loop

        for (int y = 0; y < allLines; y++) {
            char[] line = lines.get(y); // Avoid calling lines.get() in the loop

            for (int x = 0; x < line.length; x++) {
                char character = line[x];

                if (character == '*') {
                    List<Integer> adjacentNums = getAdjacentNums(y, x);
                    if (adjacentNums.size() == 2) gears.add(adjacentNums.get(0) * adjacentNums.get(1));
                }
            }
        }

        return gears;
    }

    private List<Integer> getAdjacentNums(int y, int x) {
        List<Integer> adjNums = new ArrayList<>();

        // top line
        if (y > 0) {
            List<Integer> topLineNums = getAdjacent_TopOrBottomLine(y - 1, x);
            if (topLineNums != null) adjNums.addAll(topLineNums);
        }

        // current line
        List<Integer> thisLineNums = getAdjacent_CurrentLine(y, x);
        if (thisLineNums != null) adjNums.addAll(thisLineNums);

        // bottom line
        if (y < lines.size() - 1) {
            List<Integer> bottomLineNums = getAdjacent_TopOrBottomLine(y + 1, x);
            if (bottomLineNums != null) adjNums.addAll(bottomLineNums);
        }

        return adjNums;
    }

    private List<Integer> getAdjacent_TopOrBottomLine(int y, int x) {
        char[] line = lines.get(y);

        int[] num1 = null, num2 = null;

        if (x != 0 && Character.isDigit(line[x - 1])) {
            num1 = buildNum(y, x - 1);
        }

        if (num1 != null) {
            if (x != line.length - 1 && num1[NUM_END] < x + 1 && Character.isDigit(line[x + 1])) {
                num2 = buildNum(y, x + 1);
            }
        } else {
            if (Character.isDigit(line[x])) {
                num1 = buildNum(y, x);
            } else if (Character.isDigit(line[x + 1])) {
                num1 = buildNum(y, x + 1);
            }
        }

        return listOfNums(num1, num2);
    }

    private List<Integer> getAdjacent_CurrentLine(int y, int x) {
        char[] line = lines.get(y);

        int[] numLeft = null, numRight = null;

        if (x != 0 && Character.isDigit(line[x - 1])) {
            numLeft = buildNum(y, x - 1);
        }

        if (x != line.length - 1 && Character.isDigit(line[x + 1])) {
            numRight = buildNum(y, x + 1);
        }

        return listOfNums(numLeft, numRight);
    }

    /**
     * Builds a number from a given line and an index of any digit in the number.
     *
     * @param y = line index
     * @param x = index of ANY digit in the number
     * @return int[] = {number, index of last digit in the number}
     */
    private int[] buildNum(int y, int x) {
        char[] line = lines.get(y);

        int start = x, end = x;

        // find the start and end of the num
        while (start > 0 && Character.isDigit(line[start - 1])) {
            start--;
        }
        while (end < line.length - 1 && Character.isDigit(line[end + 1])) {
            end++;
        }

        // build the number
        StringBuilder num = new StringBuilder();
        for (int i = start; i <= end; i++) {
            num.append(line[i]);
        }

        return new int[]{Integer.parseInt(num.toString()), end};
    }

    private List<Integer> listOfNums(int[]... nums) {
        List<Integer> list = new ArrayList<>();

        for (int[] num : nums) {
            if (num != null) list.add(num[NUMBER]);
        }

        return !list.isEmpty() ? list : null;
    }
}