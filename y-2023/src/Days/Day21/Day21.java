package Days.Day21;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day21 implements Day {
    // map has garden plots (.), rocks (#), and visited tiles.
    private final List<List<Tile>> map = new ArrayList<>();
    private static final int steps = 64;
    private Queue<Coordinate> toVisit = new LinkedList<>();

    private record Coordinate(int y, int x) {
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Coordinate)) {
                return false;
            }

            Coordinate other = (Coordinate) obj;
            return this.y == other.y && this.x == other.x;
        }

        boolean isValid(List<List<Tile>> map) {
            return y >= 0 && y < map.size() && x >= 0 && x < map.get(y).size();
        }

        boolean isGardenPlot(List<List<Tile>> map) {
            return isValid(map) && map.get(y).get(x).equals(Tile.GARDEN_PLOT);
        }
    }

    enum Tile {
        GARDEN_PLOT {
            @Override
            public String toString() {
                return ".";
            }
        },
        ROCK {
            @Override
            public String toString() {
                return "#";
            }
        },
        START {
            @Override
            public String toString() {
                return "S";
            }
        },
        VISITED {
            @Override
            public String toString() {
                return "O";
            }
        }
    }

    public static void main(String[] args) {
        Day day21 = new Day21();
        day21.loadData(Helper.filename(21));
        System.out.println(day21.part1());
        System.out.println(day21.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                List<Tile> row = new ArrayList<>();
                char[] line = scanner.nextLine().toCharArray();

                for (char c : line) {
                    switch (c) {
                        case '.' -> row.add(Tile.GARDEN_PLOT);
                        case '#' -> row.add(Tile.ROCK);
                        case 'S' -> {
                            row.add(Tile.START);
                            toVisit.add(new Coordinate(map.size(), row.size() - 1));
                        }
                        default -> throw new Exception("Unknown tile: " + c);
                    }
                }

                map.add(row);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        printMap();
        System.out.println();

        int halfSteps = steps / 2;
        for (int i = 0; i < halfSteps + 1; i++) {
            int queueSize = toVisit.size();
            for (int j = 0; j < queueSize; j++) {
                visit();
            }
        }

        printMap();
        System.out.println();

        int visited = 0;
        for (List<Tile> row : map) {
            for (Tile tile : row) {
                if (tile.equals(Tile.VISITED)) {
                    visited++;
                }
            }
        }

        return String.valueOf(visited);
    }

    @Override
    public String part2() {
        return "to be implemented";
    }

    // A single visit call = 2 steps.
    private void visit() {
        Coordinate current = toVisit.poll();
        if (current == null) return;

        // Visit current.
        map.get(current.y).set(current.x, Tile.VISITED);

        // Find the first step neighbours.
        List<Coordinate> firstStep = getNeighbours(current);

        // Add the second step neighbours to the queue.
        for (Coordinate neighbour : firstStep) {
            List<Coordinate> temp = getNeighbours(neighbour);

            for (Coordinate n : temp) {
                if (!toVisit.contains(n) && !n.equals(current)) {
                    toVisit.add(n);
                }
            }
        }
    }

    private List<Coordinate> getNeighbours(Coordinate current) {
        List<Coordinate> neighbours = new ArrayList<>();
        for (int y = current.y - 1; y <= current.y + 1; y++) {
            for (int x = current.x - 1; x <= current.x + 1; x++) {
                Coordinate neighbour = new Coordinate(y, x);
                if (neighbour.isGardenPlot(map) && (Math.abs(current.y - y) + Math.abs(current.x - x) == 1)) {
                    neighbours.add(neighbour);
                }
            }
        }
        return neighbours;
    }

    private void printMap() {
        for (List<Tile> row : map) {
            for (Tile tile : row) {
                System.out.print(tile);
            }
            System.out.println();
        }
    }
}