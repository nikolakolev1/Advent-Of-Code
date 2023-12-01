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

    /**
     * Returns the first numeric digit in the string and its index
     *
     * @param str = String to search
     * @return = int[] {digit, index}
     */
    private static int[] getFirstNumericDigit(String str) {
        // Loop through the string and return the first numeric character
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return new int[]{Character.getNumericValue(str.charAt(i)), i};
            }
        }

        throw new IllegalArgumentException("No digit found in string");
    }

    /**
     * Returns the last numeric digit in the string and its index
     *
     * @param str = String to search
     * @return = int[] {digit, index}
     */
    private static int[] getLastNumericDigit(String str) {
        // Loop through the string backwards and return the first numeric character
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                return new int[]{Character.getNumericValue(str.charAt(i)), i};
            }
        }

        throw new IllegalArgumentException("No digit found in string");
    }

    /**
     * Returns true if the string contains a spelled digit
     *
     * @param str = String to search
     * @return = boolean of whether the string contains a spelled digit
     */
    private static boolean hasSpelledDigit(String str) {
        // For each spelled digit, check if the string contains it
        for (String spelledDigit : SPELLED_DIGITS) {
            if (str.contains(spelledDigit)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the first spelled digit in the string and its index
     *
     * @param str = String to search
     * @return = int[] {digit, index}
     */
    private static int[] getFirstSpelledDigit(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            // Skip if the character is a digit (a spelled digit cannot contain a numeric)
            if (Character.isDigit(str.charAt(i))) {
                continue;
            }

            // For each spelled digit, check if the string starts with it
            for (int j = 0; j < SPELLED_DIGITS.length; j++) {
                // Make sure the spelled digit doesn't go out of bounds
                if (i + SPELLED_DIGITS[j].length() <= str.length() && str.startsWith(SPELLED_DIGITS[j], i)) {
                    return new int[]{j + 1, i};
                }
            }
        }

        throw new IllegalArgumentException("No spelled digit found in string");
    }

    /**
     * Returns the last spelled digit in the string and its index
     *
     * @param str = String to search
     * @return = int[] {digit, index}
     */
    private static int[] getLastSpelledDigit(String str) {
        for (int i = str.length() - 3; i >= 0; i--) {
            // Skip if the character is a digit (a spelled digit cannot contain a numeric)
            if (Character.isDigit(str.charAt(i))) {
                i -= 2;
                continue;
            }

            // Make sure the spelled digit doesn't go out of bounds
            int end = str.length();
            if (i + 5 < str.length()) {
                end = i + 5;
            }

            // For each spelled digit, check if the string has it
            for (int j = 0; j < SPELLED_DIGITS.length; j++) {
                if (str.substring(i, end).contains(SPELLED_DIGITS[j])) {
                    return new int[]{j + 1, i};
                }
            }
        }

        throw new IllegalArgumentException("No spelled digit found in string");
    }

    /**
     * Extracts the number from the string, without checking for spelled digits
     *
     * @param str = String to extract number from
     * @return = the extracted number as an int
     */
    private static int extractNumber_NumericOnly(String str) {
        int[] firstDigit = getFirstNumericDigit(str);
        int[] lastDigit = getLastNumericDigit(str);

        int[] digits = new int[]{firstDigit[VALUE], lastDigit[VALUE]};
        return buildInteger(digits);
    }

    /**
     * Builds an integer from an array of digits
     *
     * @param digits = array of digits
     * @return = the integer
     */
    private static int buildInteger(int[] digits) {
        int num = 0;

        // For each digit, move one place to the left and add the digit
        for (int digit : digits) {
            num *= 10;
            num += digit;
        }

        return num;
    }
}