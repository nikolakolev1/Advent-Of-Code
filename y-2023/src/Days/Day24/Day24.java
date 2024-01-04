package Days.Day24;

import General.Day;
import General.Helper;
import Utils.RangeD;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day24 implements Day {
    private record Hailstone(Position position, Velocity velocity) {
        @Override
        public String toString() {
            return position + " @ " + velocity;
        }

        public double getYWhenX(long x) {
            double a = (x - position.x) / (double) velocity.x;
            return a * velocity.y + position.y;
        }
    }

    private record Position(long x, long y, long z) {
        @Override
        public String toString() {
            return x + ", " + y + ", " + z;
        }
    }

    private record Velocity(long x, long y, long z) {
        @Override
        public String toString() {
            return x + ", " + y + ", " + z;
        }
    } // Direction and speed

    private static final long TEST_MIN_X = 7, TEST_MAX_X = 27, TEST_MIN_Y = 7, TEST_MAX_Y = 27;
    private final List<Hailstone> hailstones = new ArrayList<>();

    public static void main(String[] args) {
        Day day24 = new Day24();
        day24.loadData(Helper.filename_test(24));
        System.out.println(day24.part1());
        System.out.println(day24.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                hailstones.add(parseHailstone(line));
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private Hailstone parseHailstone(String line) {
        String[] split = line.split(" @ ");

        Position position = parsePosition(split[0].trim());
        Velocity velocity = parseVelocity(split[1].trim());

        return new Hailstone(position, velocity);
    }

    private Position parsePosition(String line) {
        String[] split = line.split(",");

        long x = Long.parseLong(split[0].trim());
        long y = Long.parseLong(split[1].trim());
        long z = Long.parseLong(split[2].trim());

        return new Position(x, y, z);
    }

    private Velocity parseVelocity(String line) {
        String[] split = line.split(",");

        long x = Long.parseLong(split[0].trim());
        long y = Long.parseLong(split[1].trim());
        long z = Long.parseLong(split[2].trim());

        return new Velocity(x, y, z);
    }

    @Override
    public String part1() {
        printHailstones();
        System.out.println();

//        System.out.println(crossInsideTestArea_XY(hailstones.get(0), hailstones.get(3)));

        int count = 0;
        for (int i = 0; i < hailstones.size() - 1; i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                System.out.println(crossInsideTestArea_XY(hailstones.get(i), hailstones.get(j)));
//                if (crossInsideTestArea_XY(hailstones.get(i), hailstones.get(j))) {
//                    count++;
//                }
            }
        }

//        return String.valueOf(count);
        return "to be implemented";
    }

    @Override
    public String part2() {
        return "to be implemented";
    }

    private boolean crossInsideTestArea_XY(Hailstone h1, Hailstone h2) {
        double yWhenMinX_h1 = h1.getYWhenX(TEST_MIN_X);
        double yWhenMaxX_h1 = h1.getYWhenX(TEST_MAX_X);
        double yWhenMinX_h2 = h2.getYWhenX(TEST_MIN_X);
        double yWhenMaxX_h2 = h2.getYWhenX(TEST_MAX_X);

        System.out.println(yWhenMinX_h1 + " " + yWhenMaxX_h1 + " " + yWhenMinX_h2 + " " + yWhenMaxX_h2);

        // Parallel
        if (yWhenMinX_h1 - yWhenMaxX_h1 == yWhenMinX_h2 - yWhenMaxX_h2) {
            return false;
        }

//        RangeD range_h1 = new RangeD(yWhenMinX_h1, yWhenMaxX_h1);
//        RangeD range_h2 = new RangeD(yWhenMinX_h2, yWhenMaxX_h2);

//        return range_h1.overlaps(range_h2);

        if (Math.max(yWhenMinX_h1, yWhenMaxX_h1) < Math.min(yWhenMinX_h2, yWhenMaxX_h2)) {
            return false;
        } else if (Math.max(yWhenMinX_h2, yWhenMaxX_h2) < Math.min(yWhenMinX_h1, yWhenMaxX_h1)) {
            return false;
        }

        if (yWhenMinX_h1 < yWhenMaxX_h1) {
            return yWhenMinX_h2 < yWhenMaxX_h1;
        } else {
            return yWhenMinX_h1 < yWhenMaxX_h2;
        }
    }

    private void printHailstones() {
        for (Hailstone hailstone : hailstones) {
            System.out.println(hailstone);
        }
    }
}