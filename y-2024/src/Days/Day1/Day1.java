package Days.Day1;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Day1 implements Day {
    private static ArrayList<Integer> left = new ArrayList<>();
    private static ArrayList<Integer> right = new ArrayList<>();

    public static void main(String[] args) {
        Day1 day1 = new Day1();
        day1.loadData("data/day1/input.txt");
        System.out.println(day1.part1());
        System.out.println(day1.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

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
        HashMap<Integer, Integer> leftMap = new HashMap<>();
        HashMap<Integer, Integer> rightMap = new HashMap<>();

        for (int i = 0; i < left.size(); i++) {
            // Left list
            // if already in the map, increment the value
            if (leftMap.containsKey(left.get(i))) {
                leftMap.put(left.get(i), leftMap.get(left.get(i)) + 1);
            } else {
                leftMap.put(left.get(i), 1);
            }

            // Right list
            // if already in the map, increment the value
            if (rightMap.containsKey(right.get(i))) {
                rightMap.put(right.get(i), rightMap.get(right.get(i)) + 1);
            } else {
                rightMap.put(right.get(i), 1);
            }
        }

        // Calculate the sum
        int sum = 0;
        HashSet<Integer> leftSet = new HashSet<>(leftMap.keySet());
        for (int key : leftSet) {
            if (rightMap.get(key) != null) {
                sum += key * rightMap.get(key);
            }
        }

        return String.valueOf(sum);
    }
}