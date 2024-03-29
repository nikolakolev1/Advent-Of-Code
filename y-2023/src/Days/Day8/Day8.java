package Days.Day8;

import General.Day;
import General.Helper;
import Utils.LCM;
import Utils.Node_LR;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day8 implements Day {
    private String[] instructions;
    private final ArrayList<Node_LR> nodes = new ArrayList<>();
    private final HashMap<String, Node_LR> nodeMap = new HashMap<>();

    public static void main(String[] args) {
        Day day8 = new Day8();
        day8.loadData(Helper.filename(8));
        System.out.println(day8.part1());
        System.out.println(day8.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            boolean firstLine = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (firstLine) {
                    firstLine = false;

                    instructions = line.split("");

                    scanner.nextLine();
                    continue;
                }

                nodes.add(new Node_LR(line.substring(0, 3)));
                nodeMap.put(line.substring(0, 3), nodes.getLast());
            }

            scanner = new Scanner(input);
            scanner.nextLine();
            scanner.nextLine();

            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] split = line.split("=")[1].split(",");

                Node_LR node = nodes.get(i);

                String leftStr = split[0].trim().substring(1);
                String rightStr = split[1].trim().substring(0, 3);

                node.addLeft(nodeMap.get(leftStr));
                node.addRight(nodeMap.get(rightStr));

                i++;
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        Node_LR current = nodeMap.get("AAA");

        int steps = 0;

        int instructionPointer = 0;
        while (!current.value.equals("ZZZ")) {
            steps++;

            if (instructions[instructionPointer].equals("L")) {
                current = current.left;
            } else {
                current = current.right;
            }

            if (instructionPointer == instructions.length - 1) {
                instructionPointer = 0;
            } else {
                instructionPointer++;
            }
        }

        return String.valueOf(steps);
    }

    @Override
    public String part2() {
        List<Node_LR> nodes_LastCharA = new ArrayList<>();

        for (Node_LR node : nodes) {
            if (node.value.charAt(2) == 'A') {
                nodes_LastCharA.add(node);
            }
        }

        List<Integer> steps = new ArrayList<>();

        for (Node_LR node : nodes_LastCharA) {
            int stepsForNode = getStepsForNode_P2(node);

            steps.add(stepsForNode);
        }

        return LCM.findLCM(steps).toString();
    }

    private int getStepsForNode_P2(Node_LR node) {
        int stepsForNode = 0;

        Node_LR current = node;
        int instructionPointer = 0;
        while (!(current.value.charAt(2) == 'Z')) {
            stepsForNode++;

            if (instructions[instructionPointer].equals("L")) {
                current = current.left;
            } else {
                current = current.right;
            }

            if (instructionPointer == instructions.length - 1) {
                instructionPointer = 0;
            } else {
                instructionPointer++;
            }
        }
        return stepsForNode;
    }
}