package Days.Day18;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day18 implements Day {
    private boolean[][] map; // F = ground, T = trench
    private final List<instruction> instructions = new ArrayList<>(); // List of instructions (from the input file)
    int maxUp, maxLeft; // This is actually the starting position

    private record instruction(char direction, int steps, String color) {
        @Override
        public String toString() {
            return String.format("%s %d %s", direction, steps, color);
        }
    }

    private record coordinates(int y, int x) {
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof coordinates other) {
                return this.y == other.y && this.x == other.x;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Day day18 = new Day18();
        day18.loadData(Helper.filename_test(18));
        System.out.println(day18.part1());
        System.out.println(day18.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(" ");
                instructions.add(new instruction(split[0].charAt(0), Integer.parseInt(split[1]), split[2].substring(1, split[2].length() - 1)));
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        initMap();
        digTrenchBorder(new coordinates(maxUp, maxLeft), 0);
        digTrenchInside();

        int count = 0;
        for (boolean[] row : map) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2() {
        List<coordinates> coords = instructionsToCoords();
        int area = shoelace(coords);

//        return String.valueOf(area);
        return "not implemented";
    }


    private void initMap() {
        int maxUp = 0, maxDown = 0, maxLeft = 0, maxRight = 0;
        int currentUp = 0, currentDown = 0, currentLeft = 0, currentRight = 0;

        for (instruction instruction : instructions) {
            switch (instruction.direction) {
                case 'U' -> {
                    currentUp += instruction.steps;
                    currentDown -= instruction.steps;
                }
                case 'D' -> {
                    currentUp -= instruction.steps;
                    currentDown += instruction.steps;
                }
                case 'L' -> {
                    currentLeft += instruction.steps;
                    currentRight -= instruction.steps;
                }
                case 'R' -> {
                    currentLeft -= instruction.steps;
                    currentRight += instruction.steps;
                }
            }

            maxUp = Math.max(maxUp, currentUp);
            maxDown = Math.max(maxDown, currentDown);
            maxLeft = Math.max(maxLeft, currentLeft);
            maxRight = Math.max(maxRight, currentRight);
        }

        this.maxUp = maxUp;
        this.maxLeft = maxLeft;

        map = new boolean[maxDown + maxUp + 1][maxRight + maxLeft + 1];

        // Set the starting position to true
        map[maxUp][maxLeft] = true;
    }

    // Recursively digs the border of the trench (assumes that the coordinates are valid)
    private void digTrenchBorder(coordinates coordinates, int instructionIndex) {
        // Termination condition
        if (instructionIndex >= instructions.size()) return;

        instruction i = instructions.get(instructionIndex);

        for (int step = 0; step < i.steps; step++) {
            coordinates = move(coordinates, i.direction);
            map[coordinates.y][coordinates.x] = true;
        }

        digTrenchBorder(coordinates, instructionIndex + 1);
    }

    private coordinates move(coordinates coordinates, char direction) {
        coordinates newCoordinates;

        switch (direction) {
            case 'U' -> newCoordinates = new coordinates(coordinates.y - 1, coordinates.x);
            case 'D' -> newCoordinates = new coordinates(coordinates.y + 1, coordinates.x);
            case 'L' -> newCoordinates = new coordinates(coordinates.y, coordinates.x - 1);
            case 'R' -> newCoordinates = new coordinates(coordinates.y, coordinates.x + 1);
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }

        return newCoordinates;
    }

    // Find an inner cell and fill the trench
    private void digTrenchInside() {
        // Find a border cell (in the first row) that is not a corner (i.e. touches the inside of the trench)
        int x;
        for (x = 0; x < map[0].length; x++) {
            if (map[0][x] && !map[1][x]) {
                break;
            }
        }

        // Fill the trench
        fill(new coordinates(1, x));
    }

    // BFS - fill the trench
    private void fill(coordinates coordinates) {
        Queue<coordinates> queue = new LinkedList<>();
        queue.add(coordinates);

        while (!queue.isEmpty()) {
            coordinates = queue.poll();
            int y = coordinates.y, x = coordinates.x;

            // Fill the current cell
            map[y][x] = true;

            // Add the neighboring cells to the queue
            coordinates c1 = new coordinates(y - 1, x), c2 = new coordinates(y + 1, x), c3 = new coordinates(y, x - 1), c4 = new coordinates(y, x + 1);

            if (!map[y - 1][x] && !queue.contains(c1)) queue.add(c1);
            if (!map[y + 1][x] && !queue.contains(c2)) queue.add(c2);
            if (!map[y][x - 1] && !queue.contains(c3)) queue.add(c3);
            if (!map[y][x + 1] && !queue.contains(c4)) queue.add(c4);
        }
    }

    private int shoelace(List<coordinates> coordinates) {
        int sum = 0;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            sum += coordinates.get(i).x * coordinates.get(i + 1).y - coordinates.get(i + 1).x * coordinates.get(i).y;
        }
        sum += coordinates.getLast().x * coordinates.getFirst().y - coordinates.getFirst().x * coordinates.getLast().y;
        return Math.abs(sum) / 2;
    }

    private List<coordinates> instructionsToCoords() {
        List<coordinates> coords = new ArrayList<>();
        int x = maxLeft + 1, y = maxUp + 1;

        coords.add(new coordinates(y, x));

        for (instruction instruction : instructions) {
            switch (instruction.direction) {
                case 'U' -> y -= instruction.steps;
                case 'D' -> y += instruction.steps;
                case 'L' -> x -= instruction.steps;
                case 'R' -> x += instruction.steps;
            }
            coords.add(new coordinates(y, x));
        }
        return coords;
    }
}