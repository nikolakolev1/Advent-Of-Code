package Days.Day1;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1 implements Day {
    private int startX = 50;
    private int currX = startX;
    private int maxX = 99;
    private int minX = 0;

    private List<Rotaton> rotations = new ArrayList<>();

    public static void main(String[] args) {
        Day day1= new Day1();
        day1.loadData("data/day1/input_test.txt");
        System.out.println(day1.part1());
        System.out.println(day1.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String direction = line.substring(0, 1);
                int distance = Integer.parseInt(line.substring(1));

                rotations.add(new Rotaton(direction, distance));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (Rotaton rotation : rotations) {
            String direction = rotation.direction;
            int distance = rotation.distance;

            if (direction.equals("R")) {
                currX += distance;
            } else if (direction.equals("L")) {
                currX -= distance;
            }

            currX %= 100;
            if (currX < 0) currX = 100 + currX;

            if (currX == 0) sum++;

            // debug
//            System.out.println("The dial is rotated " + direction + distance + " to point at " + currX);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        currX = 50;

        int sum = 0;

        boolean lastWas0 = false;

        for (Rotaton rotation : rotations) {
            String direction = rotation.direction;
            int distance = rotation.distance;

            if (direction.equals("R")) {
                currX += distance;
            } else if (direction.equals("L")) {
                currX -= distance;
            }

            if (currX > maxX + 1) {
                int over = currX - maxX;
                System.out.print("During this rotation the dial points at 0: " + ((over / maxX) + 1) + " ");
                sum += (over / maxX) + 1;

                if (lastWas0) sum--;
            } else if (currX < minX) {
                int under = -currX;
                System.out.print("During this rotation the dial points at 0: " + ((under / maxX) + 1) + " ");
                sum += (under / maxX) + 1;

                if (lastWas0) sum--;
            }

            currX %= 100;
            if (currX < 0) currX = 100 + currX;

            if (currX == 0) {
                sum++;
                lastWas0 = true;
            } else {
                lastWas0 = false;
            }

            // debug
            System.out.println("The dial is rotated " + direction + distance + " to point at " + currX);
        }

        return String.valueOf(sum);
    }

    private record Rotaton(String direction, int distance) {

    }
}