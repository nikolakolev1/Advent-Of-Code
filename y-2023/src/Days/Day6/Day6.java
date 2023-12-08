package Days.Day6;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day6 implements Day {
    private record Race(int time, long distance) {
    }

    private final ArrayList<Race> races = new ArrayList<>();
    private Race raceP2;

    public static void main(String[] args) {
        Day day6 = new Day6();
        day6.loadData(Helper.filename(6));
        System.out.println(day6.part1());
        System.out.println(day6.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            ArrayList<Integer> times = new ArrayList<>();
            ArrayList<Integer> distances = new ArrayList<>();

            int P2time = 0;
            long P2distance = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isBlank()) continue;

                String[] split = line.split(":");

                // Time: ...
                if (split[0].equals("Time")) {
                    // merge the times into one
                    P2time = Integer.parseInt(split[1].trim().replace(" ", ""));

                    // store the times
                    for (String time : split[1].split(" ")) {
                        if (!time.isEmpty()) times.add(Integer.parseInt(time.trim()));
                    }
                }

                // Distance: ...
                else {
                    // merge the distances into one
                    P2distance = Long.parseLong(split[1].trim().replace(" ", ""));

                    // store the distances
                    for (String distance : split[1].trim().split(" ")) {
                        if (!distance.isEmpty()) distances.add(Integer.parseInt(distance.trim()));
                    }
                }
            }

            // Save the race details for part 2
            raceP2 = new Race(P2time, P2distance);

            // Save the races details for part 1
            for (int i = 0; i < times.size(); i++) {
                races.add(new Race(times.get(i), distances.get(i)));
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        ArrayList<Integer> better = new ArrayList<>();

        for (Race race : races) {
            better.add(countBetter(race));
        }

        return String.valueOf(better.stream()
                .reduce(1, (a, b) -> a * b));
    }

    @Override
    public String part2() {
        return String.valueOf(getLastBetterIndex(raceP2) - getFirstBetterIndex(raceP2) + 1);
    }

    private long distance(long t, long x) {
        return x * (t - x);
    }

    private int countBetter(Race race) {
        int better = 0;

        for (int i = 0; i < race.time; i++) {
            if (distance(race.time, i) > race.distance) {
                better++;
            }
        }

        return better;
    }

    private long getFirstBetterIndex(Race race) {
        for (int i = 0; i < race.time; i++) {
            if (distance(race.time, i) > race.distance) {
                return i;
            }
        }

        throw new RuntimeException("No better index found");
    }

    private long getLastBetterIndex(Race race) {
        for (long i = race.time; i > 0; i--) {
            if (distance(race.time, i) > race.distance) {
                return i;
            }
        }

        throw new RuntimeException("No better index found");
    }
}