package Days.Day4;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Stream_Day4 implements Day {
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
                String[] nums = scanner.nextLine().split("\\|");

                HashSet<Integer> winningSet = new HashSet<>();
                Arrays.stream(nums[WINNING].split(":")[1].split(" "))
                        .filter(num -> !num.isBlank())
                        .forEach(num -> winningSet.add(Integer.parseInt(num.trim())));

                ArrayList<Integer> yourList = new ArrayList<>();
                Arrays.stream(nums[YOUR].split(" "))
                        .filter(num -> !num.isBlank())
                        .forEach(num -> yourList.add(Integer.parseInt(num.trim())));

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
    public long part1() {
        return winningNums.stream()
                .mapToInt(winning -> countMatches(winning, yourNums.get(winningNums.indexOf(winning))))
                .map(matches -> (matches > 0) ? (int) Math.pow(2, matches - 1) : 0)
                .sum();
    }

    @Override
    public long part2() {
        winningNums.forEach(winning -> {
            int i = winningNums.indexOf(winning);
            int matches = Math.min(countMatches(winning, yourNums.get(i)), copies.size() - i - 1);

            for (int j = 1; j <= matches; j++) {
                copies.set(i + j, (copies.get(i + j) + copies.get(i)));
            }
        });

        // return the sum of all copies
        return copies.stream()
                .reduce(0, Integer::sum);
    }

    private static int countMatches(HashSet<Integer> winningSet, ArrayList<Integer> yourList) {
        return (int) yourList.stream()
                .filter(winningSet::contains)
                .count();
    }
}