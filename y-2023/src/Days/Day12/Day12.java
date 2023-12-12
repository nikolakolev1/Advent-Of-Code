package Days.Day12;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day12 implements Day {
    private final List<List<Boolean>> springsMap = new ArrayList<>(); // true = working, false = broken, null = unknown
    private final List<List<Integer>> damagedGroups = new ArrayList<>(); // what we know is in reality

    public static void main(String[] args) {
        Day day12 = new Day12();
        day12.loadData(Helper.filename(12));
        long start = System.nanoTime();
        System.out.println(day12.part1());
        System.out.println("Duration: " + (System.nanoTime() - start) / 1000000 + "ms");
        System.out.println(day12.part2());
    }

    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(" ");

                String[] springs = split[0].trim().split("");
                String[] damagedGroups = split[1].trim().split(",");

                List<Boolean> springsAL = new ArrayList<Boolean>();
                for (String s : springs) {
                    switch (s) {
                        case "." -> springsAL.add(true);
                        case "#" -> springsAL.add(false);
                        case "?" -> springsAL.add(null);
                    }
                }

                List<Integer> damagedGroupsAL = new ArrayList<Integer>();
                for (String s : damagedGroups) {
                    damagedGroupsAL.add(Integer.parseInt(s));
                }

                this.springsMap.add(springsAL);
                this.damagedGroups.add(damagedGroupsAL);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (int i = 0; i < springsMap.size(); i++) {
            List<List<Boolean>> possibleSprings = getAllPossibleSprings(springsMap.get(i), damagedGroups.get(i)); // getAllPossibleSprings(springsMap.get(i), damagedGroups.get(i));
            sum += countMatching(possibleSprings, damagedGroups.get(i));
        }

        System.out.println();
        return String.valueOf(sum);
    }

    public String part2() {
        return "to be implemented";
    }

    private List<List<Boolean>> getAllPossibleSprings(List<Boolean> springs, List<Integer> damagedGroups) {
        List<List<Boolean>> possibleSprings = new ArrayList<>();

        if (!springs.contains(null)) {
            possibleSprings.add(springs);
        } else {
            int nullIndex = springs.indexOf(null);

            List<Boolean> copy = new ArrayList<>(springs);
            copy.set(nullIndex, true);
            if (satisfies(copy, damagedGroups)) possibleSprings.addAll(getAllPossibleSprings(copy, damagedGroups));

            copy = new ArrayList<>(springs);
            copy.set(nullIndex, false);
            if (satisfies(copy, damagedGroups)) possibleSprings.addAll(getAllPossibleSprings(copy, damagedGroups));
        }

        return possibleSprings;
    }

    private boolean satisfies(List<Boolean> springs, List<Integer> damagedGroups) {
        int countFalse = Collections.frequency(springs, false);
        int countNull = Collections.frequency(springs, null);
        int allFalse = damagedGroups.stream().mapToInt(Integer::intValue).sum();

//        int firstFalseContinuous = 0;
//        for (Boolean spring : springs) {
//            if (spring == null || spring) {
//                if (firstFalseContinuous > 0) break;
//            } else firstFalseContinuous++;
//        }

        if (countFalse > allFalse) return false;
        if (countNull + countFalse < allFalse) return false;
//        if (firstFalseContinuous > damagedGroups.getFirst()) return false;

        return true;
    }

    private int countMatching(List<List<Boolean>> possibleSprings, List<Integer> damagedGroups) {
        int count = 0;

        for (List<Boolean> possibleSpring : possibleSprings) {
            if (holdsForScheme(possibleSpring, damagedGroups)) count++;
        }

        return count;
    }

    private boolean holdsForScheme(List<Boolean> springs, List<Integer> damagedGroups) {
        if (springs.contains(null)) throw new IllegalArgumentException("Springs cannot contain null");
        List<Integer> thisDamagedGroups = new ArrayList<>();

        int damaged = 0;
        for (Boolean spring : springs) {
            // spring is working
            if (spring) {
                if (damaged > 0) {
                    thisDamagedGroups.add(damaged);
                    damaged = 0;
                }
            }

            // spring is broken
            else damaged++;
        }
        if (damaged > 0) thisDamagedGroups.add(damaged);

        return compare(thisDamagedGroups, damagedGroups);
    }

    private boolean compare(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) return false;

        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }

        return true;
    }
}