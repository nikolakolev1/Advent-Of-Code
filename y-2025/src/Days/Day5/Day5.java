package Days.Day5;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day5 implements Day {
    private ArrayList<Range> ranges = new ArrayList<>();
    private ArrayList<Long> ingredientIDs = new ArrayList<>();

    public static void main(String[] args) {
        Day day5 = new Day5();
        day5.loadData("data/day5/input.txt");
        System.out.println(day5.part1());
        System.out.println(day5.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            boolean inRangesSection = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    inRangesSection = false;
                    continue;
                }

                if (inRangesSection) {
                    String[] parts = line.split("-");
                    long start = Long.parseLong(parts[0]);
                    long end = Long.parseLong(parts[1]);
                    ranges.add(new Range(start, end));
                } else {
                    ingredientIDs.add(Long.parseLong(line));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (Long ingredientID : ingredientIDs) {
            for (Range range : ranges) {
                if (range.contains(ingredientID)) {
                    sum++;
                    break;
                }
            }

        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        // TODO: Implement part 2

        return String.valueOf(sum);
    }

    private record Range(long start, long end) {
        boolean contains(long number) {
            return number >= start && number <= end;
        }
    }
}