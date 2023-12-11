package Days.Day10;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class Day10 implements Day {
    private final ArrayList<ArrayList<Tube>> tubes = new ArrayList<>();
    private final ArrayList<Tube> loop = new ArrayList<>();
    private int[] loopStart; // y, x
    private Boolean[][] map;
    private final int SOUTH = 0, WEST = 1, NORTH = 2, EAST = 3;
    private int LEFT = SOUTH;

    public record Tube(char value, int x, int y) {
    }

    public static void main(String[] args) {
        Day day10 = new Day10();
        day10.loadData("data/day10/input.txt"); // day10.loadData("data/day10/test3.txt");
        System.out.println(day10.part1());
        System.out.println(day10.part2());
    }

    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            int y = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split("");

                ArrayList<Tube> row = new ArrayList<>();

                for (int x = 0; x < split.length; x++) {
                    if (split[x].equals("S")) {
                        loopStart = new int[]{y, x};
                        split[x] = "F"; // hardcoded
                    }

                    row.add(new Tube(split[x].charAt(0), x, y));
                }

                tubes.add(row);
                y++;
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Find the loop and divide its length by 2
    public String part1() {
        return String.valueOf(findLoop().size() / 2);
    }

    /*
     * Create a Boolean[][] map
     * Identify the loop on the map
     * Go clockwise and flood left (mark the outside of the loop)
     * Count what's left (nulls)
     */
    public String part2() {
        clearTubes(); // Clear the tubes to make a map with only the loop
        map = new Boolean[tubes.size()][tubes.getFirst().size()]; // (false -> outside the loop, true -> the loop border, null -> inside the loop)

        for (Tube tube : loop) {
            int y = tube.y, x = tube.x;

            tubes.get(y).set(x, tube); // Mark the loop on the tube map
            map[y][x] = true;          // Mark the loop on the Boolean map
        }

        goClockwiseAndFloodLeft();
        // The map looks cool when printed, find a way to implement that in the future
        // printMap(map);

        return String.valueOf(countNulls());
    }

    // Identify the loop, knowing the starting point
    public ArrayList<Tube> findLoop() {
        Tube start = get(loopStart[0], loopStart[1]);

        loop.add(start);

        Tube previous = start;
        Tube current = getConnectedTubes(start)[1];

        while (current != start) {
            loop.add(current);

            // Get the next tube (make sure it's not the previous one)
            Tube[] adjacent = getConnectedTubes(current);
            if (adjacent[0] != previous) {
                previous = current;
                current = adjacent[0];
            } else {
                previous = current;
                current = adjacent[1];
            }
        }

        return loop;
    }

    // Get the tubes connected to the given tube
    public Tube[] getConnectedTubes(Tube tube) {
        Tube[] adjacent = new Tube[2];

        int x = tube.x;
        int y = tube.y;
        char value = tube.value;

        switch (value) {
            case '|' -> {
                adjacent[0] = get(y - 1, x);
                adjacent[1] = get(y + 1, x);
            }
            case '-' -> {
                adjacent[0] = get(y, x - 1);
                adjacent[1] = get(y, x + 1);
            }
            case 'L' -> {
                adjacent[0] = get(y - 1, x);
                adjacent[1] = get(y, x + 1);
            }
            case 'J' -> {
                adjacent[0] = get(y - 1, x);
                adjacent[1] = get(y, x - 1);
            }
            case '7' -> {
                adjacent[0] = get(y, x - 1);
                adjacent[1] = get(y + 1, x);
            }
            case 'F' -> {
                adjacent[0] = get(y, x + 1);
                adjacent[1] = get(y + 1, x);
            }
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }

        return adjacent;
    }


    // Begging of part 2
    public void clearTubes() {
        for (int y = 0; y < tubes.size(); y++) {
            for (int x = 0; x < tubes.getFirst().size(); x++) {
                tubes.get(y).set(x, new Tube('.', x, y));
            }
        }
    }

    public void printMap() {
        for (ArrayList<Tube> row : tubes) {
            for (Tube tube : row) {
                System.out.print(tube.value);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printMap(Boolean[][] map) {
        for (int y = 0; y < tubes.size(); y++) {
            for (int x = 0; x < tubes.getFirst().size(); x++) {
                if (map[y][x] == null) {
                    System.out.print("I");
                } else if (map[y][x]) {
                    System.out.print(get(y, x).value);
                } else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Get the tiles surrounding the given tile
    public ArrayList<Tube> getSurroundingSpace(Tube tube) {
        ArrayList<Tube> surrounding = new ArrayList<>();

        int x = tube.x;
        int y = tube.y;

        int width = tubes.getFirst().size();

        // top row
        if (y > 0) {
            if (x > 0) surrounding.add(get(y - 1, x - 1));         // top left
            surrounding.add(get(y - 1, x));                           // top center
            if (x < width - 1) surrounding.add(get(y - 1, x + 1)); // top right
        }

        // middle row: left and right
        if (x > 0) surrounding.add(get(y, x - 1));
        if (x < width - 1) surrounding.add(get(y, x + 1));

        // bottom row
        if (y < tubes.size() - 1) {
            if (x > 0) surrounding.add(get(y + 1, x - 1));          // bottom left
            surrounding.add(get(y + 1, x));                            // bottom center
            if (x < width - 1) surrounding.add(get(y + 1, x + 1));  // bottom right
        }

        return surrounding;
    }

    // Get the tile at the given coordinates
    public Tube get(int y, int x) {
        return tubes.get(y).get(x);
    }

    // Count the nulls in the map (for part 2 - after flooding)
    public int countNulls() {
        int count = 0;

        for (int i = 0; i < tubes.size(); i++) {
            for (int j = 0; j < tubes.getFirst().size(); j++) {
                if (map[i][j] == null && tubes.get(i).get(j).value == '.') {
                    count++;
                }
            }
        }

        return count;
    }

    /*
     * Main method for part 2
     * Find a starting point
     * Start going clockwise
     * If you find a space to the left, flood it
     */
    public void goClockwiseAndFloodLeft() {
        Tube start = findTubeGoingClockwise();

        // Start going clockwise
        Tube previous = get(start.y, start.x - 1);
        Tube current = start;

        do {
            // Get the tile to the left
            Tube inner = getAdjacent(current, LEFT);

            // If it's a space, and it's not flooded, flood it
            if (inner != null && inner.value == '.' && map[inner.y][inner.x] == null) {
                flood(inner.y, inner.x);
            }

            char val = current.value;
            // If you find a corner, change direction
            if (val == 'L' || val == 'J' || val == '7' || val == 'F') {
                changeDirection(current, previous.x);

                // Flood both sides of the corner
                Tube inner2 = getAdjacent(current, LEFT);

                if (inner2 != null && inner2.value == '.' && map[inner2.y][inner2.x] == null) {
                    flood(inner2.y, inner2.x);
                }
            }

            // Continue going clockwise
            Tube[] adjacent = getConnectedTubes(current);
            if (adjacent[0] != previous) {
                previous = current;
                current = adjacent[0];
            } else {
                previous = current;
                current = adjacent[1];
            }
        } while (current != start);
    }

    // Find a starting point for part 2
    public Tube findTubeGoingClockwise() {
        for (int y = 0; y < tubes.size(); y++) {
            for (int x = 0; x < tubes.getFirst().size(); x++) {
                char val = get(y, x).value;
                if (val == '-') {
                    return get(y, x);
                }
            }
        }

        throw new IllegalStateException("No tube going right found");
    }

    // Flood the space (and all the connected spaces) at the given coordinates
    public void flood(int y, int x) {
        Queue<Tube> queue = new java.util.LinkedList<>();
        queue.add(get(y, x));

        while (!queue.isEmpty()) {
            Tube current = queue.poll();

            map[current.y][current.x] = false;

            ArrayList<Tube> surrounding = getSurroundingSpace(current);

            for (Tube tube : surrounding) {
                if (map[tube.y][tube.x] == null && !queue.contains(tube)) {
                    queue.add(tube);
                }
            }
        }
    }

    public Tube getAdjacent(Tube tube, int direction) {
        int x = tube.x, y = tube.y;

        switch (direction) {
            case SOUTH -> {
                if (y < tubes.size() - 1) return get(y + 1, x);
            }
            case WEST -> {
                if (x > 0) return get(y, x - 1);
            }
            case NORTH -> {
                if (y > 0) return get(y - 1, x);
            }
            case EAST -> {
                if (x < tubes.getFirst().size() - 1) return get(y, x + 1);
            }
            default -> throw new IllegalStateException("Unexpected value: " + LEFT);
        }

        return null;
    }

    public void changeDirection(Tube tube, int prevX) {
        switch (tube.value) {
            case ('F'), ('J') -> {
                if (prevX == tube.x) LEFT++;
                else LEFT--;
            }
            case ('7'), ('L') -> {
                if (prevX == tube.x) LEFT--;
                else LEFT++;
            }
        }

        if (LEFT == 4) LEFT = 0;
        else if (LEFT == -1) LEFT = 3;
    }
}