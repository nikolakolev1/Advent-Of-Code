package Days.Day10;

import General.Day;

import java.io.File;
import java.util.*;

public class Day10 implements Day {
    private record Coordinates(int x, int y) {
    }

    private class Node {
        private final int value;
        private List<Node> neighbors;
        private Coordinates coordinates;

        public Node(int value) {
            this.value = value;
        }

        private void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        private void setNeighbors() {
            List<Node> neighbors = new ArrayList<>();

            List<Node> adjacentNodes = adjacentNodes();
            for (Node adjacentNode : adjacentNodes) {
                if (adjacentNode.value - value == 1) {
                    neighbors.add(adjacentNode);
                }
            }

            this.neighbors = neighbors;
        }

        private List<Node> adjacentNodes() {
            List<Node> adjacentNodes = new ArrayList<>();

            if (coordinates.x != 0) {
                adjacentNodes.add(topography[coordinates.y][coordinates.x - 1]);
            }
            if (coordinates.x != topography.length - 1) {
                adjacentNodes.add(topography[coordinates.y][coordinates.x + 1]);
            }
            if (coordinates.y != 0) {
                adjacentNodes.add(topography[coordinates.y - 1][coordinates.x]);
            }
            if (coordinates.y != topography[0].length - 1) {
                adjacentNodes.add(topography[coordinates.y + 1][coordinates.x]);
            }

            return adjacentNodes;
        }

        /**
         * Finds all 9-value nodes that can be reached from this node.
         * Uses Depth-First Search (DFS) to find all possible paths.
         *
         * @return the number of 9-value nodes that can be reached from this node
         */
        private int score() {
            Set<Coordinates> visited = new HashSet<>();
            int score = 0;
            Stack<Node> stack = new Stack<>();
            stack.push(this);

            while (!stack.isEmpty()) {
                Node node = stack.pop();
                visited.add(node.coordinates);

                if (node.value == 9) {
                    score++;
                }

                for (Node neighbor : node.neighbors) {
                    if (!visited.contains(neighbor.coordinates)) {
                        stack.push(neighbor);
                    }
                }
            }

            return score;
        }

        /**
         * Calculates the number of distinct paths to nodes with a value of 9.
         * Uses Depth-First Search (DFS) to traverse the graph.
         *
         * @return the rating of this node
         */
        private int rating() {
            int rating = 0;
            Stack<Node> stack = new Stack<>();
            stack.push(this);

            while (!stack.isEmpty()) {
                Node node = stack.pop();

                if (node.value == 9) {
                    rating++;
                }

                for (Node neighbor : node.neighbors) {
                    stack.push(neighbor);
                }
            }

            return rating;
        }
    }

    private Node[][] topography;

    public static void main(String[] args) {
        Day day10 = new Day10();
        day10.loadData("data/day10/input.txt");
        System.out.println(day10.part1());
        System.out.println(day10.part2());
    }

    @Override
    public void loadData(String filename) {

        try (Scanner scanner = new Scanner(new File(filename))) {
            int i = 0;
            Node[] firstRow = Arrays.stream(scanner.nextLine().split("")).map(s -> new Node(Integer.parseInt(s))).toArray(Node[]::new);

            topography = new Node[firstRow.length][firstRow.length];
            topography[i++] = firstRow;

            while (scanner.hasNextLine()) {
                Node[] row = Arrays.stream(scanner.nextLine().split("")).map(s -> new Node(Integer.parseInt(s))).toArray(Node[]::new);
                topography[i++] = row;
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        setAllNodes();

        for (Node[] row : topography) {
            for (Node node : row) {
                sum += node.value == 0 ? node.score() : 0;
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        setAllNodes();

        for (Node[] row : topography) {
            for (Node node : row) {
                sum += node.value == 0 ? node.rating() : 0;
            }
        }

        return String.valueOf(sum);
    }

    private void setAllNodes() {
        if (topography[0][0].coordinates != null) return;

        for (int y = 0; y < topography.length; y++) {
            for (int x = 0; x < topography[y].length; x++) {
                topography[y][x].setCoordinates(new Coordinates(x, y));
                topography[y][x].setNeighbors();
            }
        }
    }

    private void printTopography() {
        for (Node[] row : topography) {
            for (Node node : row) {
                System.out.print(node.value);
            }
            System.out.println();
        }
    }
}