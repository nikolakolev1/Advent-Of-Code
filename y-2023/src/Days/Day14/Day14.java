package Days.Day14;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Day14 implements Day {
    // 0 - empty space; 1 - rolling rock; 2 - tree
    private List<List<Integer>> landscape = new ArrayList<>();
    private static final int NORTH = 0, WEST = 1, SOUTH = 2, EAST = 3;
    private int direction = NORTH;
    private static final int CYCLES = 1000000000;

    public static void main(String[] args) {
        Day day14 = new Day14();
        day14.loadData(Helper.filename(14));
        System.out.println(day14.part1());
        System.out.println(day14.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split("");

                List<Integer> row = new ArrayList<>();
                for (String s : split) {
                    switch (s) {
                        case "." -> row.add(0);
                        case "O" -> row.add(1);
                        case "#" -> row.add(2);
                        default -> throw new Exception("Invalid character");
                    }
                }
                landscape.add(row);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        return String.valueOf(calculate(tilt(landscape)));
    }

    @Override
    public String part2() {
        int cycles = (CYCLES - 100) % cycleDetection();

        for (int cycle = 1; cycle <= cycles; cycle++) {
            for (direction = 0; direction < 4; direction++) {
                landscape = tilt(landscape);
            }
        }

        return String.valueOf(calculate(landscape));
    }

    // Print the current global landscape
    private void printLandscape() {
        for (List<Integer> row : landscape) {
            for (Integer tile : row) {
                switch (tile) {
                    case 0 -> System.out.print(".");
                    case 1 -> System.out.print("O");
                    case 2 -> System.out.print("#");
                    default -> System.out.print("?");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    // Print the given landscape
    private void printLandscape(List<List<Integer>> landscape) {
        for (List<Integer> row : landscape) {
            for (Integer tile : row) {
                switch (tile) {
                    case 0 -> System.out.print(".");
                    case 1 -> System.out.print("O");
                    case 2 -> System.out.print("#");
                    default -> System.out.print("?");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    // Tilt the landscape in the given direction
    private List<List<Integer>> tilt(List<List<Integer>> landscape) {
        List<List<Integer>> newLandscape = emptyLandscape(landscape.getFirst().size(), landscape.size());
        populateWithTrees(newLandscape, landscape);

        switch (direction) {
            case NORTH, WEST -> tiltNorthOrWest(newLandscape);
            case SOUTH, EAST -> tiltSouthOrEast(newLandscape);
        }

        return newLandscape;
    }

    // Tilt the landscape in the given direction (north or west)
    private void tiltNorthOrWest(List<List<Integer>> newLandscape) {
        int height = landscape.size();
        int width = landscape.getFirst().size();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // if there is a rolling rock at this position -> move it
                if (landscape.get(y).get(x) == 1) {
                    moveRock(x, y, newLandscape);
                }
            }
        }
    }

    // Tilt the landscape in the given direction (south or east)
    private void tiltSouthOrEast(List<List<Integer>> newLandscape) {
        int height = landscape.size();
        int width = landscape.getFirst().size();

        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                // if there is a rolling rock at this position -> move it
                if (landscape.get(y).get(x) == 1) {
                    moveRock(x, y, newLandscape);
                }
            }
        }
    }

    // Move the rock at the given position in the given direction
    private void moveRock(int x, int y, List<List<Integer>> newLandscape) {
        switch (direction) {
            case NORTH, SOUTH -> {
                int newY = getBoundInColumn(x, y, newLandscape, direction);
                newLandscape.get(newY).set(x, 1);
            }
            case WEST, EAST -> {
                int newX = getBoundInRow(x, y, newLandscape, direction);
                newLandscape.get(y).set(newX, 1);
            }
        }
    }

    // Create an empty landscape with the given width and height
    private List<List<Integer>> emptyLandscape(int width, int height) {
        List<List<Integer>> landscape = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            List<Integer> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(0);
            }
            landscape.add(row);
        }

        return landscape;
    }

    // Populate the new landscape with the trees from the original landscape
    private void populateWithTrees(List<List<Integer>> newLandscape, List<List<Integer>> originalLandscape) {
        int height = originalLandscape.size();
        int width = originalLandscape.getFirst().size();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int current = originalLandscape.get(y).get(x);
                if (current == 2) {
                    newLandscape.get(y).set(x, 2);
                }
            }
        }
    }

    // Get the first block in the column, depending on the direction of the tilt
    private int getBoundInColumn(int x, int y, List<List<Integer>> landscape, int direction) {
        int bound = direction == NORTH ? 0 : landscape.size() - 1;

        switch (direction) {
            case NORTH -> {
                for (int i = y - 1; i >= 0; i--) {
                    if (landscape.get(i).get(x) != 0) {
                        return i + 1;
                    }
                }

            }
            case SOUTH -> {
                for (int i = y + 1; i < landscape.size(); i++) {
                    if (landscape.get(i).get(x) != 0) {
                        return i - 1;
                    }
                }
            }
            default -> throw new IllegalArgumentException("Invalid direction, maybe use getBoundInRow instead?");
        }

        return bound;
    }

    // Get the first block in the row, depending on the direction of the tilt
    private int getBoundInRow(int x, int y, List<List<Integer>> landscape, int direction) {
        int bound = direction == WEST ? 0 : landscape.getFirst().size() - 1;

        switch (direction) {
            case WEST -> {
                for (int i = x - 1; i >= 0; i--) {
                    if (landscape.get(y).get(i) != 0) {
                        return i + 1;
                    }
                }
            }
            case EAST -> {
                for (int i = x + 1; i < landscape.get(y).size(); i++) {
                    if (landscape.get(y).get(i) != 0) {
                        return i - 1;
                    }
                }
            }
            default -> throw new IllegalArgumentException("Invalid direction, maybe use getBoundInColumn instead?");
        }

        return bound;
    }

    /*
     * The amount of load caused by a single rounded rock (O) is equal to the number of rows
     * from the rock to the south edge of the platform, including the row the rock is on.
     */
    private int calculate(List<List<Integer>> landscape) {
        int height = landscape.size();

        int load = 0;

        for (int y = 0; y < height; y++) {
            int rocks = 0;
            for (Integer tile : landscape.get(y)) {
                rocks += tile == 1 ? 1 : 0;
            }
            load += rocks * (height - y);
        }

        return load;
    }

    // detect cycle (in this case it is 22)
    private int cycleDetection() {
        for (int cycle = 1; cycle <= 100; cycle++) {
            for (direction = 0; direction < 4; direction++) {
                landscape = tilt(landscape);
            }
        }

        // compare = a copy of landscape at cycle 100
        List<List<Integer>> compare = new ArrayList<>();
        for (List<Integer> row : landscape) {
            compare.add(new ArrayList<>(row));
        }

        // for each cycle, compare landscape to the 100th cycle
        for (int cycle = 101; cycle <= 1000; cycle++) {
            for (direction = 0; direction < 4; direction++) {
                landscape = tilt(landscape);
            }

            if (compareLandscape(compare, landscape)) {
                return cycle;
            }
        }

        throw new IllegalArgumentException("No cycle detected");
    }

    // compare two landscapes (return true if equal)
    private boolean compareLandscape(List<List<Integer>> l1, List<List<Integer>> l2) {
        int height = l1.size();
        int width = l1.getFirst().size();

        // compare each tile
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!Objects.equals(l1.get(y).get(x), l2.get(y).get(x))) {
                    return false;
                }
            }
        }

        return true;
    }
}