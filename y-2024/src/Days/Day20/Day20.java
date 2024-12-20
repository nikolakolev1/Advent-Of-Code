package Days.Day20;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Day20 implements Day {
    private record Coordinates(int x, int y) {
    }

    private int[][] map; // -1 = wall, 0 = end, 1... = distance to the end
    private Coordinates start, end;
    private final int saveAtLeast = 100;

    public static void main(String[] args) {
        Day day20 = new Day20();
        day20.loadData("data/day20/input.txt");
        System.out.println(day20.part1());
        System.out.println(day20.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int y = 0;
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split("");

                if (map == null) {
                    map = new int[split.length][split.length];
                }

                for (int x = 0; x < split.length; x++) {
                    if (split[x].equals("#")) {
                        map[y][x] = -1;
                    } else {
                        map[y][x] = 0;

                        if (split[x].equals("S")) {
                            start = new Coordinates(x, y);
                        } else if (split[x].equals("E")) {
                            end = new Coordinates(x, y);
                        }
                    }
                }
                y++;
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        fillMap();
        return String.valueOf(countJumpsThatSaveZSteps(saveAtLeast, true));
    }

    @Override
    public String part2() {
        if (map == null) fillMap();
        return String.valueOf(countJumpsThatSaveZSteps(saveAtLeast, false));
    }

    private void fillMap() {
        Coordinates current = end;

        while (getNextNode(current) != null) {
            Coordinates next = getNextNode(current);

            map[next.y][next.x] = map[current.y][current.x] + 1;
            current = next;
        }
    }

    private Coordinates getNextNode(Coordinates current) {
        Coordinates coordinates1 = new Coordinates(current.x, current.y - 1);
        Coordinates coordinates2 = new Coordinates(current.x, current.y + 1);
        Coordinates coordinates3 = new Coordinates(current.x - 1, current.y);
        Coordinates coordinates4 = new Coordinates(current.x + 1, current.y);

        if (inBounds(coordinates1) && map[coordinates1.y][coordinates1.x] == 0) {
            return coordinates1;
        } else if (inBounds(coordinates2) && map[coordinates2.y][coordinates2.x] == 0) {
            return coordinates2;
        } else if (inBounds(coordinates3) && map[coordinates3.y][coordinates3.x] == 0) {
            return coordinates3;
        } else if (inBounds(coordinates4) && map[coordinates4.y][coordinates4.x] == 0) {
            return coordinates4;
        }

        return null;
    }

    private boolean inBounds(Coordinates c) {
        return c.x >= 0 && c.x < map[0].length && c.y >= 0 && c.y < map.length;
    }

    private boolean leftJumpPossible(Coordinates c) {
        return c.x - 2 >= 0 &&                     // Make sure we don't jump out of bounds
                map[c.y][c.x - 2] > -1 &&          // Make sure we don't jump into a wall
                map[c.y][c.x - 1] == -1 &&         // Check if there is a wall in between
                map[c.y][c.x - 2] < map[c.y][c.x]; // Check if the distance is shorter, otherwise we don't jump
    }

    private boolean rightJumpPossible(Coordinates c) {
        return c.x + 2 < map[0].length &&          // Make sure we don't jump out of bounds
                map[c.y][c.x + 2] > -1 &&          // Make sure we don't jump into a wall
                map[c.y][c.x + 1] == -1 &&         // Check if there is a wall in between
                map[c.y][c.x + 2] < map[c.y][c.x]; // Check if the distance is shorter, otherwise we don't jump
    }

    private boolean upJumpPossible(Coordinates c) {
        return c.y - 2 >= 0 &&                     // Make sure we don't jump out of bounds
                map[c.y - 2][c.x] > -1 &&          // Make sure we don't jump into a wall
                map[c.y - 1][c.x] == -1 &&         // Check if there is a wall in between
                map[c.y - 2][c.x] < map[c.y][c.x]; // Check if the distance is shorter, otherwise we don't jump
    }

    private boolean downJumpPossible(Coordinates c) {
        return c.y + 2 < map.length &&             // Make sure we don't jump out of bounds
                map[c.y + 2][c.x] > -1 &&          // Make sure we don't jump into a wall
                map[c.y + 1][c.x] == -1 &&         // Check if there is a wall in between
                map[c.y + 2][c.x] < map[c.y][c.x]; // Check if the distance is shorter, otherwise we don't jump
    }

    private Coordinates leftJump(Coordinates c) {
        return new Coordinates(c.x - 2, c.y);
    }

    private Coordinates rightJump(Coordinates c) {
        return new Coordinates(c.x + 2, c.y);
    }

    private Coordinates upJump(Coordinates c) {
        return new Coordinates(c.x, c.y - 2);
    }

    private Coordinates downJump(Coordinates c) {
        return new Coordinates(c.x, c.y + 2);
    }

    private int timeSaved(Coordinates jumpFrom, Coordinates jumpTo, int timeTaken) {
        return map[jumpFrom.y][jumpFrom.x] - map[jumpTo.y][jumpTo.x] - timeTaken;
    }

    private int countJumpsThatSaveZSteps(int z, boolean part1) {
        AtomicInteger jumps = new AtomicInteger();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] > 0) {
                    Coordinates current = new Coordinates(x, y);
                    if (part1) {
                        if (leftJumpPossible(current) && timeSaved(current, leftJump(current), 2) >= z)
                            jumps.getAndIncrement();
                        if (rightJumpPossible(current) && timeSaved(current, rightJump(current), 2) >= z)
                            jumps.getAndIncrement();
                        if (upJumpPossible(current) && timeSaved(current, upJump(current), 2) >= z)
                            jumps.getAndIncrement();
                        if (downJumpPossible(current) && timeSaved(current, downJump(current), 2) >= z)
                            jumps.getAndIncrement();
                    } else {
                        manhattanDistanceAway(current, z).forEach(jumpTo -> {
                            if (timeSaved(current, jumpTo, getManhattanDistance(current, jumpTo)) >= saveAtLeast)
                                jumps.getAndIncrement();
                        });
                    }
                }
            }
        }

        return jumps.get();
    }

    private List<Coordinates> manhattanDistanceAway(Coordinates current, int distance) {
        List<Coordinates> coordinates = new ArrayList<>();

        for (int y = -distance; y <= distance; y++) {
            for (int x = -distance; x <= distance; x++) {
                if (Math.abs(x) + Math.abs(y) <= distance) {
                    Coordinates c = new Coordinates(current.x + x, current.y + y);

                    if (inBounds(c) && map[c.y][c.x] > -1) {
                        coordinates.add(c);
                    }
                }
            }
        }

        return coordinates;
    }

    private int getManhattanDistance(Coordinates c1, Coordinates c2) {
        return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
    }


    private void printMap(boolean printDistances) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                int tile = map[y][x];
                if (tile == -1) {
                    System.out.print("#");
                } else if (start != null && start.x == x && start.y == y) {
                    System.out.print("S");
                } else if (end != null && end.x == x && end.y == y) {
                    System.out.print("E");
                } else {
                    if (printDistances) System.out.print(tile % 10);
                    else System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}