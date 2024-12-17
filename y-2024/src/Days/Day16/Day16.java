package Days.Day16;

import General.Day;

import java.io.File;
import java.util.*;

import static Days.Day16.Direction.UP;
import static Days.Day16.Direction.DOWN;
import static Days.Day16.Direction.LEFT;
import static Days.Day16.Direction.RIGHT;

public class Day16 implements Day {
    private boolean[][] grid; // false = wall, true = open; grid[y][x]
    private List<Node>[][] nodesGrid; // each list contains 4 nodes: up, down, left, right
    private Coordinate start, end;
    private Graph graph = new Graph();

    public static void main(String[] args) {
        Day day16 = new Day16();
        day16.loadData("data/day16/input.txt");
        System.out.println(day16.part1());
        System.out.println(day16.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int y = 0;
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split("");

                // Create grid if it doesn't exist (first line)
                if (grid == null) {
                    grid = new boolean[split.length][split.length];
                    nodesGrid = new List[split.length][split.length];
                }

                // Fill grid
                for (int x = 0; x < split.length; x++) {
                    grid[y][x] = split[x].equals(".");

                    // Save starting and ending positions
                    if (split[x].equals("S")) {
                        start = new Coordinate(x, y);
                        grid[y][x] = true;
                    } else if (split[x].equals("E")) {
                        end = new Coordinate(x, y);
                        grid[y][x] = true;
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
        printGrid();

        addNodes();
        connectNodes();
        setStartAndEnd();

        Dijkstra dijkstra = new Dijkstra();
        graph = dijkstra.calculateShortestPathFromSource(graph, graph.getSource());

        System.out.println("\n--- Shortest path - destination 1 ---");
        printShortestPath(graph.getDestination1());
        System.out.println("\n--- Shortest path - destination 2 ---");
        printShortestPath(graph.getDestination2());

        int result = Math.min(graph.getDestination1().getDistance(), graph.getDestination2().getDistance());
        return String.valueOf(result);
    }

    @Override
    public String part2() {
        int sum = 0;

        // TODO: Implement part 2

        return String.valueOf(sum);
    }

    private void setStartAndEnd() {
        graph.setSource(start, nodesGrid);
        graph.setDestination(end, nodesGrid);
    }

    private void addNodes() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x]) {
                    Coordinate coordinates = new Coordinate(x, y);
                    Node nodeUp = new Node(coordinates, UP);
                    Node nodeDown = new Node(coordinates, DOWN);
                    Node nodeLeft = new Node(coordinates, LEFT);
                    Node nodeRight = new Node(coordinates, RIGHT);

                    // Add nodes to graph
                    graph.addNode(nodeUp);
                    graph.addNode(nodeDown);
                    graph.addNode(nodeLeft);
                    graph.addNode(nodeRight);

                    // Add nodes to grid
                    List<Node> nodes = new ArrayList<>();
                    nodes.add(nodeUp);
                    nodes.add(nodeDown);
                    nodes.add(nodeLeft);
                    nodes.add(nodeRight);
                    nodesGrid[y][x] = nodes;
                }
            }
        }
    }

    private void connectNodes() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x]) {
                    List<Node> nodes = nodesGrid[y][x];
                    Node nodeUp = nodes.get(UP.ordinal());
                    Node nodeDown = nodes.get(DOWN.ordinal());
                    Node nodeLeft = nodes.get(LEFT.ordinal());
                    Node nodeRight = nodes.get(RIGHT.ordinal());

                    // Connect nodes
                    // Going up
                    if (y > 0 && grid[y - 1][x]) {
                        nodeUp.addAdjacentNode(nodesGrid[y - 1][x].get(UP.ordinal()), 1);
                        nodeLeft.addAdjacentNode(nodesGrid[y - 1][x].get(UP.ordinal()), 1000 + 1);
                        nodeRight.addAdjacentNode(nodesGrid[y - 1][x].get(UP.ordinal()), 1000 + 1);
                    }

                    // Going down
                    if (y < grid.length - 1 && grid[y + 1][x]) {
                        nodeDown.addAdjacentNode(nodesGrid[y + 1][x].get(DOWN.ordinal()), 1);
                        nodeLeft.addAdjacentNode(nodesGrid[y + 1][x].get(DOWN.ordinal()), 1000 + 1);
                        nodeRight.addAdjacentNode(nodesGrid[y + 1][x].get(DOWN.ordinal()), 1000 + 1);
                    }

                    // Going left
                    if (x > 0 && grid[y][x - 1]) {
                        nodeLeft.addAdjacentNode(nodesGrid[y][x - 1].get(LEFT.ordinal()), 1);
                        nodeUp.addAdjacentNode(nodesGrid[y][x - 1].get(LEFT.ordinal()), 1000 + 1);
                        nodeDown.addAdjacentNode(nodesGrid[y][x - 1].get(LEFT.ordinal()), 1000 + 1);
                    }

                    // Going right
                    if (x < grid[y].length - 1 && grid[y][x + 1]) {
                        nodeRight.addAdjacentNode(nodesGrid[y][x + 1].get(RIGHT.ordinal()), 1);
                        nodeUp.addAdjacentNode(nodesGrid[y][x + 1].get(RIGHT.ordinal()), 1000 + 1);
                        nodeDown.addAdjacentNode(nodesGrid[y][x + 1].get(RIGHT.ordinal()), 1000 + 1);
                    }
                }
            }
        }
    }

    // This print method does not show the start and end positions
    private void printGrid() {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print(cell ? "." : "#");
            }
            System.out.println();
        }
    }

    private void printShortestPath(Node node) {
        Map<Coordinate, Direction> map = new HashMap<>();
        for (Node n : node.getShortestPath()) {
            map.put(n.getCoordinates(), n.getDirection());
        }

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (map.containsKey(new Coordinate(x, y))) {
                    System.out.print(map.get(new Coordinate(x, y)).toString());
                } else {
                    System.out.print(grid[y][x] ? "." : "#");
                }
            }
            System.out.println();
        }
    }
}

