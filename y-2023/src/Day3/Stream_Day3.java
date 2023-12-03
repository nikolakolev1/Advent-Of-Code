package Day3;

import Helpers.Day;
import Helpers.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Stream_Day3 implements Day {
    private final ArrayList<char[]> lines = new ArrayList<>();
    private static final int NUMBER = 0, X_END = 2;

    public static void main(String[] args) {
        Day day3 = new Stream_Day3();
        day3.loadData(Helper.filename(3));

        long startTime = System.nanoTime();
        System.out.println(day3.part1());
        long endTime = System.nanoTime();
        System.out.println("Part 1 time: " + (endTime - startTime) / 1000000 + "ms");

        System.out.println(day3.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line.toCharArray());
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public int part1() {
        return findAllNumbers().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public int part2() {
        return findAllGears().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private List<Integer> findAllNumbers() {
        List<Integer> numbers = new ArrayList<>();

        int allLines = lines.size();

        for (int line = 0; line < allLines; line++) {
            char[] currentLine = lines.get(line);
            int allChars = currentLine.length;

            for (int character = 0; character < allChars; character++) {
                char currentChar = currentLine[character];

                if (currentChar != '.' && !Character.isDigit(currentChar)) {
                    List<Integer> adjacentNumbers = getAdjacentNumbers(line, character);
                    if (!adjacentNumbers.isEmpty()) numbers.addAll(adjacentNumbers);
                }
            }
        }

        return numbers;
    }

    private List<Integer> findAllGears() {
        List<Integer> gears = new ArrayList<>();

        int allLines = lines.size();

        for (int line = 0; line < allLines; line++) {
            char[] currentLine = lines.get(line);
            int allChars = currentLine.length;

            for (int character = 0; character < allChars; character++) {
                char currentChar = currentLine[character];

                if (currentChar == '*') {
                    List<Integer> adjacentNumbers = getAdjacentNumbers(line, character);
                    if (adjacentNumbers.size() == 2) gears.add(adjacentNumbers.get(0) * adjacentNumbers.get(1));
                }
            }
        }

        return gears;
    }

    private List<Integer> getAdjacentNumbers(int y, int x) {
        List<Integer> adjacentNumbers = new ArrayList<>();

        // top line
        if (y > 0) {
            List<Integer> topLineNumbers = getAdjacent_TopLine(y, x);
            if (topLineNumbers != null) adjacentNumbers.addAll(topLineNumbers);
        }

        // current line
        List<Integer> currentLineNumbers = getAdjacent_CurrentLine(y, x);
        if (currentLineNumbers != null) adjacentNumbers.addAll(currentLineNumbers);

        // bottom line
        if (y < lines.size() - 1) {
            List<Integer> bottomLineNumbers = getAdjacent_BottomLine(y, x);
            if (bottomLineNumbers != null) adjacentNumbers.addAll(bottomLineNumbers);
        }

        return adjacentNumbers;
    }

    private List<Integer> getAdjacent_TopLine(int y, int x) {
        char[] topLine = lines.get(y - 1);

        // Similarity between this and getAdjacent_BottomLine => abstract into a method
        int[] num1 = null, num2 = null;

        if (x != 0 && Character.isDigit(topLine[x - 1])) {
            num1 = buildNumber(y - 1, x - 1);
        }

        if (num1 != null) {
            if (x != topLine.length - 1 && num1[X_END] < x + 1 && Character.isDigit(topLine[x + 1])) {
                num2 = buildNumber(y - 1, x + 1);
            }
        } else {
            if (Character.isDigit(topLine[x])) {
                num1 = buildNumber(y - 1, x);
            } else if (Character.isDigit(topLine[x + 1])) {
                num1 = buildNumber(y - 1, x + 1);
            }
        }

        if (num1 != null) {
            if (num2 != null) {
                return List.of(num1[NUMBER], num2[NUMBER]);
            } else {
                return List.of(num1[NUMBER]);
            }
        } else {
            return null;
        }
    }

    private List<Integer> getAdjacent_CurrentLine(int y, int x) {
        char[] currentLine = lines.get(y);

        int[] numLeft = null, numRight = null;

        if (x != 0 && Character.isDigit(currentLine[x - 1])) {
            numLeft = buildNumber(y, x - 1);
        }

        if (x != currentLine.length - 1 && Character.isDigit(currentLine[x + 1])) {
            numRight = buildNumber(y, x + 1);
        }

        if (numLeft != null) {
            if (numRight != null) {
                return List.of(numLeft[NUMBER], numRight[NUMBER]);
            } else {
                return List.of(numLeft[NUMBER]);
            }
        } else {
            if (numRight != null) {
                return List.of(numRight[NUMBER]);
            } else {
                return null;
            }
        }
    }

    private List<Integer> getAdjacent_BottomLine(int y, int x) {
        char[] bottomLine = lines.get(y + 1);

        // Similarity between this and getAdjacent_BottomLine => abstract into a method
        int[] num1 = null, num2 = null;

        if (x != 0 && Character.isDigit(bottomLine[x - 1])) {
            num1 = buildNumber(y + 1, x - 1);
        }

        if (num1 != null) {
            if (x != bottomLine.length - 1 && num1[X_END] < x + 1 && Character.isDigit(bottomLine[x + 1])) {
                num2 = buildNumber(y + 1, x + 1);
            }
        } else {
            if (Character.isDigit(bottomLine[x])) {
                num1 = buildNumber(y + 1, x);
            } else if (Character.isDigit(bottomLine[x + 1])) {
                num1 = buildNumber(y + 1, x + 1);
            }
        }

        if (num1 != null) {
            if (num2 != null) {
                return List.of(num1[NUMBER], num2[NUMBER]);
            } else {
                return List.of(num1[NUMBER]);
            }
        } else {
            return null;
        }
    }

    private int[] buildNumber(int y, int x) {
        char[] line = lines.get(y);

        int numberStart = x, numberEnd = x;
        for (int i = x; i >= 0; i--) {
            if (Character.isDigit(line[i])) {
                numberStart = i;
            } else {
                break;
            }
        }
        for (int i = x; i < line.length; i++) {
            if (Character.isDigit(line[i])) {
                numberEnd = i;
            } else {
                break;
            }
        }

        StringBuilder number = new StringBuilder();
        for (int i = numberStart; i <= numberEnd; i++) {
            number.append(line[i]);
        }

        return new int[]{Integer.parseInt(number.toString()), numberStart, numberEnd};
    }
}