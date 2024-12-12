package Days.Day12;

import General.Day;

import java.io.File;
import java.util.*;

public class Day12 implements Day {
    private String[][] map;
    private boolean[][] visited;
    private final List<Group> groups = new ArrayList<>();

    private record Coordinates(int y, int x) {
    }

    private record Group(Set<Coordinates> coordinates, Set<Side> sides) {
    }

    /**
     * Represents a side of a cell in the map.
     *
     * @param horizontal   true if the side is horizontal, false if vertical
     * @param smaller_xOrY - if the side is horizontal, the smaller y value of the two cells; else the smaller x value
     * @param bigger_xOrY  - if the side is horizontal, the bigger y value of the two cells; else the bigger x value
     * @param orientation  - the orientation of the side (UP, DOWN, LEFT, RIGHT)
     * @param first_xOrY   - if the side is horizontal, the x value of the first cell; else the y value
     * @param last_xOrY    - if the side is horizontal, the x value of the second cell; else the y value
     */
    private record Side(boolean horizontal,
                        int smaller_xOrY, int bigger_xOrY,
                        Orientation orientation,
                        int first_xOrY, int last_xOrY) {
    }

    private enum Orientation {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        Day day12 = new Day12();
        day12.loadData("data/day12/input.txt");
        System.out.println(day12.part1());
        System.out.println(day12.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            String[] line = scanner.nextLine().split("");
            map = new String[line.length][line.length];
            visited = new boolean[line.length][line.length];
            int i = 0;
            map[i++] = line;

            while (scanner.hasNextLine()) {
                map[i++] = scanner.nextLine().split("");
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        identifyGroups();
        for (Group group : groups) {
            sum += cost(group, true);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        if (groups.isEmpty()) identifyGroups();
        for (Group group : groups) {
            sum += cost(group, false);
        }

        return String.valueOf(sum);
    }

    private void identifyGroups() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (!visited[y][x]) {
                    Group group = new Group(new HashSet<>(), new HashSet<>());
                    findGroup(y, x, group, map[y][x]);
                    groups.add(group);
                }
            }
        }
    }

    private void findGroup(int y, int x, Group group, String identifier) {
        if (y < 0 || y >= map.length || x < 0 || x >= map.length) { // Out of bounds
            return;
        } else if (visited[y][x]) { // Already visited
            return;
        } else if (!map[y][x].equals(identifier)) { // Not the same identifier
            return;
        }

        // Mark as visited and add to group (if all conditions above are met)
        visited[y][x] = true;
        group.coordinates.add(new Coordinates(y, x));

        // Recursively check all directions
        findGroup(y + 1, x, group, identifier);
        findGroup(y - 1, x, group, identifier);
        findGroup(y, x + 1, group, identifier);
        findGroup(y, x - 1, group, identifier);
    }

    private int cost(Group group, boolean part1) {
        return part1 ? area(group) * perimeter(group) : area(group) * sides(group);
    }

    private String identifier(Group group) {
        return map[group.coordinates.iterator().next().y][group.coordinates.iterator().next().x];
    }

    private int area(Group group) {
        return group.coordinates.size();
    }

    /**
     * Calculates the perimeter of a group of coordinates.
     * The perimeter is defined as the number of edges that are adjacent to cells
     * with different identifiers or are out of bounds.
     *
     * @param group the set of coordinates representing the group
     * @return the perimeter of the group
     */
    private int perimeter(Group group) {
        int perimeter = 0;

        // Count the neighbors with different identifiers of each cell in the group
        for (Coordinates coordinates : group.coordinates) {
            int y = coordinates.y;
            int x = coordinates.x;

            if (outOfBounds(y + 1) || !group.coordinates.contains(new Coordinates(y + 1, x))) {
                perimeter++;
            }

            if (outOfBounds(y - 1) || !group.coordinates.contains(new Coordinates(y - 1, x))) {
                perimeter++;
            }

            if (outOfBounds(x + 1) || !group.coordinates.contains(new Coordinates(y, x + 1))) {
                perimeter++;
            }

            if (outOfBounds(x - 1) || !group.coordinates.contains(new Coordinates(y, x - 1))) {
                perimeter++;
            }
        }

        return perimeter;
    }

