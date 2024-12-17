package Utils;

import java.util.*;

public class Dijkstra {
    public Graph calculateShortestPathFromSource(Graph graph, NodeDji source) {
        source.setDistance(0);

        Set<NodeDji> settledNodes = new HashSet<>();
        Set<NodeDji> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            NodeDji currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<NodeDji, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                NodeDji adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static NodeDji getLowestDistanceNode(Set<NodeDji> unsettledNodes) {
        NodeDji lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (NodeDji node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(NodeDji evaluationNode, Integer edgeWeigh, NodeDji sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<NodeDji> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}

class Graph {
    private final Set<NodeDji> nodes = new HashSet<>();
    private NodeDji source;

    public void addNode(NodeDji nodeA) {
        nodes.add(nodeA);
    }

    // getters and setters
    public Set<NodeDji> getNodes() {
        return nodes;
    }

    public void setSource(NodeDji source) {
        this.source = source;
    }

    public NodeDji getSource() {
        return source;
    }
}

record Coordinate(int x, int y) {
}

class NodeDji {
    // Either use a Coordinate object or use a name
    private Coordinate coordinates;
//    private String name;

    private List<NodeDji> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    Map<NodeDji, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(NodeDji destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public NodeDji(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    //    public Node(String name) {
    // this.name = name;
//    }

    // getters and setters
    public String getCoordinates() {
        return coordinates.toString();
    }

    public List<NodeDji> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<NodeDji> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<NodeDji, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void addAdjacentNode(NodeDji node, Integer distance) {
        adjacentNodes.put(node, distance);
    }
}
