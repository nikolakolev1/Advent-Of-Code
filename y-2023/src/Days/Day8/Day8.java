package Days.Day8;

import General.Day;
import General.Helper;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day8 implements Day {
    private String[] instructions;
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final HashMap<String, Node> nodeMap = new HashMap<>();

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

                nodes.add(new Node(line.substring(0, 3)));
                nodeMap.put(line.substring(0, 3), nodes.get(nodes.size() - 1));
            }

            scanner = new Scanner(input);
            scanner.nextLine();
            scanner.nextLine();

            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] split = line.split("=")[1].split(",");

                Node node = nodes.get(i);

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
        Node current = nodeMap.get("AAA");

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
        List<Node> nodes_LastCharA = new ArrayList<>();

        for (Node node : nodes) {
            if (node.value.charAt(2) == 'A') {
                nodes_LastCharA.add(node);
            }
        }

        List<Integer> steps = new ArrayList<>();

        for (Node node : nodes_LastCharA) {
            int stepsForNode = getStepsForNode_P2(node);

            steps.add(stepsForNode);
//            System.out.print(stepsForNode + ", ");
        }
//        System.out.println();

        return LCM(steps).toString();
    }

    private int getStepsForNode_P2(Node node) {
        int stepsForNode = 0;

        Node current = node;
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

    private BigInteger LCM(List<Integer> denominators) {
        ArrayList<Integer> commonMultiples = new ArrayList<>();
        int currentPrime = 2;

        while (!allAreOnes(denominators)) {
            if (anyIsDivisibleBy(denominators, currentPrime)) {
                commonMultiples.add(currentPrime);

                for (int i = 0; i < denominators.size(); i++) {
                    int dividend = denominators.get(i);
                    if (isDivisibleBy(dividend, currentPrime)) {
                        denominators.set(i, (dividend / currentPrime));
                    }
                }
            } else {
                currentPrime = getNextPrime(currentPrime);
            }
        }

        BigInteger lcm = new BigInteger("1");
        for (Integer commonMultiple : commonMultiples) {
            lcm = lcm.multiply(new BigInteger(String.valueOf(commonMultiple)));
        }

        // finish this
        return lcm;
    }

    private int getNextPrime(int currentPrime) {
        int num = currentPrime + 1;

        while (!isPrime(num)) num++;

        return num;
    }

    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }

        for (int i = 2; i <= num / 2; i++) {
            if ((num % i) == 0)
                return false;
        }
        return true;
    }

    private boolean allAreOnes(List<Integer> list) {
        for (Integer i : list) {
            if (i != 1) return false;
        }

        return true;
    }

    private boolean isDivisibleBy(int dividend, int divisor) {
        return dividend % divisor == 0;
    }

    private boolean anyIsDivisibleBy(List<Integer> list, int divisor) {
        for (Integer i : list) {
            if (isDivisibleBy(i, divisor)) return true;
        }

        return false;
    }

    static class Node {
        public Node left;
        public Node right;
        public String value;

        public Node(String value) {
            this.value = value;
        }

        public void addLeft(Node node) {
            this.left = node;
        }

        public void addRight(Node node) {
            this.right = node;
        }
    }
}