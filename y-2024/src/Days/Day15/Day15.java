package Days.Day15;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day15 implements Day {
    private static final int EMPTY = 0, BOX = 1, WALL = 2;
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private int robotX, robotY, robotDirection;
    private int[][] map;
    private List<Integer> instructions = new ArrayList<>();

    public static void main(String[] args) {
        Day day15 = new Day15();
        day15.loadData("data/day15/input.txt");
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
                                robotX = i;
                                robotY = lineCount;
                                robotDirection = 0;
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
        printMap();
        System.out.println();
        printInstructions();

        for (int instruction : instructions) {
//            System.out.println();
//            printInstruction(instruction);
//            System.out.println();

            moveRobot(robotX, robotY, instruction);

//            printMap();
        }

        printMap();

        return String.valueOf(score());
    }

    @Override
    public String part2() {
        int sum = 0;

        // TODO: Implement part 2

        return String.valueOf(sum);
    }

    private void moveRobot(int x, int y, int direction) {
        int nextX = getNextX(x, direction);
        int nextY = getNextY(y, direction);

        if (movable(nextX, nextY, direction)) {
            // move robot to a box (update the coordinates and the box's coordinates)
            if (map[nextY][nextX] == BOX) {
                // move the box
                int boxX = getNextX(nextX, direction);
                int boxY = getNextY(nextY, direction);
                moveBox(nextX, nextY, direction);
            }

            // move the robot
            robotX = nextX;
            robotY = nextY;
        }
    }

    // Only called when already calculated that the box is movable
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

    private boolean movable(int x, int y, int direction) {
        // x must be in bounds
        if (x < 0 || x >= map.length) return false;

        // y must be in bounds
        if (y < 0 || y >= map.length) return false;

        // tile must not be a wall
        if (map[y][x] == WALL) return false;

        // might be movable, might not be
        if (map[y][x] == BOX) return movable(getNextX(x, direction), getNextY(y, direction), direction);

        // there is nothing in the way => movable
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

    private int boxScore(int boxX, int boxY) {
        return (100 * boxY) + boxX;
    }

    private void printMap() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (y == robotY && x == robotX) {
                    System.out.print("@");
                    continue;
                }

                switch (map[y][x]) {
                    case EMPTY -> System.out.print(".");
                    case WALL -> System.out.print("#");
                    case BOX -> System.out.print("O");
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