package Days.Day24;

import General.Day;
import General.Helper;

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

    // Direction and speed
    private record Velocity(long x, long y, long z) {
        @Override
        public String toString() {
            return x + ", " + y + ", " + z;
        }
    }

    private static final long TEST_MIN_X = 200000000000000L, TEST_MAX_X = 400000000000000L, TEST_MIN_Y = 200000000000000L, TEST_MAX_Y = 400000000000000L;
    private final List<Hailstone> hailstones = new ArrayList<>();

    public static void main(String[] args) {
        Day day24 = new Day24();
//        day24.loadData(Helper.filename_test(24));
        day24.loadData(Helper.filename(24));
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
        boolean print = false;

        int count = 0;
        for (int i = 0; i < hailstones.size() - 1; i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                if (print) System.out.print(i + " & " + j + ") ");

                if (crossInsideTestArea_XY(hailstones.get(i), hailstones.get(j))) {
                    if (print) System.out.println("Cross");
                    count++;
                } else {
                    if (print) System.out.println("No cross");
                }
            }
        }

        if (print) System.out.println("Count: " + count);
        return String.valueOf(count);
    }

    @Override
    public String part2() {
        return "to be implemented";
    }

    private boolean crossInsideTestArea_XY(Hailstone h1, Hailstone h2) {
        // Calculate the slopes of the hailstones
        double slope1 = (double) h1.velocity().y / h1.velocity().x;
        double slope2 = (double) h2.velocity().y / h2.velocity().x;

        // If the slopes are equal, the hailstones are parallel => no cross
        if (slope1 == slope2) {
            return false;
        }

        /*
         * The y-intercepts are the points where the hailstones' paths intersect the y-axis. In other words,
         * they are the y-coordinates of the points where the paths cross the y-axis (where x = 0).
         */
        // Calculate the y-intercepts of the hailstones
        double intercept1 = h1.position().y - slope1 * h1.position().x;
        double intercept2 = h2.position().y - slope2 * h2.position().x;

        // Calculate the x-coordinate of the intersection point
        double xIntersection = (intercept2 - intercept1) / (slope1 - slope2);

        // Calculate the y-coordinate of the intersection point using the first hailstone's equation
        double yIntersection = slope1 * xIntersection + intercept1;

        // Check if the intersection point is within the test area boundaries
        if (xIntersection >= TEST_MIN_X && xIntersection <= TEST_MAX_X && yIntersection >= TEST_MIN_Y && yIntersection <= TEST_MAX_Y) {
            // Ensure the intersection point is in the future for both hailstones
            boolean isFutureForH1 = (xIntersection - h1.position().x) / h1.velocity().x >= 0;
            boolean isFutureForH2 = (xIntersection - h2.position().x) / h2.velocity().x >= 0;

            return isFutureForH1 && isFutureForH2; // The hailstones cross within the test area in the future
        } else {
            return false; // The hailstones do not cross within the test area
        }
    }
}