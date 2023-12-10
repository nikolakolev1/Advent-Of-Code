package Days.Day10;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day10 implements Day {
    private final ArrayList<ArrayList<Tube>> tubes = new ArrayList<>();
    private final ArrayList<Tube> loop = new ArrayList<>();
    private int[] loopStart; // y, x

    public record Tube(char value, int x, int y) {
    }

    public static void main(String[] args) {
        Day day10 = new Day10();
        day10.loadData(Helper.filename(10));
        System.out.println(day10.part1());
        System.out.println(day10.part2());
    }

    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            int y = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split("");

                ArrayList<Tube> row = new ArrayList<>();

                for (int x = 0; x < split.length; x++) {
                    if (split[x].equals("S")) {
                        loopStart = new int[]{y, x};
                        split[x] = "F"; // hardcoded
                    }

                    row.add(new Tube(split[x].charAt(0), x, y));
                }

                tubes.add(row);

                y++;
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public String part1() {
        generateLoop();

        return String.valueOf(loop.size() / 2);
    }

    /*
     * Idea:
     * - create a Boolean map, true -> outside, false -> inside, null -> not visited
     * - Traverse the tube map\
     * - If your coordinates are not visited, according to the Boolean map
     *      - If you encounter a tube, mark it as false on the Boolean map
     *      - If you encounter a space ('.')
     *          - if it is connected to the end of the map (one of the sides), mark it as true on the Boolean map
     *          - and start a search from it to find all the connected spaces and mark them as true on the Boolean map
     *          - if it is not connected to the end of the map, do nothing
     * - In the end count all the nulls on the Boolean map (these are the spaces that are not connected to the end of the map)
     */
    public String part2() {
        return "to be implemented";
    }

    public void generateLoop() {
        Tube start = tubes.get(loopStart[0]).get(loopStart[1]);

        loop.add(start);

        Tube previous = start;
        Tube current = getAdjacent(start)[1];

        while (current != start) {
            loop.add(current);

            Tube[] adjacent = getAdjacent(current);

            if (adjacent[0] != previous) {
                previous = current;
                current = adjacent[0];
            } else {
                previous = current;
                current = adjacent[1];
            }
        }
    }

    public Tube[] getAdjacent(Tube tube) {
        Tube[] adjacent = new Tube[2];

        int x = tube.x;
        int y = tube.y;
        char value = tube.value;

        switch (value) {
            case '|' -> {
                adjacent[0] = tubes.get(y - 1).get(x);
                adjacent[1] = tubes.get(y + 1).get(x);
            }
            case '-' -> {
                adjacent[0] = tubes.get(y).get(x - 1);
                adjacent[1] = tubes.get(y).get(x + 1);
            }
            case 'L' -> {
                adjacent[0] = tubes.get(y - 1).get(x);
                adjacent[1] = tubes.get(y).get(x + 1);
            }
            case 'J' -> {
                adjacent[0] = tubes.get(y - 1).get(x);
                adjacent[1] = tubes.get(y).get(x - 1);
            }
            case '7' -> {
                adjacent[0] = tubes.get(y).get(x - 1);
                adjacent[1] = tubes.get(y + 1).get(x);
            }
            case 'F' -> {
                adjacent[0] = tubes.get(y).get(x + 1);
                adjacent[1] = tubes.get(y + 1).get(x);
            }
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }

        return adjacent;
    }
}