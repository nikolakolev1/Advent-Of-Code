package Days.Day17.Other;

import General.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17 {

    protected Day17(String filename) {
        final List<String> input = getStringList(filename);

        Instant start = Instant.now();
        final String output1 = runPart1(input);
        Instant end = Instant.now();
        System.out.println("Answer to part 1: " + output1);
        System.out.println("Runtime: " + Duration.between(start, end).toMillis() + " ms.");

        start = Instant.now();
        final String output2 = runPart2(input);
        end = Instant.now();
        System.out.println("Answer to part 2: " + output2);
        System.out.println("Runtime: " + Duration.between(start, end).toMillis() + " ms.");
    }

    private List<String> getStringList(final String filename) {
        try (Stream<String> stream = Files.lines(Path.of(Helper.filename(17)))) {
            return stream.collect(Collectors.toList());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected List<Integer> convertToIntList(final List<String> input) {
        return input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    protected List<Long> convertToLongList(final List<String> input) {
        return input.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    private int[][] readGrid(final List<String> input) {
        int[][] grid = new int[input.size()][input.get(0).length()];
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x) - '0';
            }
        }
        return grid;
    }

    private int dijkstra(int[][] grid, boolean part1) {
        Queue<Element> queue = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();
        int endX = grid[grid.length - 1].length - 1;
        int endY = grid.length - 1;

        Node eastStart = new Node(1, 0, 1, Element.EAST);
        Node southStart = new Node(0, 1, 1, Element.SOUTH);
        queue.add(new Element(eastStart, grid[0][1]));
        queue.add(new Element(southStart, grid[1][0]));

        while (!queue.isEmpty()) {
            final Element current = queue.poll();
            if (visited.contains(current.getNode())) {
                continue;
            }
            visited.add(current.getNode());
            if (current.getNode().x() == endX && current.getNode().y() == endY
                    && (part1 || current.getNode().blocks() >= 4)) {
                return current.getHeatLoss();
            }

            queue.addAll(part1 ? current.getNeighbours(grid) : current.getNeighboursForPart2(grid));

        }

        return 0;
    }

    protected String runPart2(final List<String> input) {
        return String.valueOf(dijkstra(readGrid(input), false));
    }

    protected String runPart1(final List<String> input) {
        return String.valueOf(dijkstra(readGrid(input), true));
    }

    public static void main(String... args) {
        new Day17(Helper.filename_test(17));
    }
}
