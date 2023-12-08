package Days.Day5;

import Days.Day5.Main.GA;
import General.Day;
import General.Helper;
import General.IntervalLong;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Day5 implements Day {
    private final ArrayList<Long> seedIDs = new ArrayList<>();
    private static final int DEST_RANGE_START = 0, SRC_RANGE_START = 1, RANGE_LENGTH = 2;
    private static final int HARDCODED_ALL_MAPS = 7;
    private final ArrayList<Map> maps = new ArrayList<>();

    public static void main(String[] args) {
        Day5 day5 = new Day5();
        day5.loadData(Helper.filename_test(5));
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
                        maps.get(currentMap).ranges.sort(Comparator.comparing(a -> a.source.left));

                        // stop if we are at the last map
                        if (currentMap == HARDCODED_ALL_MAPS - 1) {
                            break;
                        }

                        currentMap++;
                        scanner.nextLine();
                        continue;
                    }

                    String[] values = line.trim().split(" ");

                    if (maps.size() < currentMap + 1) {
                        maps.add(new Map(new Range(Long.parseLong(values[DEST_RANGE_START]), Long.parseLong(values[SRC_RANGE_START]), Long.parseLong(values[RANGE_LENGTH]))));
                    } else {
                        maps.get(currentMap).addRange(new Range(Long.parseLong(values[DEST_RANGE_START]), Long.parseLong(values[SRC_RANGE_START]), Long.parseLong(values[RANGE_LENGTH])));
                    }
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        long min = Long.MAX_VALUE;

        for (long seedId : seedIDs) {
            min = Math.min(min, mapSeedToLocation(seedId));
        }

        return String.valueOf(min);
    }

    @Override
    public String part2() {
        // This code doesn't work for the real input (too slow)
        /*
         * long min = Long.MAX_VALUE;

         * for (int i = 0; i < seedIDs.size(); i++) {
         *    long seedRangeStart = seedIDs.get(i);
         *    long seedRangeLength = seedIDs.get(++i);
         *
         *    for (long seed = seedRangeStart; seed < seedRangeStart + seedRangeLength; seed++) {
         *        min = Math.min(min, con+v*ertSeedToLocation(seed));
         *    }
         * }

         * return min;
         */

        return String.valueOf(GA.part2());
    }

    private long mapSeedToLocation(long seedID) {
        long currentValue = seedID;

        // apply all mappings
        for (Map map : maps) {
            currentValue = map.mapValue(currentValue);
        }

        return currentValue;
    }

    class Map {
        ArrayList<Range> ranges = new ArrayList<>();

        public Map() {
        }

        public Map(Range range) {
            ranges.add(range);
        }

        void addRange(Range range) {
            ranges.add(range);
        }

        long mapValue(long value) {
            for (Range range : ranges) {
                if (range.containsValue(value)) {
                    return range.convertValue(value);
                }
            }

            return value;
        }
    }

    class Range {
        IntervalLong destination;
        IntervalLong source;
        long length;

        public Range(long destinationRangeStart, long sourceRangeStart, long length) {
            destination = new IntervalLong(destinationRangeStart, destinationRangeStart + length - 1);
            source = new IntervalLong(sourceRangeStart, sourceRangeStart + length - 1);
            this.length = length;
        }

        boolean containsValue(long value) {
            return source.contains(value);
        }

        long convertValue(long value) {
            return value + destination.left - source.left;
        }
    }
}