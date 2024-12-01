package Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {

    public String value;
    public List<Node> connections;
    public boolean indicator = false;

    public Node(String value) {
        this.value = value;
        connections = new ArrayList<>();
    }

    public void addConnection(Node node) {
        this.connections.add(node);
    }

    public void addConnections(List<Node> nodes) {
        this.connections.addAll(nodes);
    }

    public void addConnections(Node... nodes) {
        Collections.addAll(this.connections, nodes);
    }

    public void addBidirectionalConnection(Node node) {
        this.connections.add(node);
        node.connections.add(this);
    }

    public void removeBidirectionalConnection(Node node) {
        this.connections.remove(node);
        node.connections.remove(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(value).append(": ");
        for (Node node : connections) {
            sb.append(" ").append(node.value);
        }

        return sb.toString();
    }
}