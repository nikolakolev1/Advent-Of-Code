package Day1;

import Helpers.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day1 {
    private static final ArrayList<String> lines = new ArrayList<>();
    private static final int VALUE = 0, INDEX = 1;
    private static final String[] SPELLED_DIGITS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}; // 1-9, 0 is not included

    public static void main(String[] args) {
        loadData();
        part1();
        part2();
    }

    private static void loadData() {
        try {
            File input = new File(Helper.filename(1));
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void part1() {
        int sum = 0;

        for (String line : lines) {
            int num = extractNumber_NumericOnly(line);
            sum += num;
        }

        Helper.printAnswer(1, sum);
    }

    private static void part2() {
        int sum = 0, num;

        for (String line : lines) {
            if (hasSpelledDigit(line)) {
                int[] firstSpelledDigit = getFirstSpelledDigit(line);
                int[] lastSpelledDigit = getLastSpelledDigit(line);

                int[] firstNumericDigit = getFirstNumericDigit(line);
                int[] lastNumericDigit = getLastNumericDigit(line);

                int firstDigit = firstSpelledDigit[INDEX] < firstNumericDigit[INDEX] ? firstSpelledDigit[VALUE] : firstNumericDigit[VALUE];
                int lastDigit = lastSpelledDigit[INDEX] > lastNumericDigit[INDEX] ? lastSpelledDigit[VALUE] : lastNumericDigit[VALUE];

                int[] digits = new int[]{firstDigit, lastDigit};
                num = buildInteger(digits);
            } else {
                num = extractNumber_NumericOnly(line);
            }

            sum += num;
        }

        Helper.printAnswer(2, sum);
    }

    private static int[] getFirstNumericDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return new int[]{Character.getNumericValue(str.charAt(i)), i};
            }
        }

        throw new IllegalArgumentException("No digit found in string");
    }

    private static int[] getLastNumericDigit(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                return new int[]{Character.getNumericValue(str.charAt(i)), i};
            }
        }

        throw new IllegalArgumentException("No digit found in string");
    }

    private static boolean hasSpelledDigit(String str) {
        for (String spelledDigit : SPELLED_DIGITS) {
            if (str.contains(spelledDigit)) {
                return true;
            }
        }

        return false;
    }

    private static int[] getFirstSpelledDigit(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            if (Character.isDigit(str.charAt(i))) {
                continue;
            }

            for (int j = 0; j < SPELLED_DIGITS.length; j++) {
                if (i + SPELLED_DIGITS[j].length() <= str.length()) {
                    if (str.startsWith(SPELLED_DIGITS[j], i)) {
                        return new int[]{j + 1, i};
                    }
                }
            }
        }

        throw new IllegalArgumentException("No spelled digit found in string");
    }

    private static int[] getLastSpelledDigit(String str) {
        for (int i = str.length() - 3; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                i -= 2;
            } else {
                int end = str.length();
                if (i + 5 < str.length()) {
                    end = i + 5;
                }

                for (int j = 0; j < SPELLED_DIGITS.length; j++) {
                    if (str.substring(i, end).contains(SPELLED_DIGITS[j])) {
                        return new int[]{j + 1, i};
                    }
                }
            }
        }

        throw new IllegalArgumentException("No spelled digit found in string");
    }

    private static int extractNumber_NumericOnly(String str) {
        int[] firstDigit = getFirstNumericDigit(str);
        int[] lastDigit = getLastNumericDigit(str);

        int[] digits = new int[]{firstDigit[VALUE], lastDigit[VALUE]};
        return buildInteger(digits);
    }

    private static int buildInteger(int[] digits) {
        int num = 0;

        for (int digit : digits) {
            num *= 10;
            num += digit;
        }

        return num;
    }
}