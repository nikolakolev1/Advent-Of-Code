package Days.Day17;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day17 implements Day {
    // A 2D int array -> each number represents the cost of moving to that position
    private int[][] costMap;

    // A 4D int array -> each number represents the least amount of steps to get to that position (considering direction and straight moves left)
    private int[][][][] visited;

    // A global list of the states to be visited by the search algorithm
    private final Stack<State> states = new Stack<>();

    // Directions
    private final int EAST = 0, SOUTH = 1, WEST = 2, NORTH = 3;

    // Max number of straight moves in a row
    private final int MAX_STRAIGHT = 3;

    // Least amount of steps to reach the goal found so far
    private int leastSteps;

    // A State is a position on the map, with a direction and a number of straight moves in a row, reached by a certain number of steps
    private record State(Coordinates coordinates, int direction, int straightMoves, int steps) {
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof State)) {
                return false;
            }

            State state = (State) obj;

            return coordinates.equals(state.coordinates) && direction == state.direction && straightMoves == state.straightMoves && steps == state.steps;
        }
    }

    private record Coordinates(int x, int y) {
    }

    public static void main(String[] args) {
        Day day17 = new Day17();
        day17.loadData(Helper.filename_test(17));
//        day17.loadData(Helper.filename(17));
//        day17.loadData("data/day17/test.txt");
//        day17.loadData("data/day17/test2.txt");
        System.out.println(day17.part1());
//        System.out.println(day17.part2());
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
                if (costMap == null) {
                    costMap = new int[row.length()][row.length()];
                }

                // Fill map (row by row)
                for (int x = 0; x < row.length(); x++) {
                    costMap[y][x] = row.charAt(x) - 48;
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
        initLeastSteps();
        initVisited();

//        long startTime = System.nanoTime();
//
        State start1 = new State(new Coordinates(0, 0), EAST, 0, 0);
        State start2 = new State(new Coordinates(0, 0), SOUTH, 0, 0);
        states.push(start1);
        states.push(start2);

//        search();
//        System.out.println(leastSteps);

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
        int x = 0;
        int y = 0;

        leastSteps += costMap[y][x];

        while (x < costMap.length - 1) {
            for (int forward = 0; forward < 3; forward++) {
                leastSteps += costMap[y][++x];

                if (x == costMap.length - 1) {
                    break;
                }
            }

            leastSteps += y == 0 ? costMap[++y][x] : costMap[--y][x];
        }

        while (y < costMap.length - 1) {
            for (int forward = 0; forward <= 3; forward++) {
                leastSteps += costMap[y++][x];

                if (y == costMap.length - 1) {
                    break;
                }
            }

            leastSteps += x == costMap.length - 1 ? costMap[y][x--] : costMap[y][x++];
        }
    }

    // Initialize visited array with max values
    private void initVisited() {
        visited = new int[costMap.length][costMap.length][4][4];

        for (int[][][] x : visited) {
            for (int[][] y : x) {
                for (int[] row : y) {
                    Arrays.fill(row, leastSteps);
                }
            }
        }
    }

    // Search for the shortest path
    private void search() {
        int iterations = 0;

        while (!states.isEmpty()) {
            iterations++;
            if (iterations % 10000 == 0) {
                System.out.println("Iterations: " + iterations);
                System.out.println("Least steps " + leastSteps);
            }
            if (iterations % 10000000 == 0) {
                System.out.println("Least steps " + leastSteps);
            }

            State currentState = states.pop();

            if (currentState == null) {
                continue;
            }

            // If we have already found a path with fewer steps, skip this State
            if (currentState.steps >= leastSteps) {
                continue;
            }

            // If we are at the exit (with fewer steps), update leastSteps
            if (currentState.coordinates.y == costMap.length - 1 && currentState.coordinates.x == costMap.length - 1) {
                leastSteps = currentState.steps;
                continue;
            }

            // If we have already visited this State with fewer steps, skip it
            if (getStepsFromVisited(currentState) < currentState.steps) {
                continue;
            } else {
                addStepsToVisited(currentState);
            }

            // Add possible moves to the stack
            List<State> moves = possibleMoves(currentState);
            for (State move : moves) {
                states.push(move);
            }

            List<State> nextStates = nextStatesSameDirection(currentState);
            for (State nextState : nextStates) {
                if (states.contains(nextState)) {
//                    System.out.println("Already contains");
                } else {
                    states.push(nextState);
                }
            }
        }

        System.out.println("Iterations: " + iterations);
    }

    // Return a sorted list of possible moves from current State
    private List<State> possibleMoves(State currentState) {

        List<State> moves = new LinkedList<>();

        if (currentState.straightMoves == 0) return moves;

        // May turn left or right
        int[] directions = directionsAfterTurn(currentState.direction);

        for (int direction : directions) {
            if (direction == EAST && currentState.coordinates.x == costMap.length - 1 ||
                    direction == SOUTH && currentState.coordinates.y == costMap.length - 1 ||
                    direction == WEST && currentState.coordinates.x == 0 ||
                    direction == NORTH && currentState.coordinates.y == 0) {
                continue;
            }

            State newState = new State(new Coordinates(currentState.coordinates.x, currentState.coordinates.y), direction, 0, currentState.steps);
            moves.add(newState);
        }

        // Sort the moves by the cost of the move
//        moves.sort(Comparator.comparingInt(state -> (state.coordinates.x + state.coordinates.y)));
        moves.sort(Comparator.comparingInt(state -> state.coordinates.x * state.coordinates.y));
        Collections.reverse(moves);
        return moves;
    }

    // Return possible directions (left, right, straight)
    private int[] directionsAfterTurn(int direction) {
        // Else we can turn left, right or continue straight
        switch (direction) {
            case WEST, EAST -> {
                return new int[]{SOUTH, NORTH};
            }
            case SOUTH, NORTH -> {
                return new int[]{WEST, EAST};
            }
            default -> throw new RuntimeException("Invalid State");
        }
    }

    private State nextStateSameDirection(State currentState) {
        int x = currentState.coordinates.x;
        int y = currentState.coordinates.y;

        switch (currentState.direction) {
            case EAST -> x++;
            case SOUTH -> y++;
            case WEST -> x--;
            case NORTH -> y--;
        }

        if (x < 0 || x >= costMap.length || y < 0 || y >= costMap.length) {
            return null;
        } else {
            return new State(new Coordinates(x, y), currentState.direction, currentState.straightMoves + 1, currentState.steps + costMap[y][x]);
        }

    }

    private List<State> nextStatesSameDirection(State current) {
        List<State> nextStates = new LinkedList<>();
        State state = new State(new Coordinates(current.coordinates.x, current.coordinates.y), current.direction, current.straightMoves, current.steps);

        for (int i = current.straightMoves + 1; i <= MAX_STRAIGHT; i++) {
            if (state != null) {
                state = nextStateSameDirection(state);
                nextStates.add(state);
            }
        }

        return nextStates;
    }

    private int getStepsFromVisited(State state) {
        return visited[state.coordinates.x][state.coordinates.y][state.direction][state.straightMoves];
    }

    private void addStepsToVisited(State state) {
        if (getStepsFromVisited(state) > state.steps) {
            for (int i = state.straightMoves; i <= MAX_STRAIGHT; i++) {
                visited[state.coordinates.x][state.coordinates.y][state.direction][i] = state.steps;
            }

//            for (int i = 0; i < 4; i++) {
//                if (i != state.direction) {
//                    visited[state.coordinates.x][state.coordinates.y][i][0] = state.steps;
//                }
//            }
//            if (state.straightMoves < MAX_STRAIGHT) {
//                State nextState = nextStateSameDirection(state);
//                if (nextState != null) addStepsToVisited_ifLess(nextState);
//            }
        }
    }

//     Get the values of the surrounding cells
    private ArrayList<Integer> getSurroundingCellsValues(Coordinates coordinates) {
        int surroundingCell1_value = ((coordinates.x - 1 >= 0) && (coordinates.y - 1 >= 0)) ? costMap[coordinates.y - 1][coordinates.x - 1] : -1; // top left
        int surroundingCell2_value = (coordinates.y - 1 >= 0) ? costMap[coordinates.y - 1][coordinates.x] : -1; // top
        int surroundingCell3_value = ((coordinates.x + 1 < costMap.length) && (coordinates.y - 1 >= 0)) ? costMap[coordinates.y - 1][coordinates.x + 1] : -1; // top right
        int surroundingCell4_value = (coordinates.x - 1 >= 0) ? costMap[coordinates.y][coordinates.x - 1] : -1; // left
        int surroundingCell5_value = (coordinates.x + 1 < costMap.length) ? costMap[coordinates.y][coordinates.x + 1] : -1; // right
        int surroundingCell6_value = ((coordinates.x - 1 >= 0) && (coordinates.y + 1 < costMap.length)) ? costMap[coordinates.y + 1][coordinates.x - 1] : -1; // bottom left
        int surroundingCell7_value = (coordinates.y + 1 < costMap.length) ? costMap[coordinates.y + 1][coordinates.x] : -1; // bottom
        int surroundingCell8_value = ((coordinates.x + 1 < costMap.length) && (coordinates.y + 1 < costMap.length)) ? costMap[coordinates.y + 1][coordinates.x + 1] : -1; // bottom right

        return new ArrayList<>(Arrays.asList(surroundingCell1_value, surroundingCell2_value, surroundingCell3_value, surroundingCell4_value, surroundingCell5_value, surroundingCell6_value, surroundingCell7_value, surroundingCell8_value));
    }
}