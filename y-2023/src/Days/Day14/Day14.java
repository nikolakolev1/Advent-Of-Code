package Days.Day14;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
                String line = scanner.nextLine();
                String[] split = line.split("");

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
        List<List<Integer>> landscape = this.landscape;
        List<List<Integer>> compare = this.landscape;

        int hardcodedCycleDetection = (CYCLES - 100) % 22;


        for (int cycle = 1; cycle <= 100 + hardcodedCycleDetection; cycle++) {
            for (int i = 0; i < 4; i++) {
                landscape = tilt(landscape);
                direction = (direction + 1) % 4;

//                printLandscape();
            }

//            if (cycle == 100) compare = landscape;

//            else if (compareLandscape(compare, landscape)) {
//                System.out.println("Cycle " + cycle + " is the same as the compare landscape");
//            }

//            printLandscape();
        }

//        for (int i = -756; i < hardcodedCycleDetection; i++) {
//            compare = tilt(compare);
//        }

        return String.valueOf(calculate(landscape));
    }

    private void printLandscape() {
        for (List<Integer> row : landscape) {
            for (Integer i : row) {
                switch (i) {
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

    private void printLandscape(List<List<Integer>> landscape) {
        for (List<Integer> row : landscape) {
            for (Integer i : row) {
                switch (i) {
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

    private List<List<Integer>> tilt(List<List<Integer>> landscape) {
        int height = landscape.size();
        int width = landscape.getFirst().size();

        List<List<Integer>> newLandscape = emptyLandscape(width, height);
        populateWithTrees(newLandscape, landscape);

        switch (direction) {
            case NORTH, WEST -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int current = landscape.get(y).get(x);
                        if (current == 1) {
                            switch (direction) {
                                case NORTH -> {
                                    int newY = getBoundInColumn(x, y, newLandscape, direction);
                                    newLandscape.get(newY).set(x, 1);
                                }
                                case WEST -> {
                                    int newX = getBoundInRow(x, y, newLandscape, direction);
                                    newLandscape.get(y).set(newX, 1);
                                }
                                default -> throw new IllegalArgumentException("Invalid direction");
                            }

                        }
                    }
                }
            }
            case SOUTH, EAST -> {
                for (int y = height - 1; y >= 0; y--) {
                    for (int x = width - 1; x >= 0; x--) {
                        int current = landscape.get(y).get(x);
                        if (current == 1) {
                            switch (direction) {
                                case SOUTH -> {
                                    int newY = getBoundInColumn(x, y, newLandscape, direction);
                                    newLandscape.get(newY).set(x, 1);
                                }
                                case EAST -> {
                                    int newX = getBoundInRow(x, y, newLandscape, direction);
                                    newLandscape.get(y).set(newX, 1);
                                }
                                default -> throw new IllegalArgumentException("Invalid direction");
                            }

                        }
                    }
                }
            }
        }

//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int current = landscape.get(y).get(x);
//                if (current == 1) {
//                    switch (direction) {
//                        case NORTH, SOUTH -> {
//                            int newY = getBoundInColumn(x, y, newLandscape, direction);
//                            newLandscape.get(newY).set(x, 1);
//                        }
//                        case WEST, EAST -> {
//                            int newX = getBoundInRow(x, y, newLandscape, direction);
//                            newLandscape.get(y).set(newX, 1);
//                        }
//                    }
//
//                }
//            }
//        }

        return newLandscape;
    }

    private List<List<Integer>> emptyLandscape(int width, int height) {
        List<List<Integer>> landscape = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(0);
            }
            landscape.add(row);
        }

        return landscape;
    }

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

//    private int cycleDetection()

    private boolean compareLandscape(List<List<Integer>> landscape1, List<List<Integer>> landscape2) {
        int height = landscape1.size();
        int width = landscape1.getFirst().size();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (landscape1.get(y).get(x) != landscape2.get(y).get(x)) {
                    return false;
                }
            }
        }

        return true;
    }
}