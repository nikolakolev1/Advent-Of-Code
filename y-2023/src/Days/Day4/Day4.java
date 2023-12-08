package Days.Day4;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Day4 implements Day {
    private final List<HashSet<Integer>> winningNums = new ArrayList<>();
    private final List<ArrayList<Integer>> yourNums = new ArrayList<>();
    private final List<Integer> copies = new ArrayList<>();
    private static final int WINNING = 0, YOUR = 1;

    public static void main(String[] args) {
        Day day4 = new Day4();
        day4.loadData(Helper.filename(4));
        System.out.println(day4.part1());
        System.out.println(day4.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] nums = line.split("\\|");

                String[] winning = nums[WINNING].split(":")[1].split(" ");
                HashSet<Integer> winningSet = new HashSet<>();
                for (String num : winning) {
                    if (!num.isBlank()) winningSet.add(Integer.parseInt(num.trim()));
                }

                String[] your = nums[YOUR].split(" ");
                ArrayList<Integer> yourList = new ArrayList<>();
                for (String num : your) {
                    if (!num.isBlank()) yourList.add(Integer.parseInt(num.trim()));
                }

                winningNums.add(winningSet);
                yourNums.add(yourList);
                copies.add(1); // for part 2
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        int scratchcards = winningNums.size(); // to avoid calling size() every iteration

        for (int i = 0; i < scratchcards; i++) {
            int matches = countMatches(winningNums.get(i), yourNums.get(i));

            if (matches > 0) {
                sum += (int) Math.pow(2, matches - 1);
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int scratchcards = winningNums.size(); // to avoid calling size() every iteration

        for (int i = 0; i < scratchcards; i++) {
            int matches = countMatches(winningNums.get(i), yourNums.get(i));
            matches = Math.min(matches, copies.size() - i - 1); // to avoid IndexOutOfBoundsException

            for (int j = 1; j <= matches; j++) {
                copies.set(i + j, (copies.get(i + j) + copies.get(i)));
            }
        }

        // return the sum of all copies
        return String.valueOf(copies.stream()
                .reduce(0, Integer::sum));
    }

    private static int countMatches(HashSet<Integer> winningSet, ArrayList<Integer> yourList) {
        int matches = 0;

        for (int num : yourList) {
            if (winningSet.contains(num)) {
                matches++;
            }
        }

        return matches;
    }
}