package Days.Day19;

import General.Day;

import java.io.File;
import java.util.*;

public class Day19 implements Day {
    private final List<Part> parts = new ArrayList<>();
    private final HashMap<String, Workflow> workflows = new HashMap<>();
    private final List<Part> acceptedParts = new ArrayList<>();

    // Represents a machine part. Each machine part has a rating in four categories (attributes): x, m, a, and s.
    private record Part(int x, int m, int a, int s) {
        public int value() {
            return x + m + a + s;
        }
    }

    // Each part is sent through a series of workflows that will ultimately accept or reject the part
    private record Workflow(String name, List<Condition> conditions) {
    }

    // Each condition is a comparison of an attribute to a value
    static class Condition {
        char attribute;
        char operator;
        int value;
        String doIfHolds;

        // Workflow, Condition or Boolean (True = accept, False = reject)
        Object cond1, cond2; // For part 2

        Condition(char attribute, char operator, int value, String doIfHolds) {
            this.attribute = attribute;
            this.operator = operator;
            this.value = value;
            this.doIfHolds = doIfHolds;
        }

        private boolean holds(Part part) {
            int partValue = switch (attribute) {
                case 'x' -> part.x;
                case 'm' -> part.m;
                case 'a' -> part.a;
                case 's' -> part.s;
                default -> throw new IllegalStateException("Unexpected value: " + operator);
            };

            return switch (operator) {
                case '>' -> partValue > value;
                case '<' -> partValue < value;
                default -> throw new IllegalStateException("Unexpected value: " + operator);
            };
        }
    }

    // Inclusive range (for part 2)
    private record Range(int min, int max) {
        private int size() {
            return max - min + 1;
        }
    }

