package Days.Day17;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day17 implements Day {
    // A 2D int array -> each number represents the cost of moving to that position
    private int[][] map;

    // A 4D int array -> each number represents the least amount of steps to get to that position (considering direction and straight moves left)
    private int[][][][] visited;

    // A global list of the states to be visited by the search algorithm
    private final List<state> states = new ArrayList<>();

    // Directions
    private final int EAST = 0, SOUTH = 1, WEST = 2, NORTH = 3;

    // Max number of straight moves in a row
    private final int MAX_STRAIGHT = 2; // 3, but we start at 0 => 2

    // Least amount of steps to reach the goal found so far
    private int leastSteps = Integer.MAX_VALUE;

    // A state is a position on the map, with a direction and a number of straight moves in a row, reached by a certain number of steps
    private record state(int y, int x, int straightMoves, int direction, int steps) {
    }

    public static void main(String[] args) {
        Day day17 = new Day17();
//        day17.loadData(Helper.filename_test(17));
//        day17.loadData(Helper.filename(17));
//        day17.loadData("data/day17/test.txt");
        day17.loadData("data/day17/test2.txt");
        System.out.println(day17.part1());
        System.out.println(day17.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            int y = 0;

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();

                // Initialize map (if not already)
                if (map == null) {
                    map = new int[row.length()][row.length()];
                }

                // Fill map (row by row)
                for (int x = 0; x < row.length(); x++) {
                    map[y][x] = row.charAt(x) - 48;
                }

                y++;
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Part 1: Find the shortest path from the top left corner to the bottom right corner
    @Override
    public String part1() {
//        initLeastSteps();
//        initVisited();
//
//        long startTime = System.nanoTime();
//
//        state start = new state(0, 0, 0, EAST, 0);
//        states.add(start);
//        search();
//        System.out.println(leastSteps);
//
//        long endTime = System.nanoTime();
//        long timeElapsed = endTime - startTime;
//        System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000 + "ms\n");

        return "to be implemented";
    }

    @Override
    public String part2() {
        return "to be implemented";
    }

    // Assign leastSteps to the sum of the first and second row and the last column
    private void initLeastSteps() {
        int firstRowSum = Arrays.stream(map[0]).sum();
        int secondRowSum = Arrays.stream(map[1]).sum();

        int[] lastCol = new int[map.length];
        for (int i = 0; i < lastCol.length; i++) {
            lastCol[i] = map[i][map.length - 1];
        }

        int lastColSum = Arrays.stream(lastCol).sum();

        leastSteps = firstRowSum + secondRowSum + lastColSum;
    }

    // Initialize visited array with max values
    private void initVisited() {
        visited = new int[4][3][map.length][map.length];

        for (int[][][] direction : visited) {
            for (int[][] straightMoves : direction) {
                for (int[] row : straightMoves) {
                    Arrays.fill(row, Integer.MAX_VALUE);
                }
            }
        }
    }

    // Search for the shortest path
    private void search() {
        int debug = 100;

        while (!states.isEmpty()) {
            if (debug-- == 0) {
//                System.out.println(states.size());
                debug = 100;
            }

            state current = states.getFirst();
            states.removeFirst();

            // If we are at the exit, update leastSteps
            if (current.y == map.length - 1 && current.x == map.length - 1) {
                if (current.steps < leastSteps) {
                    leastSteps = current.steps;

                    states.removeIf(next -> next.steps >= leastSteps);
                }

                continue;
            }

            // If we have already found a path with fewer steps, skip this state
            for (state move : possibleMoves(current)) {
                if (move.steps >= leastSteps) {
                    continue;
                } else if (visited(move)) {
                    continue;
                }

                insert(move);
            }
        }
    }

    // Return possible moves from current state
    private List<state> possibleMoves(state current) {
        List<state> moves = new ArrayList<>();

        // May turn left, right or continue straight (if not at max straight moves)
        int[] directions = possibleDirections(current);

        for (int direction : directions) {
            int newStraightMoves = (current.direction == direction) ? current.straightMoves + 1 : 0;

            switch (direction) {
                case EAST -> {
                    if (current.x + 1 < map.length) {
                        moves.add(new state(current.y, current.x + 1, newStraightMoves, EAST, current.steps + map[current.y][current.x + 1]));
                    }
                }
                case SOUTH -> {
                    if (current.y + 1 < map.length) {
                        moves.add(new state(current.y + 1, current.x, newStraightMoves, SOUTH, current.steps + map[current.y + 1][current.x]));
                    }
                }
                case WEST -> {
                    if (current.x - 1 >= 0) {
                        moves.add(new state(current.y, current.x - 1, newStraightMoves, WEST, current.steps + map[current.y][current.x - 1]));
                    }
                }
                case NORTH -> {
                    if (current.y - 1 >= 0) {
                        moves.add(new state(current.y - 1, current.x, newStraightMoves, NORTH, current.steps + map[current.y - 1][current.x]));
                    }
                }
            }
        }

        return moves;
    }

    // Return possible directions (left, right, straight)
    private int[] possibleDirections(state state) {
        // If we are at max straight moves, we can only turn left or right
        if (state.straightMoves == MAX_STRAIGHT) {
            int left = state.direction - 1;
            int right = state.direction + 1;

            if (left < 0) left = 3;
            if (right > 3) right = 0;

            return new int[]{left, right};
        }

        // Else we can turn left, right or continue straight
        switch (state.direction) {
            case EAST -> {
                return new int[]{EAST, SOUTH, NORTH};
            }
            case SOUTH -> {
                return new int[]{SOUTH, WEST, EAST};
            }
            case WEST -> {
                return new int[]{WEST, NORTH, SOUTH};
            }
            case NORTH -> {
                return new int[]{NORTH, EAST, WEST};
            }
        }

        // Should never reach this
        throw new RuntimeException("Invalid state");
    }

    // Insert state into states list, sorted by manhattan distance to the goal
    private void insert(state state) {
        for (int i = state.straightMoves; i < 3; i++) {
            int temp = visited[state.direction][i][state.y][state.x];
            visited[state.direction][i][state.y][state.x] = Math.min(temp, state.steps);

            for (int dir = 1; dir < 4; dir++) {
                int temp2 = visited[(state.direction + dir) % 4][i][state.y][state.x];
                visited[(state.direction + dir) % 4][i][state.y][state.x] = Math.min(temp2, state.steps + 1);
            }

//            visited[state.direction][i][state.y][state.x] = state.steps;
        }

//        Comparator<state> comparator = Comparator.comparingInt(s -> - s.y - s.x);
        Comparator<state> comparator = Comparator.comparingInt(s -> s.y + s.x + s.steps);
        int index = Collections.binarySearch(states, state, comparator);

        // I don't know why that happens (https://stackoverflow.com/questions/16764007/insert-into-an-already-sorted-list)
        if (index < 0) {
            index = -index - 1;
        }

        states.add(index, state);
    }

    // Check if we have already visited this state (with fewer steps, considering direction and straight moves)
    private boolean visited(state state) {
        return visited[state.direction][state.straightMoves][state.y][state.x] < state.steps;
    }
}