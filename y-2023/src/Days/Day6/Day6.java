package Days.Day6;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day6 implements Day {
    private record Race(long time, long distance) {}
    private ArrayList<Race> races = new ArrayList<>();
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

            long rP2time = 0;
            long rP2distance = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isBlank()) continue;

                String[] split = line.split(":");
                if (split[0].equals("Time")) {
                    String raceP2time = split[1].trim().replace(" ", "");
                    rP2time = Long.parseLong(raceP2time);

                    String[] timeSplit = split[1].split(" ");
                    for (String time : timeSplit) {
                        if (time.equals("")) continue;
                        times.add(Integer.parseInt(time.trim()));
                    }
                } else {
                    String raceP2distance = split[1].trim().replace(" ", "");
                    rP2distance = Long.parseLong(raceP2distance);

                    String[] distanceSplit = split[1].trim().split(" ");
                    for (String distance : distanceSplit) {
                        if (distance.equals("")) continue;
                        distances.add(Integer.parseInt(distance.trim()));
                    }
                }
            }

            raceP2 = new Race(rP2time, rP2distance);

            for (int i = 0; i < times.size(); i++) {
                races.add(new Race(times.get(i), distances.get(i)));
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public long part1() {
        ArrayList<Integer> better = new ArrayList<>();

        for (Race race : races) {
            better.add(evaluateRace(race));
        }

        int result = 1;
        for (int i = 0; i < better.size(); i++) {
            result *= better.get(i);
        }
        return result;
    }

    @Override
    public long part2() {
        long firstBetterIndex = evaluateRaceFirstBetterIndex(raceP2);
        long lastBetterIndex = evaluateRaceLastBetterIndex(raceP2);

        return lastBetterIndex - firstBetterIndex + 1;
    }

    private long distance(long t, long x) {
        return x * (t - x);
    }

    private int evaluateRace(Race race) {
        long totalTime = race.time;
        int better = 0;

        for (long i = 0; i < totalTime; i++) {
            long distanceTraveled = distance(totalTime, i);
            if (distanceTraveled > race.distance) {
                better++;
            }
        }

        return better;
    }

    private long evaluateRaceFirstBetterIndex(Race race) {
        long totalTime = race.time;

        for (int i = 0; i < totalTime; i++) {
            long distanceTraveled = distance(totalTime, i);
            if (distanceTraveled > race.distance) {
                return i;
            }
        }

        return -1;
    }

    private long evaluateRaceLastBetterIndex(Race race) {
        long totalTime = race.time;

        for (long i = totalTime; i > 0; i--) {
            long distanceTraveled = distance(totalTime, i);
            if (distanceTraveled > race.distance) {
                return i;
            }
        }

        return -1;
    }
}