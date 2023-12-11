package Days.Day11;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day11 implements Day {
    private boolean[][] cosmos; // true = galaxy, false = empty space

    private record Coordinates(int y, int x) {
    }

    private final ArrayList<Coordinates> galaxies = new ArrayList<>();
    private final ArrayList<Coordinates> galaxies_expanded = new ArrayList<>();
    private static final long RATE_OF_EXPANSION = 1000000L; // hardcoded (part 2)

    public static void main(String[] args) {
        Day day11 = new Day11();
        day11.loadData(Helper.filename(11));
        System.out.println(day11.part1());
        System.out.println(day11.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            Scanner tempSc = new Scanner(input);
            String tempLine = tempSc.nextLine();
            int length = tempLine.length();
            tempSc.close();

            cosmos = new boolean[length][length];
            int row = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                for (int col = 0; col < length; col++) {
                    if (line.charAt(col) == '#') {
                        cosmos[row][col] = true;
                        galaxies.add(new Coordinates(row, col));
                    }
                }

                row++;
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        boolean[][] expandedCosmos = expandUniverse(cosmos);
        for (int row = 0; row < expandedCosmos.length; row++) {
            for (int col = 0; col < expandedCosmos[0].length; col++) {
                if (expandedCosmos[row][col]) {
                    galaxies_expanded.add(new Coordinates(row, col));
                }
            }
        }

        int sum = 0;

        for (int galaxy1 = 0; galaxy1 <= galaxies_expanded.size() - 2; galaxy1++) {
            for (int galaxy2 = galaxy1 + 1; galaxy2 <= galaxies_expanded.size() - 1; galaxy2++) {
                sum += manhattanDistance(galaxies_expanded.get(galaxy1), galaxies_expanded.get(galaxy2));
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        ArrayList<Integer> emptyRows = emptyRows(cosmos);
        ArrayList<Integer> emptyCols = emptyCols(cosmos);

        long sum = 0;

        for (int galaxy1 = 0; galaxy1 <= galaxies.size() - 2; galaxy1++) {
            for (int galaxy2 = galaxy1 + 1; galaxy2 <= galaxies.size() - 1; galaxy2++) {
                sum += manhattanDistance(galaxies.get(galaxy1), galaxies.get(galaxy2), emptyRows, emptyCols);
            }
        }

        return String.valueOf(sum);
    }

    private boolean[][] expandUniverse(boolean[][] universe) {
        ArrayList<Integer> emptyRows = emptyRows(universe);
        ArrayList<Integer> emptyCols = emptyCols(universe);

        int y = universe.length + emptyRows.size();
        int x = universe[0].length + emptyCols.size();

        boolean[][] newUniverse = new boolean[y][x];

        for (int newRow = 0, row = 0; row < universe.length; newRow++, row++) {
            for (int newCol = 0, col = 0; col < universe[0].length; newCol++, col++) {
                if (emptyRows.contains(row)) {
                    newUniverse[newRow] = new boolean[x];
                    newUniverse[++newRow] = new boolean[x];
                    break;
                } else if (emptyCols.contains(col)) {
                    newUniverse[newRow][newCol] = false;
                    newUniverse[newRow][++newCol] = false;
                } else {
                    newUniverse[newRow][newCol] = universe[row][col];
                }
            }
        }

        return newUniverse;
    }

    private ArrayList<Integer> emptyRows(boolean[][] universe) {
        ArrayList<Integer> emptyRows = new ArrayList<>();

        for (int row = 0; row < universe.length; row++) {
            boolean[] current = universe[row];
            if (isEmpty(current)) emptyRows.add(row);
        }

        return emptyRows;
    }

    private ArrayList<Integer> emptyCols(boolean[][] universe) {
        ArrayList<Integer> emptyCols = new ArrayList<>();

        for (int col = 0; col < universe[0].length; col++) {
            boolean[] current = new boolean[universe.length];
            for (int row = 0; row < universe.length; row++) {
                current[row] = universe[row][col];
            }

            if (isEmpty(current)) emptyCols.add(col);
        }

        return emptyCols;
    }

    private boolean isEmpty(boolean[] array) {
        for (boolean bool : array) {
            if (bool) return false;
        }

        return true;
    }

    private int manhattanDistance(Coordinates c1, Coordinates c2) {
        return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
    }

    private long manhattanDistance(Coordinates c1, Coordinates c2, ArrayList<Integer> emptyRows, ArrayList<Integer> emptyCols) {
        int y1 = c1.y;
        int x1 = c1.x;
        int y2 = c2.y;
        int x2 = c2.x;

        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);

        int expandedRows = countExpanded(minY, maxY, emptyRows);
        int expandedCols = countExpanded(minX, maxX, emptyCols);

        return Math.abs(x1 - x2) + Math.abs(y1 - y2) + ((RATE_OF_EXPANSION - 1) * (expandedRows + expandedCols));
    }

    private int countExpanded(int a, int b, ArrayList<Integer> empty) {
        int count = 0;

        for (Integer integer : empty) {
            if (integer > a && integer < b) {
                count++;
            }
        }

        return count;
    }
}