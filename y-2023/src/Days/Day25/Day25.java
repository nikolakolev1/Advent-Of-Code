package Days.Day25;

import General.Day;
import General.Helper;
import Utils.Node;

import java.io.File;
import java.util.*;

public class Day25 implements Day {
    private final List<Node> nodes = new ArrayList<>();

    public static void main(String[] args) {
        Day day25 = new Day25();
        day25.loadData(Helper.filename_test(25));
        System.out.println(day25.part1());
        System.out.println(day25.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(": ");

                String nodeValue = split[0].trim();
                String[] connections = split[1].trim().split(" ");

                Node node = getNode(nodeValue);

                for (String connection : connections) {
                    Node connectionNode = getNode(connection);

                    if (!node.connections.contains(connectionNode)) {
                        node.addBidirectionalConnection(connectionNode);
                    }
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        printNodes();

        List<List<Node>> groups1 = divideInGroups();
        printGroups(groups1);

        breakConnection(getNode("hfx"), getNode("pzl"));
        breakConnection(getNode("bvb"), getNode("cmg"));
        breakConnection(getNode("nvd"), getNode("jqt"));

        List<List<Node>> groups2 = divideInGroups();
        printGroups(groups2);

        return "to be implemented";
    }

    @Override
    public String part2() {
        return "to be implemented";
    }

    private Node getNode(String value) {
        for (Node node : nodes) {
            if (node.value.equals(value)) {
                return node;
            }
        }

        Node newNode = new Node(value);
        nodes.add(newNode);
        return newNode;
    }

    private List<List<Node>> divideInGroups() {
        List<List<Node>> groups = new ArrayList<>();

        for (Node node : nodes) {
            if (!node.indicator) {
                List<Node> group = new ArrayList<>();

                Queue<Node> queue = new LinkedList<>();
                queue.add(node);

                while (!queue.isEmpty()) {
                    Node currentNode = queue.poll();

                    group.add(currentNode);
                    currentNode.indicator = true;

                    for (Node connection : currentNode.connections) {
                        if (!connection.indicator && !queue.contains(connection)) {
                            queue.add(connection);
                        }
                    }
                }

                groups.add(group);
            }
        }

        return groups;
    }

    private void breakConnection(Node node1, Node node2) {
        node1.removeBidirectionalConnection(node2);
    }

    private void printNodes() {
        for (Node node : nodes) {
            System.out.println(node);
        }
    }

    private void printGroups(List<List<Node>> groups) {
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("\nGroup " + (i + 1) + ": ");
            for (Node node : groups.get(i)) {
                System.out.println(node.value);
            }
        }
        System.out.println();
    }
}