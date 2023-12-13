package Days.Day13;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 implements Day {
    private final List<List<List<Integer>>> maps = new ArrayList<>();

    public static void main(String[] args) {
        Day day13 = new Day13();
        day13.loadData(Helper.filename(13));
        System.out.println(day13.part1());
        System.out.println(day13.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            List<List<Integer>> map = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isEmpty()) {
                    maps.add(map);
                    map = new ArrayList<>();
                } else {
                    String[] split = line.split("");
                    List<Integer> row = new ArrayList<>();
                    for (String s : split) {
                        switch (s) {
                            case "." -> row.add(0);
                            case "#" -> row.add(1);
                            default -> throw new Exception("Unknown character: " + s);
                        }
                    }
                    map.add(row);
                }
            }
            maps.add(map);

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (List<List<Integer>> map : maps) {
            int mirrorRow = getMirrorIndex_row(map);
            int mirrorCol = getMirrorIndex_col(map);

            sum += mirrorRow != -1 ? (mirrorRow + 1) * 100 : mirrorCol + 1;
        }

        return String.valueOf(sum);
//        return "to be implemented";
    }

    /*
     * 1. Find the mirror index
     * 2. Go through each row again and check if the
     *    difference between two adjacent rows is just 1 element
     * 3. If it is, then we may have found a pattern
     * 4. Check if the pattern repeats itself
     *     - If it does, then we have found the pattern
     *     - If it doesn't, continue searching
     */
    @Override
    public String part2() {
        return "to be implemented";
    }

    // Print a 2D array
    private void printMap(List<List<Integer>> map) {
        for (List<Integer> row : map) {
            for (Integer i : row) {
                System.out.print(i == 0 ? "." : "#");
            }
            System.out.println();
        }
    }

    // Check if two lists are equal
    private boolean equals(List<Integer> arr1, List<Integer> arr2) {
        for (int i = 0; i < arr1.size(); i++) {
            if (!arr1.get(i).equals(arr2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private int getMirrorIndex_row(List<List<Integer>> map) {
        for (int i = 0; i < map.size() - 1; i++) {
            // If two adjacent rows are the same, then we may have found a mirror index
            if (equals(map.get(i), map.get(i + 1))) {
                int potentialMirrorIndex = i;

                // Check if the pattern repeats itself
                for (int j = i + 1; j <= map.size(); j++) {
                    int temp = potentialMirrorIndex - (j - potentialMirrorIndex);

                    if (j + 1 == map.size() || temp == -1) {
                        return potentialMirrorIndex;
                    }

                    if (!equals(map.get(j + 1), map.get(temp))) {
                        break;
                    }
                }
            }
        }

        // If we reach here, then we didn't find a mirror index
        return -1;
    }

    private int getMirrorIndex_col(List<List<Integer>> map) {
        for (int i = 0; i < map.getFirst().size() - 1; i++) {
            // If two adjacent columns are the same, then we may have found a mirror index
            if (equals(getCol(map, i), getCol(map, i + 1))) {
                int potentialMirrorIndex = i;

                // Check if the pattern repeats itself
                for (int j = i + 1; j <= map.getFirst().size(); j++) {
                    int temp = potentialMirrorIndex - (j - potentialMirrorIndex);

                    if (j + 1 == map.getFirst().size() || temp == -1) {
                        return potentialMirrorIndex;
                    }

                    if (!equals(getCol(map, j + 1), getCol(map, temp))) {
                        break;
                    }
                }
            }
        }

        // If we reach here, then we didn't find a mirror index
        return -1;
    }

    // Get a column from a 2D array
    private List<Integer> getCol(List<List<Integer>> map, int index) {
        List<Integer> col = new ArrayList<>();
        for (List<Integer> row : map) {
            col.add(row.get(index));
        }
        return col;
    }
}