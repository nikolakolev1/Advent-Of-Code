package Days.Day1;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1 implements Day {
    private final int startX = 50;
    private final int maxX = 99;
    private final int minX = 0;

    private final List<Rotation> rotations = new ArrayList<>();

    public static void main(String[] args) {
        Day day1 = new Day1();
        day1.loadData("data/day1/input.txt");
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

                rotations.add(new Rotation(direction, distance));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        // Initialize starting position
        int currX = startX;
        int sum = 0;

        for (Rotation rotation : rotations) {
            // Extract rotation info
            String direction = rotation.direction;
            int distance = rotation.distance;

            // Update current position based on rotation
            if (direction.equals("R")) {
                currX += distance;
            } else if (direction.equals("L")) {
                currX -= distance;
            }

            // Wrap around the dial
            currX %= 100;
            if (currX < 0) currX = 100 + currX;

            // Check if we landed on 0
            if (currX == 0) sum++;
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        // Initialize starting position
        int currX = startX;
        int sum = 0;
        boolean lastWas0 = false;

        for (Rotation rotation : rotations) {
            // Extract rotation info
            String direction = rotation.direction;
            int distance = rotation.distance;

            // Update current position based on rotation step by step to count passing 0
            if (direction.equals("R")) {
                for (int i = 0; i < distance; i++) {
                    currX++;
                    if (currX > maxX) currX = minX;
                    if (currX == 0) sum++;
                }
            } else if (direction.equals("L")) {
                for (int i = 0; i < distance; i++) {
                    currX--;
                    if (currX < minX) currX = maxX;
                    if (currX == 0) sum++;
                }
            }

//            // Check for passing 0
//            int passedZero = 0;
//            if (currX > maxX) {
//                int over = currX - maxX;
//                passedZero = (over / 100) + 1;
//                if (currX % 100 == 0) passedZero--;
//
//                System.out.print("During this rotation the dial points at 0: " + passedZero + " ");
//            } else if (currX < minX) {
//                int under = -currX;
//                passedZero = (under / 100) + 1;
//                if (lastWas0) passedZero--;
//                if (currX % 100 == 0) passedZero--;
//
//                System.out.print("During this rotation the dial points at 0: " + passedZero + " ");
//            }
//            sum += passedZero;
//
//            // Wrap around the dial
//            currX %= 100;
//            if (currX < 0) currX = 100 + currX;
//
//            // Check if we landed on 0 and update lastWas0
//            if (currX == 0) {
//                sum++;
//                lastWas0 = true;
//            } else {
//                lastWas0 = false;
//            }
//
//            // debug
//            System.out.println("The dial is rotated " + direction + distance + " to point at " + currX);
        }

        return String.valueOf(sum);
    }

    private record Rotation(String direction, int distance) {

    }
}