package Days.Day15;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day15 implements Day {
    private static final String filename = "data/day15/input.txt";
    private static final int EMPTY = 0, BOX = 1, WALL = 2;
    private static final int HALF_BOX_LEFT = 3, HALF_BOX_RIGHT = 4; // for part 2
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private static final boolean PART_1 = true, PART_2 = false;
    private int robotStartingX, robotStartingY;
    private int[][] map, mapP2;
    private List<Integer> instructions = new ArrayList<>();

    public static void main(String[] args) {
        Day day15 = new Day15();
        day15.loadData(filename);
        System.out.println(day15.part1());
        System.out.println(day15.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            boolean parseMap = true;

            while (scanner.hasNextLine()) {
                int lineCount = 0;

                while (parseMap) {
                    String line = scanner.nextLine();
                    if (line.isEmpty()) {
                        parseMap = false;
                        break;
                    }

                    if (map == null) {
                        map = new int[line.length()][line.length()];
                    }

                    String[] split = line.split("");
                    for (int i = 0; i < split.length; i++) {
                        switch (split[i]) {
                            case "." -> map[lineCount][i] = EMPTY;
                            case "#" -> map[lineCount][i] = WALL;
                            case "O" -> map[lineCount][i] = BOX;
                            case "@" -> {
                                robotStartingX = i;
                                robotStartingY = lineCount;
                                map[lineCount][i] = EMPTY;
                            }
                        }
                    }

                    lineCount++;
                }

                while (scanner.hasNextLine()) {
                    String[] split = scanner.nextLine().split("");
                    for (String direction : split) {
                        switch (direction) {
                            case "^" -> instructions.add(UP);
                            case ">" -> instructions.add(RIGHT);
                            case "v" -> instructions.add(DOWN);
                            case "<" -> instructions.add(LEFT);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        for (int instruction : instructions) {
            moveRobot(robotStartingX, robotStartingY, instruction, map, PART_1);
        }

        return String.valueOf(score());
    }

    @Override
    public String part2() {
        // Reset for part 2
        resetForP2();

        // Resize the map for part 2
        resizeMap();

        for (int instruction : instructions) {
            moveRobot(robotStartingX, robotStartingY, instruction, mapP2, PART_2);
        }

        return String.valueOf(scoreP2());
    }

    // Reset for part 2
    private void resetForP2() {
        map = null;
        instructions = new ArrayList<>();
        loadData(filename);
    }

    // Data must be loaded before calling this method
    // The map must be resized for part 2:
    // -> everything except the robot is twice as wide!
    private void resizeMap() {
        mapP2 = new int[map.length][map.length * 2];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case EMPTY -> {
                        mapP2[y][2 * x] = EMPTY;
                        mapP2[y][2 * x + 1] = EMPTY;
                    }
                    case WALL -> {
                        mapP2[y][2 * x] = WALL;
                        mapP2[y][2 * x + 1] = WALL;
                    }
                    case BOX -> {
                        mapP2[y][2 * x] = HALF_BOX_LEFT;
                        mapP2[y][2 * x + 1] = HALF_BOX_RIGHT;
                    }
                }
            }
        }

        // Also move the robot to the right
        robotStartingX *= 2;
    }

    /**
     * Moves the robot in the specified direction if the next position is movable.
     * If there is a box in the next position, it will be moved as well.
     *
     * @param x         The current x-coordinate of the robot.
     * @param y         The current y-coordinate of the robot.
     * @param direction The direction in which the robot should move.
     * @param map       The map of the area.
     * @param part      Indicates whether it is part 1 or part 2 of the problem.
     */
    private void moveRobot(int x, int y, int direction, int[][] map, boolean part) {
        int nextX = getNextX(x, direction);
        int nextY = getNextY(y, direction);

        if (movable(nextX, nextY, direction, map, part)) {
            // Move the box if there is one
            int tile = map[nextY][nextX];
            if (tile == BOX || tile == HALF_BOX_LEFT || tile == HALF_BOX_RIGHT) {
                if (part == PART_1) moveBox(nextX, nextY, direction);
                else moveBoxP2(nextX, nextY, direction);
            }

            // Move the robot
            robotStartingX = nextX;
            robotStartingY = nextY;
        }
    }

    /**
     * Moves a box in the specified direction.
     * This method is only called when it is already calculated that the box is movable.
     *
     * @param x         The current x-coordinate of the box.
     * @param y         The current y-coordinate of the box.
     * @param direction The direction in which the box should be moved.
     */
    private void moveBox(int x, int y, int direction) {
        int nextX = getNextX(x, direction);
        int nextY = getNextY(y, direction);

        // move box to another box
        if (map[nextY][nextX] == BOX) {
            moveBox(nextX, nextY, direction);
        }

        // move the current box
        map[y][x] = EMPTY;
        map[nextY][nextX] = BOX;
    }

    /**
     * Moves a box in part 2 of the problem.
     * This method is only called when it is already calculated that the box is movable.
     * This is the most complex and difficult part of the problem. Also, the method is messy.
     *
     * @param x         The current x-coordinate of the box.
     * @param y         The current y-coordinate of the box.
     * @param direction The direction in which the box should be moved.
     */
    private void moveBoxP2(int x, int y, int direction) {
        int adjacentX, adjacentY, adjacentTile1, adjacentTile2;

        switch (direction) {
            case LEFT, RIGHT -> {
                // Calculate the next position in the given direction
                adjacentX = getNextX(getNextX(x, direction), direction);

                // Recursively move the box if the next position is also a box
                if (mapP2[y][adjacentX] == HALF_BOX_RIGHT || mapP2[y][adjacentX] == HALF_BOX_LEFT) {
                    moveBoxP2(adjacentX, y, direction);
                }

                // Move the current box to the next position
                if (mapP2[y][x] == HALF_BOX_LEFT) {
                    mapP2[y][x] = EMPTY;
                    mapP2[y][adjacentX - 1] = HALF_BOX_LEFT;
                    mapP2[y][adjacentX] = HALF_BOX_RIGHT;
                } else if (mapP2[y][x] == HALF_BOX_RIGHT) {
                    mapP2[y][x] = EMPTY;
                    mapP2[y][adjacentX + 1] = HALF_BOX_RIGHT;
                    mapP2[y][adjacentX] = HALF_BOX_LEFT;
                }
            }
            case UP, DOWN -> {
                // Calculate the next position in the given direction
                adjacentY = getNextY(y, direction);

                if (mapP2[y][x] == HALF_BOX_LEFT) {
                    // Get the tiles adjacent to the current box
                    adjacentTile1 = mapP2[adjacentY][x];
                    adjacentTile2 = mapP2[adjacentY][x + 1];

                    // Recursively move the box if the next position is also a box
                    if (adjacentTile1 == HALF_BOX_RIGHT || adjacentTile1 == HALF_BOX_LEFT) {
                        moveBoxP2(x, adjacentY, direction);
                    }
                    if (adjacentTile2 == HALF_BOX_RIGHT || adjacentTile2 == HALF_BOX_LEFT) {
                        moveBoxP2(x + 1, adjacentY, direction);
                    }

                    // Move the current box to the next position
                    mapP2[y][x] = mapP2[y][x + 1] = EMPTY;
                    mapP2[adjacentY][x] = HALF_BOX_LEFT;
                    mapP2[adjacentY][x + 1] = HALF_BOX_RIGHT;
                } else if (mapP2[y][x] == HALF_BOX_RIGHT) {
                    // Get the tiles adjacent to the current box
                    adjacentTile1 = mapP2[adjacentY][x];
                    adjacentTile2 = mapP2[adjacentY][x - 1];

                    // Recursively move the box if the next position is also a box
                    if (adjacentTile1 == HALF_BOX_RIGHT || adjacentTile1 == HALF_BOX_LEFT) {
                        moveBoxP2(x, adjacentY, direction);
                    }
                    if (adjacentTile2 == HALF_BOX_RIGHT || adjacentTile2 == HALF_BOX_LEFT) {
                        moveBoxP2(x - 1, adjacentY, direction);
                    }

                    // Move the current box to the next position
                    mapP2[y][x] = mapP2[y][x - 1] = EMPTY;
                    mapP2[adjacentY][x] = HALF_BOX_RIGHT;
                    mapP2[adjacentY][x - 1] = HALF_BOX_LEFT;
                }
            }
        }

    }

    /**
     * Checks if the specified position is movable.
     *
     * @param x         The x-coordinate to check.
     * @param y         The y-coordinate to check.
     * @param direction The direction in which the robot should move.
     * @param map       The map of the area.
     * @param part      Indicates whether it is part 1 or part 2 of the problem.
     * @return True if the position is movable, false otherwise.
     */
    private boolean movable(int x, int y, int direction, int[][] map, boolean part) {
        // x must be in bounds
        if (x < 0 || x >= map[0].length) return false;

        // y must be in bounds
        if (y < 0 || y >= map.length) return false;

        // tile must not be a wall
        if (map[y][x] == WALL) return false;

        // Might be movable, might not be
        if (part) {
            if (map[y][x] == BOX)
                return movable(getNextX(x, direction), getNextY(y, direction), direction, map, PART_1);
        } else {
            if ((map[y][x] == HALF_BOX_LEFT || map[y][x] == HALF_BOX_RIGHT) && (direction == LEFT || direction == RIGHT)) {
                return movable(getNextX(x, direction), getNextY(y, direction), direction, map, PART_2);
            } else if (map[y][x] == HALF_BOX_LEFT) {
                return movable(getNextX(x, direction), getNextY(y, direction), direction, map, false) &&
                        movable(getNextX(x + 1, direction), getNextY(y, direction), direction, map, false);
            } else if (map[y][x] == HALF_BOX_RIGHT) {
                return movable(getNextX(x, direction), getNextY(y, direction), direction, map, PART_2) &&
                        movable(getNextX(x - 1, direction), getNextY(y, direction), direction, map, PART_2);
            }
        }

        // There is nothing in the way => movable
        return map[y][x] == EMPTY;
    }

    private int getNextX(int x, int direction) {
        switch (direction) {
            case UP, DOWN -> {
                return x;
            }
            case RIGHT -> {
                return x + 1;
            }
            case LEFT -> {
                return x - 1;
            }
        }
        return -1;
    }

    private int getNextY(int y, int direction) {
        switch (direction) {
            case RIGHT, LEFT -> {
                return y;
            }
            case UP -> {
                return y - 1;
            }
            case DOWN -> {
                return y + 1;
            }
        }
        return -1;
    }

    private long score() {
        long score = 0;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == BOX) score += boxScore(x, y);
            }
        }

        return score;
    }

    private long scoreP2() {
        long score = 0;

        for (int y = 0; y < mapP2.length; y++) {
            for (int x = 0; x < mapP2[y].length; x++) {
                if (mapP2[y][x] == HALF_BOX_LEFT) score += boxScore(x, y);
            }
        }

        return score;
    }

    private int boxScore(int boxX, int boxY) {
        return (100 * boxY) + boxX;
    }

    // ------------------ PRINTING ------------------
    // Works for both part 1 and part 2
    private void printMap(int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (y == robotStartingY && x == robotStartingX) {
                    System.out.print("@");
                    continue;
                }

                switch (map[y][x]) {
                    case EMPTY -> System.out.print(".");
                    case WALL -> System.out.print("#");
                    case BOX -> System.out.print("O");
                    case HALF_BOX_LEFT -> System.out.print("[");
                    case HALF_BOX_RIGHT -> System.out.print("]");
                }
            }
            System.out.println();
        }
    }

    private void printInstructions() {
        for (int instruction : instructions) {
            printInstruction(instruction);
        }
        System.out.println();
    }

    private void printInstruction(int instruction) {
        switch (instruction) {
            case UP -> System.out.print("^");
            case RIGHT -> System.out.print(">");
            case DOWN -> System.out.print("v");
            case LEFT -> System.out.print("<");
        }
    }
}