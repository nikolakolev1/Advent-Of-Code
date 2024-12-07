package Days.Day6;

import General.Day;

import java.io.File;
import java.util.Scanner;

public class Day6 implements Day {
    private boolean[][] map; // false = open, true = wall
    private Coordinates guard;
    private Coordinates guardStart;
    private boolean guardLeft = false;
    private boolean[][] visitedP1;

    private record Coordinates(int x, int y, Direction direction) {
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        Day day6 = new Day6();
        day6.loadData("data/day6/input.txt");
        System.out.println(day6.part1());
        System.out.println(day6.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {

            int x = 0;
            int y = 0;

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("");

                // initialize the map
                if (map == null) {
                    map = new boolean[line.length][line.length];
                }

                for (String tile : line) {
                    switch (tile) {
                        case "#" -> map[x][y] = true;
                        case "^" -> {
                            guardStart = new Coordinates(x, y, Direction.UP);
                            guard = new Coordinates(x, y, Direction.UP);
                        }
                    }
                    x++;
                }

                x = 0;
                y++;
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        boolean[][] visited = new boolean[map.length][map.length]; // false = not visited, true = visited

        while (!guardLeft) {
            visited[guard.x][guard.y] = true;
            move();
        }

        visitedP1 = visited;

        return String.valueOf(countVisited(visited));
    }

    @Override
    public String part2() {
        int sum = 0;

        for (int y = 0; y < visitedP1.length; y++) {
            for (int x = 0; x < visitedP1.length; x++) {
                if (visitedP1[x][y]) {
                    // reset guard
                    guard = new Coordinates(guardStart.x(), guardStart.y(), guardStart.direction());
                    guardLeft = false;

                    if (guardStart.x() == x && guardStart.y() == y) {
                        continue;
                    }

                    map[x][y] = true;

                    for (int i = 0; i < 10000; i++) { // hardcoded number of steps
                        if (!guardLeft) {
                            move();
                        } else {
                            break;
                        }
                    }

                    if (!guardLeft) sum++;
                    else guardLeft = false;

                    map[x][y] = false;
                }
            }
        }

        return String.valueOf(sum);
    }

    private void move() {
        if (isBlocked()) {
            turnRight();
        } else {
            if (!guardLeft) moveForward();
        }
    }

    private boolean isBlocked() {
        switch (guard.direction) {
            case UP -> {
                if (guard.y - 1 < 0) {
                    guardLeft = true;
                    return false;
                }
                return map[guard.x][guard.y - 1];
            }
            case DOWN -> {
                if (guard.y + 1 >= map.length) {
                    guardLeft = true;
                    return false;
                }
                return map[guard.x][guard.y + 1];
            }
            case LEFT -> {
                if (guard.x - 1 < 0) {
                    guardLeft = true;
                    return false;
                }
                return map[guard.x - 1][guard.y];
            }
            case RIGHT -> {
                if (guard.x + 1 >= map.length) {
                    guardLeft = true;
                    return false;
                }
                return map[guard.x + 1][guard.y];
            }
        }

        System.out.println("Should never reach this point");
        return false;
    }

    private void moveForward() {
        switch (guard.direction) {
            case UP -> guard = new Coordinates(guard.x, guard.y - 1, guard.direction);
            case DOWN -> guard = new Coordinates(guard.x, guard.y + 1, guard.direction);
            case LEFT -> guard = new Coordinates(guard.x - 1, guard.y, guard.direction);
            case RIGHT -> guard = new Coordinates(guard.x + 1, guard.y, guard.direction);
        }
    }

    private void turnRight() {
        switch (guard.direction) {
            case UP -> guard = new Coordinates(guard.x, guard.y, Direction.RIGHT);
            case DOWN -> guard = new Coordinates(guard.x, guard.y, Direction.LEFT);
            case LEFT -> guard = new Coordinates(guard.x, guard.y, Direction.UP);
            case RIGHT -> guard = new Coordinates(guard.x, guard.y, Direction.DOWN);
        }
    }

    private int countVisited(boolean[][] visited) {
        int count = 0;
        for (boolean[] row : visited) {
            for (boolean tile : row) {
                if (tile) {
                    count++;
                }
            }
        }
        return count;
    }

    private void printMap() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (guard.x == x && guard.y == y) {
                    System.out.print("^");
                } else if (map[x][y]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    private void printVisited(boolean[][] visited) {
        for (int y = 0; y < visited.length; y++) {
            for (int x = 0; x < visited.length; x++) {
                if (guard.x == x && guard.y == y) {
                    switch (guard.direction) {
                        case UP -> System.out.print("^");
                        case DOWN -> System.out.print("v");
                        case LEFT -> System.out.print("<");
                        case RIGHT -> System.out.print(">");
                    }
                } else if (visited[x][y]) {
                    System.out.print("X");
                } else if (map[x][y]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}