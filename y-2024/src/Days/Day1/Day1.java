package Days.Day1;

import General.Day;

import java.io.File;
import java.util.*;

public class Day1 implements Day {
    private static final List<Integer> left = new ArrayList<>();
    private static final List<Integer> right = new ArrayList<>();

    public static void main(String[] args) {
        Day1 day1 = new Day1();
        day1.loadData("data/day1/input.txt");
        System.out.println(day1.part1());
        System.out.println(day1.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" {3}");
                left.add(Integer.parseInt(line[0]));
                right.add(Integer.parseInt(line[1]));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        int sum = 0;
        for (int i = 0; i < left.size(); i++) {
            sum += Math.max(left.get(i), right.get(i)) - Math.min(left.get(i), right.get(i));
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        Map<Integer, Integer> leftMap = new HashMap<>();
        Map<Integer, Integer> rightMap = new HashMap<>();

        for (int i = 0; i < left.size(); i++) {
            leftMap.merge(left.get(i), 1, Integer::sum);
            rightMap.merge(right.get(i), 1, Integer::sum);
        }

        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : leftMap.entrySet()) {
            int key = entry.getKey();
            int leftCount = entry.getValue();
            int rightCount = rightMap.getOrDefault(key, 0);
            sum += key * leftCount * rightCount;
        }

        return String.valueOf(sum);
    }
}