package Days.Day16;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day16 implements Day {
    private final List<List<Integer>> contraption = new ArrayList<>(); // a square contraption of mirrors and splitters
    private List<List<Boolean>> energized; // energized = visited by the beam
    private List<List<List<Integer>>> visited; // keeps track of visited tiles and the direction of the beam
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private static final int LEFT_SIDE = 0, TOP_SIDE = 1, RIGHT_SIDE = 2, BOTTOM_SIDE = 3;

    public static void main(String[] args) {
        Day day16 = new Day16();
        day16.loadData(Helper.filename(16));
        System.out.println(day16.part1());
        System.out.println(day16.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                char[] chars = scanner.nextLine().toCharArray();
                List<Integer> row = convert(chars);

                contraption.add(row);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // converts a row of characters to a row of integers
    private static List<Integer> convert(char[] chars) throws Exception {
        List<Integer> row = new ArrayList<>();

        for (char c : chars) {
            switch (c) {
                case '.' -> row.add(0);  // 0 = empty
                case '/' -> row.add(1);  // 1 = mirror /
                case '\\' -> row.add(2); // 2 = mirror \
                case '|' -> row.add(3);  // 3 = splitter |
                case '-' -> row.add(4);  // 4 = splitter -
                default -> throw new Exception("Invalid character: " + c);
            }
        }
        return row;
    }

    @Override
    public String part1() {
        initEnergized();
        initVisited();

        tick(0, 0, RIGHT);

        return String.valueOf(countEnergized());
    }

    @Override
    public String part2() {
        int max = 0;

        // for each side of the contraption
        for (int side = LEFT_SIDE; side <= BOTTOM_SIDE; side++) {
            // for each tile on the side
            for (int tileNo = 0; tileNo < contraption.size(); tileNo++) {
                // reset the energized and visited arrays
                initEnergized();
                initVisited();

                int x = 0, y = 0, direction = 0;

                switch (side) {
                    case LEFT_SIDE -> {
                        x = 0;
                        y = tileNo;
                        direction = RIGHT;
                    }
                    case TOP_SIDE -> {
                        x = tileNo;
                        y = 0;
                        direction = DOWN;
                    }
                    case RIGHT_SIDE -> {
                        x = contraption.size() - 1;
                        y = tileNo;
                        direction = LEFT;
                    }
                    case BOTTOM_SIDE -> {
                        x = tileNo;
                        y = contraption.size() - 1;
                        direction = UP;
                    }
                }

                tick(x, y, direction);

                if (countEnergized() == 100) {
                    System.out.println("Side: " + side + ", tile: " + tileNo);
                }

                max = Math.max(max, countEnergized());
            }
        }

        return String.valueOf(max);
    }

    // Initializes the energized ArrayList (or resets it)
    private void initEnergized() {
        energized = new ArrayList<>();

        for (List<Integer> y : contraption) {
            List<Boolean> row = new ArrayList<>();
            for (int x = 0; x < y.size(); x++) {
                row.add(false);
            }
            energized.add(row);
        }
    }

    // Initializes the visited ArrayList (or resets it)
    private void initVisited() {
        visited = new ArrayList<>();

        for (List<Integer> y : contraption) {
            List<List<Integer>> row = new ArrayList<>();
            for (int x = 0; x < y.size(); x++) {
                List<Integer> tile = new ArrayList<>();
                row.add(tile);
            }
            visited.add(row);
        }
    }

    // A tick is a single step of the beam (this is a recursive method)
    private void tick(int x, int y, int direction) {
        // check if we are out of bounds (termination condition)
        if (x < 0 || x >= contraption.getFirst().size() || y < 0 || y >= contraption.size()) {
            return;
        }

        // check if we have already visited this tile in this direction (termination condition / memoization / loop prevention)
        if (visited.get(y).get(x).contains(direction)) {
            return;
        }

        // mark the tile as energized and visited
        energized.get(y).set(x, true);
        visited.get(y).get(x).add(direction);

        // check if we are at a mirror, splitter or empty tile
        switch (contraption.get(y).get(x)) {
            case 0 -> {
                // empty tile, continue in the same direction
                switch (direction) {
                    case UP -> tick(x, y - 1, direction);
                    case RIGHT -> tick(x + 1, y, direction);
                    case DOWN -> tick(x, y + 1, direction);
                    case LEFT -> tick(x - 1, y, direction);
                }
            }
            case 1 -> {
                // mirror /, change direction
                switch (direction) {
                    case UP -> tick(x + 1, y, RIGHT);
                    case RIGHT -> tick(x, y - 1, UP);
                    case DOWN -> tick(x - 1, y, LEFT);
                    case LEFT -> tick(x, y + 1, DOWN);
                }
            }
            case 2 -> {
                // mirror \, change direction
                switch (direction) {
                    case UP -> tick(x - 1, y, LEFT);
                    case RIGHT -> tick(x, y + 1, DOWN);
                    case DOWN -> tick(x + 1, y, RIGHT);
                    case LEFT -> tick(x, y - 1, UP);
                }
            }
            case 3 -> {
                // splitter |, depending on orientation, either continue in the same direction or split
                switch (direction) {
                    case UP -> tick(x, y - 1, direction);
                    case DOWN -> tick(x, y + 1, direction);
                    default -> {
                        tick(x, y - 1, UP);
                        tick(x, y + 1, DOWN);
                    }
                }
            }
            case 4 -> {
                // splitter -, depending on orientation, either continue in the same direction or split
                switch (direction) {
                    case RIGHT -> tick(x + 1, y, direction);
                    case LEFT -> tick(x - 1, y, direction);
                    default -> {
                        tick(x - 1, y, LEFT);
                        tick(x + 1, y, RIGHT);
                    }
                }
            }
        }
    }

    // counts the number of energized tiles (refers to the energized ArrayList)
    private int countEnergized() {
        int sum = 0;
        for (List<Boolean> row : energized) {
            for (Boolean tile : row) {
                sum += tile ? 1 : 0;
            }
        }
        return sum;
    }

    // prints the contraption to the console
    private void printContraption() {
        for (List<Integer> row : contraption) {
            for (Integer i : row) {
                switch (i) {
                    case 0 -> System.out.print(" ");
                    case 1 -> System.out.print("/");
                    case 2 -> System.out.print("\\");
                    case 3 -> System.out.print("|");
                    case 4 -> System.out.print("-");
                }
            }
            System.out.println();
        }
    }
}