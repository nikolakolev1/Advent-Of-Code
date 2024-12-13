package Days.Day13;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 implements Day {
    private record Equation(long ax, long ay, long bx, long by, long targetX, long targetY) {
    }

    private List<Equation> equations = new ArrayList<>();

    public static void main(String[] args) {
        Day day13 = new Day13();
        day13.loadData("data/day13/input.txt");
        System.out.println(day13.part1());
        System.out.println(day13.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] a = scanner.nextLine().split(" ");
                String[] b = scanner.nextLine().split(" ");
                String[] target = scanner.nextLine().split(" ");
                if (scanner.hasNextLine()) scanner.nextLine();

                equations.add(new Equation(
                        Integer.parseInt(a[2].substring(2, a[2].length() - 1)),
                        Integer.parseInt(a[3].substring(2)),
                        Integer.parseInt(b[2].substring(2, b[2].length() - 1)),
                        Integer.parseInt(b[3].substring(2)),
                        Long.parseLong(target[1].substring(2, target[1].length() - 1)),
                        Long.parseLong(target[2].substring(2))
                ));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        long sum = 0;

        // TODO: Implement part 1
//        printEquations();
        for (int i = 0; i < equations.size(); i++) {
            long increment = solveEquation(equations.get(i));
//            System.out.println("Increment: " + increment);
            sum += increment;
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        // TODO: Implement part 2

        return String.valueOf(sum);
    }

    // something somewhere is broken
    private long solveEquation(Equation eq) {
        // a * ax + b * bx = targetX
        // a * ay + b * by = targetY

        // Calculate the determinant of the coefficient matrix
        double det = eq.ax * eq.by - eq.ay * eq.bx;

        // If the determinant is zero, the system has no unique solution
        if (det == 0) return 0;

        // Calculate the values of a and b using Cramer's rule
        double a = Math.round((eq.targetX * eq.by - eq.bx * eq.targetY) / det);
        double b = Math.round((eq.targetX - a * eq.ax) / eq.bx);

        // Check if the calculated values of a and b satisfy the constraints
        boolean valid = a <= 100 && b <= 100 && a >= 0 && b >= 0;
        return valid ? Math.round(a * 3 + b) : 0;
    }

    private void printEquations() {
        for (Equation eq : equations) {
            printEquation(eq);
            System.out.println();
        }
    }

    private void printEquation(Equation eq) {
        System.out.println("Button A: X+" + eq.ax + ", Y+" + eq.ay);
        System.out.println("Button B: X+" + eq.bx + ", Y+" + eq.by);
        System.out.println("Prize: X=" + eq.targetX + ", Y=" + eq.targetY);
    }
}