    private int sides(Group group) {
        for (Coordinates coordinates : group.coordinates) {
            int y = coordinates.y;
            int x = coordinates.x;

            if (outOfBounds(y + 1) || !group.coordinates.contains(new Coordinates(y + 1, x))) {
                Side side = getSide(true, y, y + 1, Orientation.DOWN, coordinates);
                group.sides.add(side);
            }

            if (outOfBounds(y - 1) || !group.coordinates.contains(new Coordinates(y - 1, x))) {
                Side side = getSide(true, y - 1, y, Orientation.UP, coordinates);
                group.sides.add(side);
            }

            if (outOfBounds(x + 1) || !group.coordinates.contains(new Coordinates(y, x + 1))) {
                Side side = getSide(false, x, x + 1, Orientation.RIGHT, coordinates);
                group.sides.add(side);
            }

            if (outOfBounds(x - 1) || !group.coordinates.contains(new Coordinates(y, x - 1))) {
                Side side = getSide(false, x - 1, x, Orientation.LEFT, coordinates);
                group.sides.add(side);
            }
        }

        return group.sides.size();
    }

    private Side getSide(boolean horizontal, int smaller_xOrY, int bigger_xOrY, Orientation orientation, Coordinates current) {
        int first, last;

        // Find the first and last x values of the side
        if (horizontal) {
            first = last = current.x;

            // At the top or bottom of the map
            if ((current.y == 0 && orientation == Orientation.UP) || (current.y == map.length - 1 && orientation == Orientation.DOWN)) {
                while (first - 1 >= 0 &&
                        map[current.y][first - 1].equals(map[current.y][current.x])) {
                    first--;
                }

                while (last + 1 < map.length &&
                        map[current.y][last + 1].equals(map[current.y][current.x])) {
                    last++;
                }
            }

            // Somewhere in the middle
            else {
                if (orientation == Orientation.UP) {
                    while (first - 1 >= 0 &&
                            map[current.y][first - 1].equals(map[current.y][current.x]) &&
                            !map[current.y - 1][first - 1].equals(map[current.y][current.x])) {
                        first--;
                    }

                    while (last + 1 < map.length &&
                            map[current.y][last + 1].equals(map[current.y][current.x]) &&
                            !map[current.y - 1][last + 1].equals(map[current.y][current.x])) {
                        last++;
                    }
                }

                // Orientation.DOWN
                else {
                    while (first - 1 >= 0 &&
                            map[current.y][first - 1].equals(map[current.y][current.x]) &&
                            !map[current.y + 1][first - 1].equals(map[current.y][current.x])) {
                        first--;
                    }

                    while (last + 1 < map.length &&
                            map[current.y][last + 1].equals(map[current.y][current.x]) &&
                            !map[current.y + 1][last + 1].equals(map[current.y][current.x])) {
                        last++;
                    }
                }
            }

            return new Side(true, smaller_xOrY, bigger_xOrY, orientation, first, last);
        }

        // Find the first and last y values of the side
        else {
            first = last = current.y; // Go down from current cell

            // At the left or right of the map
            if ((current.x == 0 && orientation == Orientation.LEFT) || (current.x == map.length - 1 && orientation == Orientation.RIGHT)) {
                while (first - 1 >= 0 &&
                        map[first - 1][current.x].equals(map[current.y][current.x])) {
                    first--;
                }

                while (last + 1 < map.length &&
                        map[last + 1][current.x].equals(map[current.y][current.x])) {
                    last++;
                }
            }

            // Somewhere in the middle
            else {
                if (Orientation.LEFT == orientation) {
                    while (first - 1 >= 0 &&
                            map[first - 1][current.x].equals(map[current.y][current.x]) &&
                            !map[first - 1][current.x - 1].equals(map[current.y][current.x])) {
                        first--;
                    }

                    while (last + 1 < map.length &&
                            map[last + 1][current.x].equals(map[current.y][current.x]) &&
                            !map[last + 1][current.x - 1].equals(map[current.y][current.x])) {
                        last++;
                    }
                }

                // Orientation.RIGHT
                else {
                    while (first - 1 >= 0 &&
                            map[first - 1][current.x].equals(map[current.y][current.x]) &&
                            !map[first - 1][current.x + 1].equals(map[current.y][current.x])) {
                        first--;
                    }

                    while (last + 1 < map.length &&
                            map[last + 1][current.x].equals(map[current.y][current.x]) &&
                            !map[last + 1][current.x + 1].equals(map[current.y][current.x])) {
                        last++;
                    }
                }
            }

            return new Side(false, smaller_xOrY, bigger_xOrY, orientation, first, last);
        }
    }

    private boolean outOfBounds(int xOrY) {
        return xOrY < 0 || xOrY >= map.length;
    }

    private void printMap() {
        for (String[] row : map) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }
}