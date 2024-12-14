package Days.Day14;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day14 implements Day {
    private static class Robot {
        public int x, y;
        public int dx, dy;

        public Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.dx = vx;
            this.dy = vy;
        }
    }

    private final List<Robot> robots = new ArrayList<>();
    private static int maxX, maxY;

    public static void main(String[] args) {
        Day day14 = new Day14();

        String filename = "data/day14/input.txt";
        day14.loadData(filename);

        if (filename.contains("test")) {
            maxX = 11;
            maxY = 7;
        } else {
            maxX = 101;
            maxY = 103;
        }

        System.out.println(day14.part1());
        System.out.println(day14.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(" ");

                int x = Integer.parseInt(split[0].substring(2, split[0].indexOf(',')));
                int y = Integer.parseInt(split[0].substring(split[0].indexOf(',') + 1));
                int vx = Integer.parseInt(split[1].substring(2, split[1].indexOf(',')));
                int vy = Integer.parseInt(split[1].substring(split[1].indexOf(',') + 1));

                robots.add(new Robot(x, y, vx, vy));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        moveRobots(100);

        return countRobots() + "";
    }

    @Override
    public String part2() {
        boolean prettyPrint = false;

        robots.clear();
        loadData("data/day14/input.txt");

        for (int i = 0; i < 10000; i++) {
            moveRobots(1);

            if (checkMap()) {
                if (prettyPrint) System.out.println(getMap(true));
                return (i + 1) + "";
            }
        }

        return String.valueOf(-1);
    }

    private void moveRobots(int seconds) {
        for (Robot r : robots) {
            moveRobot(r, seconds);
        }
    }

    private void moveRobot(Robot r, int seconds) {
        int newX_outOfBounds = r.x + r.dx * seconds;
        int newY_outOfBounds = r.y + r.dy * seconds;

        int newX = newX_outOfBounds % maxX;
        int newY = newY_outOfBounds % maxY;

        if (newX < 0) newX += maxX;
        if (newY < 0) newY += maxY;

        r.x = newX;
        r.y = newY;
    }

    private int countRobots() {
        int q1 = countRobots_quadrant(1);
        int q2 = countRobots_quadrant(2);
        int q3 = countRobots_quadrant(3);
        int q4 = countRobots_quadrant(4);

        return q1 * q2 * q3 * q4;
    }

    // The ones that lie on the separating line are not counted in any quadrant
    private int countRobots_quadrant(int quadrant) {
        int quadrantWidth = (maxX - 1) / 2;
        int quadrantHeight = (maxY - 1) / 2;

        return switch (quadrant) {
            case 1 -> robots.stream().filter(r -> r.x < quadrantWidth && r.y < quadrantHeight).toArray().length;
            case 2 -> robots.stream().filter(r -> r.x > quadrantWidth && r.y < quadrantHeight).toArray().length;
            case 3 -> robots.stream().filter(r -> r.x < quadrantWidth && r.y > quadrantHeight).toArray().length;
            case 4 -> robots.stream().filter(r -> r.x > quadrantWidth && r.y > quadrantHeight).toArray().length;
            default -> 0;
        };

    }

    private void printRobotsList() {
        for (Robot r : robots) {
            System.out.println("p=" + r.x + "," + r.y + ", v=" + r.dx + "," + r.dy);
        }
    }

    private void printMap(boolean visualizeNumberOfRobots) {
        int[][] map = new int[maxY][maxX];
        for (Robot r : robots) map[r.y][r.x]++;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (map[y][x] != 0) {
                    if (visualizeNumberOfRobots) System.out.print(map[y][x]);
                    else System.out.print("#");
                } else System.out.print(".");
            }
            System.out.println();
        }
    }

    private String getMap(boolean visualizeNumberOfRobots) {
        StringBuilder sb = new StringBuilder();

        int[][] map = new int[maxY][maxX];
        for (Robot r : robots) map[r.y][r.x]++;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (map[y][x] != 0) {
                    if (visualizeNumberOfRobots) sb.append(map[y][x]);
                    else sb.append("#");
                } else sb.append(".");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private boolean checkMap() {
        int[][] map = new int[maxY][maxX];
        for (Robot r : robots) map[r.y][r.x]++;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (map[y][x] > 1) return false;
            }
        }

        return true;
    }
}