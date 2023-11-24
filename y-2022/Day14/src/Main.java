import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static int columnStart, columnEnd, rowEnd;
    private static ArrayList<ArrayList<String>> cave;
    private static int sand;

    public static void main(String[] args) {
        part1();
        System.out.println();
        part2();
    }

    private static void part1() {
        String inputStr = "input.txt";
        sand = 0;

        findRowsAndColumns(loadData1(inputStr));
        createMine();
        addRocksToMine(loadData2(inputStr));
        for (int i = 0; i < 700; i++) {
            pourOneSand(500 - columnStart, 0);// + mapOffsetX, 0);
        }

        printMine();
        System.out.println();

        System.out.println("=== Part 1 ===\nSand: " + sand);
    }

    private static void part2() {
        String inputStr = "part2_hardcode.txt";
        sand = 0;

        findRowsAndColumns(loadData1(inputStr));
        createMine();
        addRocksToMine(loadData2(inputStr));
        for (int i = 0; i < 31706; i++) {
            pourOneSand(500 - columnStart, 0);// + mapOffsetX, 0);
        }

//        printMine();
//        System.out.println();

        System.out.println("=== Part 2 ===\nSand: " + sand);
    }

    private static ArrayList<String> loadData1(String inputStr) {
        ArrayList<String> allData = new ArrayList<>();

        try {
            File input = new File(inputStr);
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();
                String[] temp = thisLine.split(" -> ");
                Collections.addAll(allData, temp);
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return allData;
    }

    private static void findRowsAndColumns(ArrayList<String> allData) {
        columnStart = 1000;
        columnEnd = -1;
        rowEnd = -1;

        for (String coordinates : allData) {
            String[] columnAndRow = coordinates.split(",");
            int thisColumn = Integer.parseInt(columnAndRow[0]);
            int thisRow = Integer.parseInt(columnAndRow[1]);

            if (columnStart > thisColumn) columnStart = thisColumn;
            else if (columnEnd < thisColumn) columnEnd = thisColumn;
            if (rowEnd < thisRow) rowEnd = thisRow;
        }
    }

    private static void createMine() {
        cave = new ArrayList<>();

        int rowAmount = rowEnd;
        for (int j = 0; j < rowAmount + 1; j++) {// + (2 * mapOffsetY); j++) {
            ArrayList<String> thisRow = new ArrayList<>();

            int columnAmount = columnEnd - columnStart;
            for (int i = 0; i < columnAmount + 1; i++) {// + (2 * mapOffsetX); i++) {
                thisRow.add(".");
            }

            cave.add(thisRow);
        }
    }

    private static ArrayList<ArrayList<Integer>> loadData2(String inputStr) {
        ArrayList<ArrayList<Integer>> allPaths = new ArrayList<>();

        try {
            File input = new File(inputStr);
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();
                String[] temp = thisLine.split(" -> ");
                ArrayList<Integer> path = new ArrayList<>();

                for (String s : temp) {
                    String[] temp2 = s.split(",");
                    for (int j = 0; j < 2; j++) {
                        path.add(Integer.parseInt(temp2[j]));
                    }
                }

                allPaths.add(path);
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return allPaths;
    }

    private static void addRocksToMine(ArrayList<ArrayList<Integer>> allPaths) {
        for (ArrayList<Integer> thisPath : allPaths) {
            int thisPathSize = thisPath.size();
            for (int j = 0; j < thisPathSize - 2; j++) {
                boolean positiveDiff = true;

                if (Objects.equals(thisPath.get(j), thisPath.get(j + 2))) {
                    int x = thisPath.get(j);
                    int y1 = thisPath.get(j + 1);
                    int y2 = thisPath.get(j + 3);

                    int difference = y1 - y2;

                    if (difference < 0) {
                        difference *= -1;
                        positiveDiff = false;
                    }

                    for (int k = 0; k < difference + 1; k++) {
                        if (!positiveDiff) {
                            cave.get(y1 + k).set(x - columnStart, "#");
                        } else {
                            cave.get(y1 - k).set(x - columnStart, "#");
                        }
                    }
                } else {
                    int y = thisPath.get(j + 1);
                    int x1 = thisPath.get(j);
                    int x2 = thisPath.get(j + 2);

                    int difference = x1 - x2;

                    if (difference < 0) {
                        difference *= -1;
                        positiveDiff = false;
                    }

                    for (int k = 0; k < difference + 1; k++) {
                        if (!positiveDiff) {
                            cave.get(y).set((x1 + k) - columnStart, "#");
                        } else {
                            cave.get(y).set((x1 - k) - columnStart, "#");
                        }
                    }
                }
                j++;
            }
        }
    }

    private static void pourOneSand(int x, int y) {
        if (tileIsInMap(x, y + 1)) {
            if (tileIsEmpty(x, y + 1)) {
                pourOneSand(x, ++y);
            } else if (tileIsInMap(x - 1, y + 1)) {
                if (tileIsEmpty(x - 1, y + 1)) {
                    pourOneSand(--x, ++y);
                } else if (tileIsInMap(x - 1, y + 1)) {
                    if (tileIsEmpty(x + 1, y + 1)) {
                        pourOneSand(++x, ++y);
                    } else {
                        cave.get(y).set(x, "O");
                        sand++;
                    }
                }
            }
        }
    }

    private static boolean tileIsEmpty(int x, int y) {
        return cave.get(y).get(x).equals(".");
    }

    private static boolean tileIsInMap(int x, int y) {
        return y <= rowEnd && (x >= 0 && x <= columnEnd - columnStart);
    }

    private static void printMine() {
        int caveRows = cave.size();
        for (int i = 0; i < caveRows; i++) {
            ArrayList<String> row = cave.get(i);
            System.out.print(i);
            if (i < 10) System.out.print("   ");
            else if (i < 100) System.out.print("  ");
            else System.out.print(" ");

            for (String tile : row) {
                System.out.print(tile);
            }
            System.out.println();
        }
    }
}