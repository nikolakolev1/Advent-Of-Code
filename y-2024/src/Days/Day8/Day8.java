package Days.Day8;

import General.Day;

import java.io.File;
import java.util.*;

public class Day8 implements Day {
    private final Map<String, List<Coordinate>> antennas = new HashMap<>();
    private int mapSize = 0;

    private record Coordinate(int x, int y) {
    }

    public static void main(String[] args) {
        Day day8 = new Day8();
        day8.loadData("data/day8/input.txt");
        System.out.println(day8.part1());
        System.out.println(day8.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int y = 0;

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("");

                for (int x = 0; x < line.length; x++) {
                    if (!line[x].equals(".")) {
                        antennas.computeIfAbsent(line[x], _ -> new ArrayList<>()).add(new Coordinate(x, y));
                    }
                }

                y++;
            }

            mapSize = y;
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        Set<Coordinate> antiNodes = new HashSet<>();

        // Loop through all antennas
        antennas.forEach((_, coordinates) -> {
            for (Coordinate antenna : coordinates) {
                for (Coordinate otherAntenna : coordinates) {
                    if (antenna.equals(otherAntenna)) continue;

                    Coordinate distance = manhattanDistance(antenna, otherAntenna);

                    if (withingBounds(antenna, distance)) {
                        antiNodes.add(new Coordinate(antenna.x + distance.x, antenna.y + distance.y));
                    }
                }
            }
        });

        return String.valueOf(antiNodes.size());
    }

    @Override
    public String part2() {
        Set<Coordinate> antiNodes = new HashSet<>();

        // Loop through all antennas
        antennas.forEach((_, coordinates) -> {
            for (Coordinate antenna : coordinates) {
                for (Coordinate otherAntenna : coordinates) {
                    if (antenna.equals(otherAntenna)) continue;

                    Coordinate distance = manhattanDistance(antenna, otherAntenna);

                    antiNodes.add(antenna);

                    int distanceX = distance.x;
                    int distanceY = distance.y;

                    while (withingBounds(antenna, distance)) {
                        antiNodes.add(new Coordinate(antenna.x + distance.x, antenna.y + distance.y));
                        distance = new Coordinate(distance.x + distanceX, distance.y + distanceY);
                    }
                }
            }
        });

        return String.valueOf(antiNodes.size());
    }

    /**
     * Calculates the Manhattan distance between two coordinates.
     * The result is a relative coordinate representing the distance.
     *
     * @return A new Coordinate representing the Manhattan distance between the two input coordinates.
     */
    private Coordinate manhattanDistance(Coordinate a, Coordinate b) {
        return new Coordinate(a.x - b.x, a.y - b.y);
    }

    /**
     * Checks if the given coordinate is within the bounds of the map.
     *
     * @param antenna  The starting coordinate.
     * @param distance The distance to check from the starting coordinate.
     * @return true if the resulting coordinate is within the bounds, false otherwise.
     */
    private boolean withingBounds(Coordinate antenna, Coordinate distance) {
        return antenna.x + distance.x >= 0 && antenna.x + distance.x < mapSize && antenna.y + distance.y >= 0 && antenna.y + distance.y < mapSize;
    }
}