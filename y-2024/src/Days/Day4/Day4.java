package Days.Day4;

import General.Day;

import java.io.File;
import java.util.*;

public class Day4 implements Day {
    private record Coordinates(int row, int col) {
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof Coordinates coordinates)) return false;
            return row == coordinates.row && col == coordinates.col;
        }
    }

    private final List<List<String>> input = new ArrayList<>();
    private final Set<Coordinates> counted = new HashSet<>();

    public static void main(String[] args) {
        Day day4 = new Day4();
        day4.loadData("data/day4/input.txt");
        System.out.println(day4.part1());
        System.out.println(day4.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                input.add(List.of(scanner.nextLine().split("")));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        return String.valueOf(countXMAS());
    }

    @Override
    public String part2() {
        return String.valueOf(countCrossedMAS());
    }

    private int countXMAS() {
        int count = 0;

        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).size(); col++) {
                if (input.get(row).get(col).equals("X")) {
                    if (upLeftDiagonal(row, col, false)) count++;
                    if (up(row, col)) count++;
                    if (upRightDiagonal(row, col, false)) count++;
                    if (left(row, col)) count++;
                    if (right(row, col)) count++;
                    if (downLeftDiagonal(row, col, false)) count++;
                    if (down(row, col)) count++;
                    if (downRightDiagonal(row, col, false)) count++;
                }
            }
        }

        return count;
    }

    private int countCrossedMAS() {
        int count = 0;

        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).size(); col++) {
                if (upLeftDiagonal(row, col, true) && notCounted(row - 1, col - 1)) {
                    if (row - 2 >= 0 && downLeftDiagonal(row - 2, col, true)) {
                        count++;
                        counted.add(new Coordinates(row - 1, col - 1));
                    } else if (col - 2 >= 0 && upRightDiagonal(row, col - 2, true)) {
                        count++;
                        counted.add(new Coordinates(row - 1, col - 1));
                    }
                }

                if (upRightDiagonal(row, col, true) && notCounted(row - 1, col + 1)) {
                    if (row - 2 >= 0 && downRightDiagonal(row - 2, col, true)) {
                        count++;
                        counted.add(new Coordinates(row - 1, col + 1));
                    } else if (col + 2 < input.get(row).size() && upLeftDiagonal(row, col + 2, true)) {
                        count++;
                        counted.add(new Coordinates(row - 1, col + 1));
                    }
                }

                if (downLeftDiagonal(row, col, true) && notCounted(row + 1, col - 1)) {
                    if (row + 2 < input.size() && upLeftDiagonal(row + 2, col, true)) {
                        count++;
                        counted.add(new Coordinates(row + 1, col - 1));
                    } else if (col - 2 >= 0 && downRightDiagonal(row, col - 2, true)) {
                        count++;
                        counted.add(new Coordinates(row + 1, col - 1));
                    }
                }

                if (downRightDiagonal(row, col, true) && notCounted(row + 1, col + 1)) {
                    if (row + 2 < input.size() && upRightDiagonal(row + 2, col, true)) {
                        count++;
                        counted.add(new Coordinates(row + 1, col + 1));
                    } else if (col + 2 < input.get(row).size() && downLeftDiagonal(row, col + 2, true)) {
                        count++;
                        counted.add(new Coordinates(row + 1, col + 1));
                    }
                }
            }
        }

        return count;
    }

    private boolean notCounted(int row, int col) {
        return !counted.contains(new Coordinates(row, col));
    }

    private boolean upLeftDiagonal(int row, int col, boolean part2) {
        for (int i = 1; i < 4; i++) {
            if (!part2) {
                if (row - i < 0 || col - i < 0) {
                    return false;
                }
            } else {
                if (row - i + 1 < 0 || col - i + 1 < 0) {
                    return false;
                }
            }

            String character = part2 ? input.get(row - i + 1).get(col - i + 1) : input.get(row - i).get(col - i);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean up(int row, int col) {
        for (int i = 1; i < 4; i++) {
            if (row - i < 0) {
                return false;
            }

            String character = input.get(row - i).get(col);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean upRightDiagonal(int row, int col, boolean part2) {
        for (int i = 1; i < 4; i++) {
            if (!part2) {
                if (row - i < 0 || col + i >= input.get(row).size()) {
                    return false;
                }
            } else {
                if (row - i + 1 < 0 || col + i - 1 >= input.get(row).size()) {
                    return false;
                }
            }

            String character = part2 ? input.get(row - i + 1).get(col + i - 1) : input.get(row - i).get(col + i);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean left(int row, int col) {
        for (int i = 1; i < 4; i++) {
            if (col - i < 0) {
                return false;
            }

            String character = input.get(row).get(col - i);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean right(int row, int col) {
        for (int i = 1; i < 4; i++) {
            if (col + i >= input.get(row).size()) {
                return false;
            }

            String character = input.get(row).get(col + i);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean downLeftDiagonal(int row, int col, boolean part2) {
        for (int i = 1; i < 4; i++) {
            if (!part2) {
                if (row + i >= input.size() || col - i < 0) {
                    return false;
                }
            } else {
                if (row + i - 1 >= input.size() || col - i + 1 < 0) {
                    return false;
                }
            }

            String character = part2 ? input.get(row + i - 1).get(col - i + 1) : input.get(row + i).get(col - i);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean down(int row, int col) {
        for (int i = 1; i < 4; i++) {
            if (row + i >= input.size()) {
                return false;
            }

            String character = input.get(row + i).get(col);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }

    private boolean downRightDiagonal(int row, int col, boolean part2) {
        for (int i = 1; i < 4; i++) {
            if (!part2) {
                if (row + i >= input.size() || col + i >= input.get(row).size()) {
                    return false;
                }
            } else {
                if (row + i - 1 >= input.size() || col + i - 1 >= input.get(row).size()) {
                    return false;
                }
            }

            String character = part2 ? input.get(row + i - 1).get(col + i - 1) : input.get(row + i).get(col + i);

            switch (i) {
                case 1 -> {
                    if (!character.equals("M")) return false;
                }
                case 2 -> {
                    if (!character.equals("A")) return false;
                }
                case 3 -> {
                    if (!character.equals("S")) return false;
                }
            }
        }

        return true;
    }
}