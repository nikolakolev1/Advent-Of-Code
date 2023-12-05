package Days.Day5;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Day5 implements Day {
    private final ArrayList<Long> seedIDs = new ArrayList<>();
    private final ArrayList<ArrayList<Long>> seed_soilMap = new ArrayList<>(), soil_fertilizerMap = new ArrayList<>(), fertilizer_waterMap = new ArrayList<>(), water_lightMap = new ArrayList<>(), light_temperatureMap = new ArrayList<>(), temperature_humidityMap = new ArrayList<>(), humidity_locationMap = new ArrayList<>();
    private static final int DEST_RANGE_START = 0, SRC_RANGE_START = 1, RANGE_LENGTH = 2;
    private final ArrayList<ArrayList<Long>>[] maps = new ArrayList[]{seed_soilMap, soil_fertilizerMap, fertilizer_waterMap, water_lightMap, light_temperatureMap, temperature_humidityMap, humidity_locationMap};

    public static void main(String[] args) {
        Day5 day5 = new Day5();
        day5.loadData(Helper.filename(5));
        System.out.println(day5.part1());
        System.out.println(day5.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            int currentMap = -1;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (currentMap == -1) {
                    String[] seeds = line.split(":")[1].trim().split(" ");
                    Arrays.stream(seeds).forEach(s -> seedIDs.add(Long.parseLong(s)));

                    scanner.nextLine();
                    scanner.nextLine();
                    currentMap++;
                } else {
                    if (line.isBlank()) {
                        // sort the map by srcRangeStart
                        maps[currentMap].sort(Comparator.comparing(a -> a.get(SRC_RANGE_START)));

                        if (currentMap == maps.length - 1) {
                            break;
                        }

                        currentMap++;
                        scanner.nextLine();
                        continue;
                    }

                    String[] values = line.trim().split(" ");
                    ArrayList<Long> valuesAL = new ArrayList<>();
                    for (String val : values) {
                        valuesAL.add(Long.parseLong(val));
                    }

                    maps[currentMap].add(valuesAL);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public long part1() {
        long min = Long.MAX_VALUE;

        for (long seedId : seedIDs) {
            min = Math.min(min, convertSeedToLocation(seedId));
        }

        return min;
    }

    @Override
    public long part2() {
        // This code doesn't work for the real input (too slow)
        /*
         * long min = Long.MAX_VALUE;

         * for (int i = 0; i < seedIDs.size(); i++) {
         *    long seedRangeStart = seedIDs.get(i);
         *    long seedRangeLength = seedIDs.get(++i);
         *
         *    for (long seed = seedRangeStart; seed < seedRangeStart + seedRangeLength; seed++) {
         *        min = Math.min(min, convertSeedToLocation(seed));
         *    }
         * }

         * return min;
         */

        return -1;
    }

    private long convertSeedToLocation(long seedID) {
        long currentValue = seedID;

        // apply all mappings
        for (ArrayList<ArrayList<Long>> map : maps) {
            for (ArrayList<Long> values : map) {
                long srcRangeStart = values.get(SRC_RANGE_START);
                long srcRangeEnd = srcRangeStart + values.get(RANGE_LENGTH);

                if (currentValue < srcRangeStart) {
                    break;
                } else if (currentValue < srcRangeEnd) {
                    currentValue += values.get(DEST_RANGE_START) - srcRangeStart;
                    break;
                }
            }
        }

        return currentValue;
    }
}