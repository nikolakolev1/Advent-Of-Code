package Days.Day7;

import General.Day;

import java.io.File;
import java.util.*;

public class Day7 implements Day {
    private record Equation(long testValue, int[] numbers) {
    }

    private final List<Equation> equations = new ArrayList<>();

    public static void main(String[] args) {
        Day day7 = new Day7();
        day7.loadData("data/day7/input.txt");
        System.out.println(day7.part1());
        System.out.println(day7.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(":");

                long testValue = Long.parseLong(split[0].trim());
                String[] numbers = split[1].trim().split(" ");

                equations.add(new Equation(testValue, Arrays.stream(numbers).mapToInt(Integer::parseInt).toArray()));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        return String.valueOf(equations.stream()
                .mapToLong(e -> bruteForceEquation(e, false))
                .sum());
    }

    @Override
    public String part2() {
        return String.valueOf(equations.stream()
                .mapToLong(e -> bruteForceEquation(e, true))
                .sum());
    }

    // Tries all possible permutations of the numbers in the equation (+ or * in between each number)
    // If the result of the equation is equal to the testValue, return the result. Otherwise, return 0.
    // Part 2 has an additional operator: The concatenation operator (||) combines the digits from its left and right inputs into a single number.
    private long bruteForceEquation(Equation equation, boolean part2) {
        long testValue = equation.testValue();
        int[] numbers = equation.numbers();

        Queue<Long> results = new LinkedList<>();
        results.add((long) numbers[0]);

        for (int i = 1; i <= numbers.length; i++) {
            int iterations = results.size();

            for (int j = 0; j < iterations; j++) {
                long currentValue = results.poll();

                if (i != numbers.length) {
                    long product = currentValue * numbers[i];
                    if (product <= testValue) results.add(product);

                    long sum = currentValue + numbers[i];
                    if (sum <= testValue) results.add(sum);

                    long concat = Long.parseLong(currentValue + "" + numbers[i]);
                    if (part2 && concat <= testValue) results.add(concat);
                } else if (currentValue == testValue) {
                    return currentValue;
                }
            }
        }

        return 0;
    }

    private void printEquations() {
        for (Equation equation : equations) {
            System.out.println(equation.testValue + ": " + Arrays.toString(equation.numbers));
        }
    }
}