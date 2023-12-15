package Days.Day13;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 implements Day {
    private final List<List<List<Integer>>> maps = new ArrayList<>();
    private int smudge, rowP2 = 0, colP2 = 0;

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
    }

    @Override
    public String part2() {
        int sum = 0;

        for (List<List<Integer>> map : maps) {
            int mirrorRow = getMirrorIndex_row(map);
            int mirrorCol = getMirrorIndex_col(map);

//            if (mirrorRow == -1 && mirrorCol == 9 && sum > 18000) {
//                printMap(map);
//            }

            int mirrorRowP2 = getMirrorIndex_row_P2(map);
            int mirrorColP2 = getMirrorIndex_col_P2(map);

//            // TODO: Remove this when done
//            if (mirrorRow == mirrorRowP2 && mirrorCol == mirrorColP2) {
//                System.out.println("Reached here, something is wrong!");
//            }

            /*
             * The mirror index for part 2 must be different from the mirror index for part 1.
             * If they are the same -> continue searching for a different mirror index.
             */
            if (mirrorRow == mirrorRowP2 && mirrorCol == mirrorColP2) {
                // Continue searching
                if (mirrorRowP2 != -1) {
                    rowP2 = mirrorRowP2 + 1;

                    // do stuff (reassign mirrorRowP2 or mirrorColP2)
                    mirrorRowP2 = getMirrorIndex_row_P2(map);
                    if (mirrorRowP2 == -1) {
                        mirrorColP2 = getMirrorIndex_col_P2(map);
                    }

                    rowP2 = 0;
                } else {
                    colP2 = mirrorColP2 + 1;

                    // do stuff (reassign mirrorColP2)
                    mirrorColP2 = getMirrorIndex_col_P2(map);

                    colP2 = 0;
                }
            }

//            // TODO: Remove this when done
//            if (mirrorRow == mirrorRowP2 && mirrorCol == mirrorColP2) {
//                System.out.println("Reached here AGAIN, something is wrong!");
//            }

            sum += mirrorRowP2 != -1 ? (mirrorRowP2 + 1) * 100 : mirrorColP2 + 1;
        }

        return String.valueOf(sum);
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

    // Check if two lists are equal, but allow for one difference (smudge)
    private boolean almostEquals(List<Integer> arr1, List<Integer> arr2) {
        int count = 0;
        for (int i = 0; i < arr1.size(); i++) {
            if (!arr1.get(i).equals(arr2.get(i))) {
                count++;
            }

            if (count > 1) {
                return false;
            }
        }

        if (count == 1) {
            smudge++;
        }
        return true;
    }

    private int getMirrorIndex_row(List<List<Integer>> map) {
        for (int i = 0; i < map.size() - 1; i++) {
            // If two adjacent rows are the same, then we may have found a mirror index
            if (equals(map.get(i), map.get(i + 1))) {
                // Check if the pattern repeats itself
                for (int j = i + 1; j <= map.size(); j++) {
                    int pairNo = i - (j - i);

                    // If we reach either edge of the map, then we have found a mirror index
                    if (j + 1 == map.size() || pairNo == -1) {
                        return i;
                    }

                    // Otherwise, check if the pattern repeats itself again
                    if (!equals(map.get(j + 1), map.get(pairNo))) {
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
                // Check if the pattern repeats itself
                for (int j = i + 1; j <= map.getFirst().size(); j++) {
                    int pairNo = i - (j - i);

                    // If we reach either edge of the map, then we have found a mirror index
                    if (j + 1 == map.getFirst().size() || pairNo == -1) {
                        return i;
                    }

                    // Otherwise, check if the pattern repeats itself again
                    if (!equals(getCol(map, j + 1), getCol(map, pairNo))) {
                        break;
                    }
                }
            }
        }

        // If we reach here, then we didn't find a mirror index
        return -1;
    }

    private int getMirrorIndex_row_P2(List<List<Integer>> map) {
        smudge = 0;

        for (int i = rowP2; i < map.size() - 1; i++) {
            // If two adjacent rows are the same, then we may have found a mirror index
            if (almostEquals(map.get(i), map.get(i + 1))) {
                // Check if the pattern repeats itself
                for (int j = i + 1; j <= map.size(); j++) {
                    int pairNo = i - (j - i);

                    // If we reach either edge of the map, then we have found a mirror index
                    if (j + 1 == map.size() || pairNo == -1) {
                        if (smudge == 1) return i;
                        else {
                            smudge = 0;
                            break;
                        }
                    }

                    // Otherwise, check if the pattern repeats itself again
                    if (!almostEquals(map.get(j + 1), map.get(pairNo)) || smudge > 1) {
                        smudge = 0;
                        break;
                    }
                }
            }
        }

        // If we reach here, then we didn't find a mirror index
        return -1;
    }

    private int getMirrorIndex_col_P2(List<List<Integer>> map) {
        smudge = 0;

        for (int i = colP2; i < map.getFirst().size() - 1; i++) {
            // If two adjacent columns are the same, then we may have found a mirror index
            if (almostEquals(getCol(map, i), getCol(map, i + 1))) {
                // Check if the pattern repeats itself
                for (int j = i + 1; j <= map.getFirst().size(); j++) {
                    int pairNo = i - (j - i);

                    // If we reach either edge of the map, then we have found a mirror index
                    if (j + 1 == map.getFirst().size() || pairNo == -1) {
                        if (smudge == 1) return i;
                        else break;
                    }

                    // Otherwise, check if the pattern repeats itself again
                    if (!almostEquals(getCol(map, j + 1), getCol(map, pairNo)) || smudge > 1) {
                        break;
                    }
                }
            }
        }

        // If we reach here, then we didn't find a mirror index
        return -1;
    }

    // Get the column at a specific index from a 2D array
    private List<Integer> getCol(List<List<Integer>> map, int index) {
        List<Integer> col = new ArrayList<>();
        for (List<Integer> row : map) {
            col.add(row.get(index));
        }
        return col;
    }
}