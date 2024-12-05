package Days.Day5;

import General.Day;

import java.io.File;
import java.util.*;

public class Day5 implements Day {
    /*
     * Key: A certain value
     * Value: A set of values that cannot be before the key
     */
    private Map<Integer, Set<Integer>> rules = new HashMap<>();
    private List<List<Integer>> updates = new ArrayList<>();

    public static void main(String[] args) {
        Day day5 = new Day5();
        day5.loadData("data/day5/input.txt");
        System.out.println(day5.part1());
        System.out.println(day5.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // A rule
                if (line.contains("|")) {
                    String[] parts = line.split("\\|");
                    int key = Integer.parseInt(parts[0]);
                    int value = Integer.parseInt(parts[1]);
                    rules.computeIfAbsent(key, _ -> new HashSet<>()).add(value);
                }

                // An update
                else if (line.contains(",")) {
                    updates.add(new ArrayList<>(Arrays.stream(line.split(","))
                            .map(Integer::parseInt)
                            .toList()));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (List<Integer> update : updates) {
            if (isUpdateValid(update)) {
                sum += getMiddleValue(update);
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        for (List<Integer> update : updates) {
            if (!isUpdateValid(update)) {
                fixUpdate(update);
                sum += getMiddleValue(update);
            }
        }

        return String.valueOf(sum);
    }

    private boolean isUpdateValid(List<Integer> update) {
        // Check if the update is valid
        for (int i = 0; i < update.size(); i++) {
            if (!rules.containsKey(update.get(i))) continue;

            // Check if any of the preceding values are in the current value's rules
            Set<Integer> currentRules = rules.get(update.get(i));
            for (int j = 0; j < i; j++) {
                if (currentRules.contains(update.get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    // Reorder the update to follow the rules
    private void fixUpdate(List<Integer> update) {
        for (int i = 0; i < update.size(); i++) {
            if (!rules.containsKey(update.get(i))) continue;

            // Check if any of the preceding values are in the current value's rules
            Set<Integer> currentRules = rules.get(update.get(i));
            for (int j = 0; j < i; j++) {
                if (currentRules.contains(update.get(j))) {
                    // Swap the values
                    int temp = update.get(i);
                    update.set(i, update.get(j));
                    update.set(j, temp);

                    // Check if the update is still valid
                    if (!isUpdateValid(update)) fixUpdate(update);
                    return;
                }
            }
        }
    }

    // Get the middle value of the update
    private int getMiddleValue(List<Integer> update) {
        int middle = update.size() / 2;
        return update.get(middle);
    }

    private void printRulesAndUpdates() {
        // Print the rules
        for (Integer key : rules.keySet()) {
            System.out.print(key + " -> ");
            for (Integer value : rules.get(key)) System.out.print(" " + value);
            System.out.println();
        }

        System.out.println();

        // Print the updates
        for (List<Integer> update : updates) {
            for (Integer value : update) System.out.print(value + ",");
            System.out.println();
        }
    }
}