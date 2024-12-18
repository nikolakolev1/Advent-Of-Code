package Days.Day18;

import General.Day;

import java.io.File;
import java.util.*;

public class Day18 implements Day {
    private record Coordinate(int x, int y) {
    }

    private List<Coordinate> wallCoordinateList = new ArrayList<>();
    private Set<Coordinate> firstXWallCoordinateList = new HashSet<>();
    private Node[][] nodesGrid;
    private Graph graph = new Graph();

    private static final String FILEPATH = "data/day18/input.txt";
    private static final String FILEPATH_TEST = "data/day18/input_test.txt";
    private static final int GRID_SIZE = 71;
    private static final int GRID_SIZE_TEST = 7;
    private static int BYTES = 1024;
    private static int BYTES_TEST = 12;
    private static final boolean testing = false;

    public static void main(String[] args) {
        Day day18 = new Day18();
        if (testing) day18.loadData(FILEPATH_TEST);
        else day18.loadData(FILEPATH);
        System.out.println(day18.part1());
        System.out.println(day18.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(",");

                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);

                wallCoordinateList.add(new Coordinate(x, y));
            }

            int iterations = testing ? BYTES_TEST : BYTES;
            for (int i = 0; i < iterations; i++) {
                firstXWallCoordinateList.add(wallCoordinateList.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        prepareNodesGrid();

        Dijkstra dijkstra = new Dijkstra();
        graph = dijkstra.calculateShortestPathFromSource(graph, graph.getSource());

        System.out.println(wallCoordinateList.get(BYTES_TEST));

        return String.valueOf(graph.getDestination().getDistance());
    }

    @Override
    public String part2() {
        int lastSmallerBytes = 0;
        int lastBiggerBytes = wallCoordinateList.size();
        int currentBytes = lastBiggerBytes;
        boolean wasBelow = false;

        return "Done by hand: 46,18";

//        while (true) {
//            firstXWallCoordinateList.clear();
//
//            if (wasBelow) {
//                lastSmallerBytes = currentBytes;
//                currentBytes = (currentBytes + lastBiggerBytes) / 2;
//            } else {
//                lastBiggerBytes = currentBytes;
//                currentBytes = (currentBytes + lastSmallerBytes) / 2;
//            }
//
//            for (int i = 0; i < currentBytes; i++) {
//                firstXWallCoordinateList.add(wallCoordinateList.get(i));
//            }
//            prepareNodesGrid();
//
//            Dijkstra dijkstra = new Dijkstra();
//            graph = dijkstra.calculateShortestPathFromSource(graph, graph.getSource());
//
//            if (graph.getDestination().getDistance() == Integer.MAX_VALUE) {
//                wasBelow = false;
//
//                firstXWallCoordinateList.remove(wallCoordinateList.get(currentBytes));
//                prepareNodesGrid();
//
//                dijkstra = new Dijkstra();
//                graph = dijkstra.calculateShortestPathFromSource(graph, graph.getSource());
//
//                if (graph.getDestination().getDistance() != Integer.MAX_VALUE) {
//                    return String.valueOf(wallCoordinateList.get(currentBytes));
//                }
//            } else {
//                wasBelow = true;
//            }
//        }
    }

    private void prepareNodesGrid() {
        createNodesGrid();
        connectNodes();
        setStartAndEnd();
    }

    private void createNodesGrid() {
        if (testing) nodesGrid = new Node[GRID_SIZE_TEST][GRID_SIZE_TEST];
        else nodesGrid = new Node[GRID_SIZE][GRID_SIZE];

        for (int y = 0; y < nodesGrid.length; y++) {
            for (int x = 0; x < nodesGrid[y].length; x++) {
                if (!firstXWallCoordinateList.contains(new Coordinate(x, y))) {
                    nodesGrid[y][x] = new Node(new Coordinate(x, y));
                }
            }
        }
    }

    private void connectNodes() {
        for (int y = 0; y < nodesGrid.length; y++) {
            for (int x = 0; x < nodesGrid[y].length; x++) {
                if (nodesGrid[y][x] != null) {
                    Node node = nodesGrid[y][x];

                    // Connect nodes
                    // Up
                    if (y > 0 && (nodesGrid[y - 1][x] != null)) {
                        node.addAdjacentNode(nodesGrid[y - 1][x], 1);
                    }

                    // Down
                    if (y < nodesGrid.length - 1 && (nodesGrid[y + 1][x] != null)) {
                        node.addAdjacentNode(nodesGrid[y + 1][x], 1);
                    }

                    // Left
                    if (x > 0 && (nodesGrid[y][x - 1] != null)) {
                        node.addAdjacentNode(nodesGrid[y][x - 1], 1);
                    }

                    // Right
                    if (x < nodesGrid[y].length - 1 && (nodesGrid[y][x + 1] != null)) {
                        node.addAdjacentNode(nodesGrid[y][x + 1], 1);
                    }
                }
            }
        }
    }

    private void setStartAndEnd() {
        graph.setSource(nodesGrid);
        graph.setDestination(nodesGrid);
    }

    private void printNodesGrid() {
        for (int y = 0; y < nodesGrid.length; y++) {
            for (int x = 0; x < nodesGrid[y].length; x++) {
                if (nodesGrid[y][x] != null) {
                    System.out.print(".");
                } else {
                    System.out.print("#");
                }
            }
            System.out.println();
        }
    }

    class Dijkstra {
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

                // If we have reached the destination, we can stop
                if (currentNode.equals(graph.getDestination())) break;
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
        private Node source, destination;

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

        // getters and setters
        public Set<Node> getNodes() {
            return nodes;
        }

        public void setSource(Node[][] nodesGrid) {
            this.source = nodesGrid[0][0];
        }

        public Node getSource() {
            return source;
        }

        public void setDestination(Node[][] nodesGrid) {
            // Hardcoded
            this.destination = nodesGrid[nodesGrid.length - 1][nodesGrid.length - 1];
        }

        public Node getDestination() {
            return destination;
        }
    }

    class Node {
        private final Coordinate coordinates;

        private List<Node> shortestPath = new LinkedList<>();

        private Integer distance = Integer.MAX_VALUE;

        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public Node(Coordinate coordinates) {
            this.coordinates = coordinates;
        }

        // getters and setters
        public Coordinate getCoordinates() {
            return coordinates;
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
}