class Dijkstra {
    private boolean destination1Reached, destination2reached;

    public Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);

            // If we have reached the destination (from both possible directions), we can stop
            if (currentNode.equals(graph.getDestination1())) {
                destination1Reached = true;

                if (destination2reached) break;
            }
            if (currentNode.equals(graph.getDestination2())) {
                destination2reached = true;

                if (destination1Reached) break;
            }
        }
        return graph;
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}

class Graph {
    private final Set<Node> nodes = new HashSet<>();
    private Node source, destination1, destination2;

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    // getters and setters
    public Set<Node> getNodes() {
        return nodes;
    }

    public void setSource(Coordinate source, List<Node>[][] nodesGrid) {
        this.source = nodesGrid[source.y()][source.x()].get(RIGHT.ordinal());
    }

    public Node getSource() {
        return source;
    }

    public void setDestination(Coordinate destination, List<Node>[][] nodesGrid) {
        // Hardcoded
        destination1 = nodesGrid[destination.y()][destination.x()].get(RIGHT.ordinal());
        destination2 = nodesGrid[destination.y()][destination.x()].get(UP.ordinal());
    }

    public Node getDestination1() {
        return destination1;
    }

    public Node getDestination2() {
        return destination2;
    }
}

record Coordinate(int x, int y) {
}

enum Direction {
    UP {
        @Override
        public String toString() {
            return "^";
        }
    },
    DOWN {
        @Override
        public String toString() {
            return "v";
        }
    },
    LEFT {
        @Override
        public String toString() {
            return "<";
        }
    },
    RIGHT {
        @Override
        public String toString() {
            return ">";
        }
    }
}

class Node {
    private Coordinate coordinates;
    private Direction direction; // when entering this node, we are facing this direction (if we are coming from the left, we are facing right => RIGHT)

    private List<Node> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(Coordinate coordinates, Direction direction) {
        this.coordinates = coordinates;
        this.direction = direction;
    }

    // getters and setters
    public Coordinate getCoordinates() {
        return coordinates;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void addAdjacentNode(Node node, Integer distance) {
        adjacentNodes.put(node, distance);
    }
}