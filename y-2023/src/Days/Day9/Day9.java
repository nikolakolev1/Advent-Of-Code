package Days.Day9;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day9 implements Day {
    private final List<List<Integer>> history = new ArrayList<>();

    public static void main(String[] args) {
        Day day9 = new Day9();
        day9.loadData(Helper.filename(9));
        System.out.println(day9.part1());
        System.out.println(day9.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                List<Integer> sequence = new ArrayList<>();
                for (String num : line.split(" ")) {
                    sequence.add(Integer.parseInt(num));
                }

                history.add(sequence);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        List<Integer> numbers = new ArrayList<>();

        for (List<Integer> sequence : history) {
            numbers.add(getNextNumber_InitialSequence(sequence));
        }

        long sum = numbers.stream().mapToLong(Integer::longValue).sum();
        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        List<Integer> numbers = new ArrayList<>();

        for (List<Integer> sequence : history) {
            numbers.add(getPrevNumber_InitialSequence(sequence));
        }

        long sum = numbers.stream().mapToLong(Integer::longValue).sum();
        return String.valueOf(sum);
    }

    // ------------------- HELPER METHODS ------------------- //

    private List<Integer> getNextSequence(List<Integer> sequence) {
        if (sequence.size() == 1) throw new IllegalArgumentException("Sequence must have at least 2 elements");

        List<Integer> nextSequence = new ArrayList<>();

        for (int i = 1, num1 = sequence.get(0), size = sequence.size(); i < size; i++) {
            int num2 = sequence.get(i);
            nextSequence.add(num2 - num1);
            num1 = num2;
        }

        return nextSequence;
    }

    private boolean notAllZeroes(List<Integer> sequence) {
        for (int num : sequence) {
            if (num != 0) {
                return true;
            }
        }

        return false;
    }

    // ------------------- PART 1 ------------------- //

    private int getNextNumber_InitialSequence(List<Integer> sequence) {
        List<List<Integer>> children = new ArrayList<>();
        children.add(sequence);

        do {
            List<Integer> nextSequence = getNextSequence(children.get(children.size() - 1));
            children.add(nextSequence);
        } while (notAllZeroes(children.get(children.size() - 1)));

        children.get(children.size() - 1).add(0);

        for (int i = children.size() - 2; i >= 0; i--) {
            List<Integer> currentSequence = children.get(i);
            int nextNumber = getNextNumber(currentSequence, children.get(i + 1).get(currentSequence.size() - 1));
            currentSequence.add(nextNumber);
        }

        return children.get(0).get(children.get(0).size() - 1);
    }

    private int getNextNumber(List<Integer> sequence, int difference) {
        return sequence.get(sequence.size() - 1) + difference;
    }

    // ------------------- PART 2 ------------------- //

    private int getPrevNumber_InitialSequence(List<Integer> sequence) {
        List<List<Integer>> children = new ArrayList<>();
        children.add(sequence);

        do {
            List<Integer> nextSequence = getNextSequence(children.get(children.size() - 1));
            children.add(nextSequence);
        } while (notAllZeroes(children.get(children.size() - 1)));

        int prevNumber = 0;

        for (int i = children.size() - 2; i >= 0; i--) {
            prevNumber = getPrevNumber(children.get(i), prevNumber);
        }

        return prevNumber;
    }

    private int getPrevNumber(List<Integer> sequence, int difference) {
        return sequence.get(0) - difference;
    }
}