    public static void main(String[] args) {
        Day day19 = new Day19();
        day19.loadData(Day.filename(19));
        System.out.println(day19.part1());
        System.out.println(day19.part2());
    }

    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            boolean isWorkflow = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isBlank()) {
                    isWorkflow = false;
                    continue;
                }

                if (isWorkflow) {
                    Workflow wf = parseWorkflow(line);
                    workflows.put(wf.name, wf);
                } else {
                    parts.add(parsePart(line));
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Given a line of input, parse it into a Workflow object
    private Workflow parseWorkflow(String line) {
        String[] split = line.substring(0, line.length() - 1).split("\\{");

        String name = split[0];
        String[] conditions = split[1].split(",");

        List<Condition> conditionList = new ArrayList<>();
        for (String condStr : conditions) {
            conditionList.add(parseCondition(condStr));
        }

        return new Workflow(name, conditionList);
    }

    // Given a line of input, parse it into a Part object
    private Part parsePart(String line) {
        String[] attributes = line.trim().substring(1, line.length() - 1).split(",");
        int[] values = new int[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            values[i] = Integer.parseInt(attributes[i].split("=")[1]);
        }

        return new Part(values[0], values[1], values[2], values[3]);
    }

    // Given a string, parse it into a Condition object
    private Condition parseCondition(String condStr) {
        // If is last condition in workflow
        if (!condStr.contains(":")) {
            // Always true
            return new Condition('x', '>', 0, condStr);
        }

        // If is regular condition
        else {
            String[] split = condStr.split(":");

            char attribute = split[0].charAt(0);
            char operator = split[0].charAt(1);
            int value = Integer.parseInt(split[0].substring(2));
            String ifTrue = split[1];

            return new Condition(attribute, operator, value, ifTrue);
        }
    }

    public String part1() {
        organizeParts();

        return String.valueOf(acceptedParts.stream().mapToInt(Part::value).sum());
    }

    public String part2() {
        connectConditions(workflows.get("in"));

        return String.valueOf(eval(workflows.get("in").conditions.getFirst(), new Range[]{new Range(1, 4000), new Range(1, 4000), new Range(1, 4000), new Range(1, 4000)}));
    }

    // Send each part through the workflows
    private void organizeParts() {
        for (Part part : parts) {
            organizePart(part, workflows.get("in"));
        }
    }

    // If the part is accepted, add it to the acceptedParts list
    private void organizePart(Part part, Workflow workflow) {
        for (Condition condition : workflow.conditions) {
            if (condition.holds(part)) {
                // If "A", accept the part
                if (condition.doIfHolds.equals("A")) {
                    acceptedParts.add(part);
                }

                // Else, as long as it's not "R", send it to the next workflow
                else if (!condition.doIfHolds.equals("R")) {
                    organizePart(part, workflows.get(condition.doIfHolds));
                }

                return; // Terminate
            }
        }
    }

    // ------------------------------- Part 2 -------------------------------
    // Assigns the cond1 and cond2 fields of each condition (builds the condition tree)
    private void connectConditions(Workflow workflow) {
        Condition prev, curr = workflow.conditions.getFirst();

        for (int i = 1; i < workflow.conditions.size(); i++) {
            // Move to next condition (keeping track of previous)
            prev = curr;
            curr = workflow.conditions.get(i);

            // Set cond1
            if (prev.doIfHolds.equals("A")) {
                prev.cond1 = true;
            } else if (prev.doIfHolds.equals("R")) {
                prev.cond1 = false;
            } else {
                Workflow next = workflows.get(prev.doIfHolds);
                prev.cond1 = next.conditions.getFirst();
                connectConditions(next);
            }

            // Set cond2
            prev.cond2 = curr;
        }

        // Set cond1 of last condition in the workflow
        if (curr.doIfHolds.equals("A")) {
            curr.cond1 = true;
        } else if (curr.doIfHolds.equals("R")) {
            curr.cond1 = false;
        } else {
            Workflow next = workflows.get(curr.doIfHolds);
            curr.cond1 = next.conditions.getFirst();
            connectConditions(next);
        }
    }

    // Recursively evaluates the condition tree

    /* Workflows: (only have two attributes, x and m)
     *    in{x<1000:aaa,bbb}
     *    aaa{m<1000:A,R}
     *    bbb{m>2000:A,R}
     *
     * Trace:
     *  ┌ eval(in: 'x<1000', {'x': (1, 4000), 'm': (1, 4000)})
     *  │ ┌ eval(aaa: 'm<1000', {'x': (1, 999), 'm': (1, 4000)})
     *  │ │ ┌ eval(true, {'x': (1, 999), 'm': (1, 999)})
     *  │ │ └> 998001
     *  | |
     *  │ │ ┌ eval(false, {'x': (1, 999), 'm': (1000, 4000)})
     *  │ │ └> 0
     *  │ └> 998001
     *  |
     *  │ ┌ eval(bbb: 'm>2000', {'x': (1000, 4000), 'm': (1, 4000)})
     *  │ │ ┌ eval(true, {'x': (1000, 4000), 'm': (2001, 4000)})
     *  │ │ └> 6002000
     *  | |
     *  │ │ ┌ eval(false, {'x': (1000, 4000), 'm': (1, 2000)})
     *  │ │ └> 0
     *  │ └> 6002000
     *  └> 7000001
     */
    private long eval(Object obj, Range[] ranges) {
        if (obj == null) return 0;

        // Terminate if obj is a boolean
        if (obj instanceof Boolean) {
            return (Boolean) obj ? multiplyRanges(ranges) : 0;
        }

        Condition cond = (Condition) obj;
        int bound = cond.value;
        Range[] newRanges1 = ranges.clone(), newRanges2 = ranges.clone();

        int attr = switch (cond.attribute) {
            case 'x' -> 0;
            case 'm' -> 1;
            case 'a' -> 2;
            case 's' -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + cond.attribute);
        };

        if (cond.operator == '>') {
            newRanges1[attr] = new Range(Math.max(bound + 1, ranges[attr].min), ranges[attr].max);
            newRanges2[attr] = new Range(ranges[attr].min, Math.min(bound, ranges[attr].max));
        } else { // <
            newRanges1[attr] = new Range(ranges[attr].min, Math.min(ranges[attr].max, bound - 1));
            newRanges2[attr] = new Range(Math.max(bound, ranges[attr].min), ranges[attr].max);
        }

        return eval(cond.cond1, newRanges1) + eval(cond.cond2, newRanges2);
    }

    // Multiplies the sizes of the 4 ranges (the 4 ranges are the x, m, a, and s possible values)
    private long multiplyRanges(Range[] ranges) {
        long product = 1;

        for (Range range : ranges) {
            product *= range.size();
        }

        return product;
    }
}