package Days.Day23;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day23 implements Day {
    // A map of nearby hiking trails that indicates paths (.), forest (#), and steep slopes (^, >, v, and <).
    private final List<List<Node>> trailMap = new ArrayList<>();

    record Coordinate(int row, int col) {
    }

    // Used for DFS
    private record NodeInfo(Node node, int distance, Node from, List<Node> visited) {
    }

    /*
     * A node in the graph
     * - null if forest
     */
    static class Node {
        char type;
        List<Node> neighbors = new ArrayList<>(), blockedNeighbors = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        Coordinate coordinate;
        boolean finalNode = false;
        boolean reduced = false;

        public Node(char type, int row, int col) {
            this.type = type;
            this.coordinate = new Coordinate(row, col);
        }

        public void addNeighbor(Node neighbor) {
            this.neighbors.add(neighbor);
            this.distances.add(1);
        }

        public void addBlockedNeighbor(Node neighbor) {
            addNeighbor(neighbor);
            this.blockedNeighbors.add(neighbor);
        }
    }

    public static void main(String[] args) {
        Day day23 = new Day23();
        day23.loadData(Helper.filename(23));
        System.out.println(day23.part1());
        day23.reset();
        System.out.println(day23.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split("");
                List<Node> row = new ArrayList<>();

                for (String node : split) {
                    if (node.equals("#"))
                        row.add(null);
                    else
                        row.add(new Node(node.charAt(0), trailMap.size(), row.size()));
                }

                trailMap.add(row);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        Node finalNode = trailMap.getLast().get(trailMap.getLast().size() - 2);
        finalNode.finalNode = true;
    }

    @Override
    public String part1() {
        addNeighbours(1);
        reduceNeighbours(1);

        return String.valueOf(dfs(1));
    }

    @Override
    public String part2() {
        addNeighbours(2);
        reduceNeighbours(2);

        return String.valueOf(dfs(2));
    }

    public void reset() {
        trailMap.clear();
        loadData(Helper.filename(23));
    }

    // Connects all nodes to their neighbors
    private void addNeighbours(int part) {
        for (int rowIndex = 0; rowIndex < trailMap.size(); rowIndex++) {
            for (int colIndex = 0; colIndex < trailMap.get(rowIndex).size(); colIndex++) {
                Node node = trailMap.get(rowIndex).get(colIndex);

                // Skip forests
                if (node == null) continue;

                if (part == 1 && node.type == '<' || node.type == '>' || node.type == '^' || node.type == 'v') {
                    addNeighbours_Slope(node, rowIndex, colIndex);
                }

                // Part 2 considers slopes as regular paths
                else {
                    addNeighbours_Regular(node, rowIndex, colIndex);
                }
            }
        }
    }

    // Adds neighbors to a regular path
    private void addNeighbours_Regular(Node node, int rowIndex, int colIndex) {
        List<Node> neighbors = new ArrayList<>();

        if (rowIndex > 0)
            neighbors.add(trailMap.get(rowIndex - 1).get(colIndex));

        if (rowIndex < trailMap.size() - 1)
            neighbors.add(trailMap.get(rowIndex + 1).get(colIndex));

        if (colIndex > 0)
            neighbors.add(trailMap.get(rowIndex).get(colIndex - 1));

        if (colIndex < trailMap.get(rowIndex).size() - 1)
            neighbors.add(trailMap.get(rowIndex).get(colIndex + 1));

        for (Node neighbor : neighbors) {
            if (neighbor != null && !node.neighbors.contains(neighbor)) {
                node.addNeighbor(neighbor);
            }
        }
    }

    // Adds neighbors to a slope (one-way path)
    private void addNeighbours_Slope(Node node, int rowIndex, int colIndex) {
        Node to, from;

        switch (node.type) {
            case '^' -> {
                to = trailMap.get(rowIndex - 1).get(colIndex);
                from = trailMap.get(rowIndex + 1).get(colIndex);
            }
            case '>' -> {
                to = trailMap.get(rowIndex).get(colIndex + 1);
                from = trailMap.get(rowIndex).get(colIndex - 1);
            }
            case 'v' -> {
                to = trailMap.get(rowIndex + 1).get(colIndex);
                from = trailMap.get(rowIndex - 1).get(colIndex);
            }
            case '<' -> {
                to = trailMap.get(rowIndex).get(colIndex - 1);
                from = trailMap.get(rowIndex).get(colIndex + 1);
            }
            default -> throw new RuntimeException("Invalid node type: " + node.type);
        }

        node.addNeighbor(to);
        node.addBlockedNeighbor(from);
        to.addBlockedNeighbor(node);
    }

    // Reduces the graph by removing one-way paths (leaves intersections only)
    private void reduceNeighbours(int part) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(trailMap.getFirst().get(1));

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            reduceNeighbours(node, part);
            node.reduced = true;

            for (Node neighbor : node.neighbors) {
                if (!neighbor.equals(node) && !neighbor.reduced && !neighbor.finalNode) {
                    queue.add(neighbor);
                }
            }
        }
    }

    // For a given node, reduce its neighbors to the closest intersection
    private void reduceNeighbours(Node node, int part) {
        for (int i = 0; i < node.neighbors.size(); i++) {
            Node neighbor = node.neighbors.get(i);
            int distance = node.distances.get(i);

            if (node.blockedNeighbors.contains(neighbor))
                continue;
            else if (neighbor.reduced)
                continue;

            while (true) {
                if (neighbor.finalNode) {
                    return;
                }

                // If the neighbor is a regular path
                if (neighbor.type == '.' || part == 2) {

                    // If it has two neighbors, it's a one-way path, so we can reduce it to its neighbor
                    if (neighbor.neighbors.size() == 2) {
                        int j = neighbor.neighbors.indexOf(node);
                        Node other = neighbor.neighbors.get((j + 1) % 2);
                        int distanceToOther = distance + neighbor.distances.get(j);

                        node.neighbors.set(i, other);
                        node.distances.set(i, distanceToOther);

                        int k = other.neighbors.indexOf(neighbor);
                        other.neighbors.set(k, node);
                        other.distances.set(k, distanceToOther);

                        neighbor = other;
                    }

                    // If it has more than two neighbors, it's a fork, so we can't reduce it
                    else break;
                }

                // If the neighbor is a slope, we can reduce it to its neighbor (since it's a one-way path)
                else if (neighbor.type == '^' || neighbor.type == '>' || neighbor.type == 'v' || neighbor.type == '<') {
                    Node other = getNextNode(neighbor);

                    int j = neighbor.neighbors.indexOf(node);
                    int distanceToOther = distance + neighbor.distances.get(j);

                    node.neighbors.set(i, other);
                    node.distances.set(i, distanceToOther);

                    int k = other.neighbors.indexOf(neighbor);
                    other.neighbors.set(k, node);
                    other.distances.set(k, distanceToOther);

                    other.blockedNeighbors.add(node);

                    neighbor = other;
                }
            }
        }
    }

    // Given a slope, return the next node in the direction of the slope
    private Node getNextNode(Node slope) {
        Node other = null;

        switch (slope.type) {
            case '^' -> other = trailMap.get(slope.coordinate.row - 1).get(slope.coordinate.col);
            case '>' -> other = trailMap.get(slope.coordinate.row).get(slope.coordinate.col + 1);
            case 'v' -> other = trailMap.get(slope.coordinate.row + 1).get(slope.coordinate.col);
            case '<' -> other = trailMap.get(slope.coordinate.row).get(slope.coordinate.col - 1);
        }
        return other;
    }

    // DFS to find the longest path
    private int dfs(int part) {
        int longestDistance = 0;
        Stack<NodeInfo> stack = new Stack<>();
        stack.push(new NodeInfo(trailMap.getFirst().get(1), 0, null, new ArrayList<>()));

        while (!stack.isEmpty()) {
            NodeInfo nodeInfo = stack.pop();
            Node node = nodeInfo.node;
            int distance = nodeInfo.distance;
            List<Node> visited = new ArrayList<>(nodeInfo.visited);
            visited.add(node);

            // Termination condition (reached the end)
            if (node.finalNode) {
//                System.out.println("Distance: " + distance);

                longestDistance = Math.max(longestDistance, distance);
                continue;
            }

            // Add neighbors to stack
            for (int i = 0; i < node.neighbors.size(); i++) {
                Node neighbor = node.neighbors.get(i);
                int distanceToNeighbor = node.distances.get(i);

                NodeInfo item = new NodeInfo(neighbor, distance + distanceToNeighbor, node, visited);
                // Condition for part 1: only add neighbors that aren't blocked by slopes
                if (part == 1 && !node.blockedNeighbors.contains(neighbor)) {
                    stack.push(item);
                }

                // Condition for part 2: only add neighbors that haven't been visited
                else if (part == 2 && !visited.contains(neighbor)) {
                    stack.push(item);
                }
            }
        }

        return longestDistance;
    }

    private void printMap() {
        for (List<Node> row : trailMap) {
            for (Node node : row) {
                if (node == null) {
                    System.out.print("#");
                } else {
                    System.out.print(node.type);
                }
            }
            System.out.println();
        }
    }
}