package Days.Day2;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 implements Day {
    private final List<Range> ranges = new ArrayList<>();

    public static void main(String[] args) {
        Day day2 = new Day2();
        day2.loadData("data/day2/input.txt");
        System.out.println(day2.part1());
        System.out.println(day2.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] rangesStrArray = scanner.nextLine().split(",");

                for (String s : rangesStrArray) {
                    long rangeMin = Long.parseLong(s.split("-")[0]);
                    long rangeMax = Long.parseLong(s.split("-")[1]);

                    ranges.add(new Range(rangeMin, rangeMax));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        long sum = 0;

        for (Range range : ranges) {
            for (long i = range.min; i <= range.max; i++) {
                int digitCount = countDigits(i);

                if (digitCount % 2 == 0) {
                    int halfLength = digitCount / 2;
                    List<String> parts = splitStringByLength(String.valueOf(i), halfLength);

                    if (checkAllPartsAreEqual(parts)) {
                        sum += i;
                    }
                }
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        long sum = 0;

        for (Range range : ranges) {
            for (long i = range.min; i <= range.max; i++) {
                int digitCount = countDigits(i);
                List<Integer> numberCouleBeSplitIn = divisibleBy(digitCount);

                for (int splitLength : numberCouleBeSplitIn) {
                    List<String> parts = splitStringByLength(String.valueOf(i), splitLength);

                    if (checkAllPartsAreEqual(parts)) {
                        sum += i;
                        break;
                    }
                }
            }
        }

        return String.valueOf(sum);
    }

    private int countDigits(long number) {
        return String.valueOf(number).length();
    }

    private List<Integer> divisibleBy(int digitCount) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = 1; i <= (digitCount / 2); i++) {
            if (digitCount % i == 0) {
                divisors.add(i);
            }
        }
        return divisors;
    }

    private List<String> splitStringByLength(String str, int length) {
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < str.length(); i += length) {
            parts.add(str.substring(i, Math.min(i + length, str.length())));
        }
        return parts;
    }

    private boolean checkAllPartsAreEqual(List<String> parts) {
        if (parts.isEmpty()) return false;

        String firstPart = parts.getFirst();

        for (String part : parts) {
            if (!part.equals(firstPart)) {
                return false;
            }
        }
        return true;
    }

    private record Range(long min, long max) {
    }
}