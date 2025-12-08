package Days.Day6;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Day6 implements Day {
    private static ArrayList<ArrayList<String>> gridP1 = new ArrayList<>();
    private static ArrayList<String> gridP2 = new ArrayList<>();
    private static ArrayList<String> instructions = new ArrayList<>();

    public static void main(String[] args) {
        Day day6 = new Day6();
        day6.loadData("data/day6/input_test.txt");
        System.out.println(day6.part1());
        System.out.println(day6.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                ArrayList<String> row = new ArrayList<>();
                Collections.addAll(row, line.trim().replaceAll("\\s+", "#").split("#"));

                if (!row.getFirst().equals("*") && !row.getFirst().equals("+")) {
                    gridP1.add(row);
                    gridP2.add(line);
                } else {
                    instructions.addAll(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        long sum = 0;

        for (int colIndex = 0; colIndex < instructions.size(); colIndex++) {
            String instruction = instructions.get(colIndex);
            if (instruction.equals("+")) {
                long result = sumAllInColumn(colIndex);
                sum += result;
            } else if (instruction.equals("*")) {
                long result = multiplyAllInColumn(colIndex);
                sum += result;
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        long sum = 0;

        ArrayList<Integer> operationValues = new ArrayList<>();

        for (int charAt = gridP2.getFirst().length() - 1; charAt >= 0; charAt--) {
            ArrayList<String> number = new ArrayList<>();
            for (String row : gridP2) {
                number.add(String.valueOf(row.charAt(charAt)));
            }

            StringBuilder numStr = new StringBuilder();

            for (String digit : number) {
                if (!digit.equals(" ")) numStr.append(digit);
            }

            if (numStr.length() > 0) {
                operationValues.add(Integer.parseInt(numStr.toString()));
            }
        }

        for (int instructionIndex =  instructions.size() - 1; instructionIndex >= 0; instructionIndex--) {
            for (int i = 0; i < gridP1.getFirst().size(); i++) {
                String instruction = instructions.get(instructionIndex);
                if (instruction.equals("+")) {
                    System.out.println(operationValues.getFirst());
                    sum += operationValues.removeFirst();
                } else if (instruction.equals("*")) {
                    System.out.println(operationValues.getFirst());
                    sum *= operationValues.removeFirst();
                }
            }
        }

        return String.valueOf(sum);
    }

    private long sumAllInColumn(int colIndex) {
        long sum = 0;
        for (ArrayList<String> row : gridP1) {
            try {
                sum += Long.parseLong(row.get(colIndex));
            } catch (NumberFormatException e) {
                // Handle non-numeric values if necessary
            }
        }
        return sum;
    }

    private long multiplyAllInColumn(int colIndex) {
        long product = 1;
        for (ArrayList<String> row : gridP1) {
            try {
                product *= Long.parseLong(row.get(colIndex));
            } catch (NumberFormatException e) {
                // Handle non-numeric values if necessary
            }
        }
        return product;
    }

    private void normalizeGridP2() {
        int longestRowLength = 0;

        for (String row : gridP2) {
            longestRowLength = Math.max(longestRowLength, row.length());
        }

        for (int i = 0; i < gridP2.size(); i++) {
            String row = gridP2.get(i);
            if (row.length() < longestRowLength) {
                StringBuilder normalizedRow = new StringBuilder(row);
                while (normalizedRow.length() < longestRowLength) {
                    normalizedRow.insert(0, " ");
                }
                gridP2.set(i, normalizedRow.toString());
            }
        }
    }
}