package Days.Day5.GA_P2;

import General.Helper;

import java.io.File;
import java.util.*;

public class AocDay5 {
    private static final ArrayList<Long> seedIDs = new ArrayList<>();

    public record seedRange(long seedRangeStart, long seedRangeLength) {
    }

    public static final ArrayList<seedRange> seedRanges = new ArrayList<>();
    private static final int DEST_RANGE_START = 0, SRC_RANGE_START = 1, RANGE_LENGTH = 2;
    private static final int HARDCODED_ALL_MAPS = 7;
    public static final ArrayList<Map> maps = new ArrayList<>();
    public static final String filename = Helper.filename(5);
//    public static final String filename = "files/aocDay5Files/test_input.txt";

    public static void main(String[] args) {
        loadData(filename);
        System.out.println(part1());
        System.out.println(part2());
    }

    public static void loadData(String filename) {
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

            populateSeedRanges();

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void populateSeedRanges() {
        for (int i = 0; i < seedIDs.size(); i++) {
            long seedRangeStart = seedIDs.get(i);
            long seedRangeLength = seedIDs.get(++i);

            seedRanges.add(new seedRange(seedRangeStart, seedRangeLength));
        }
    }

    public static long part1() {
        long min = Long.MAX_VALUE;

        for (long seedId : seedIDs) {
            min = Math.min(min, mapSeedToLocation(seedId));
        }

        return min;
    }

    public static long part2() {
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

        long min = Long.MAX_VALUE;

        for (int i = 0; i < seedIDs.size(); i++) {
            long seedRangeStart = seedIDs.get(i);
            long seedRangeLength = seedIDs.get(++i);
            for (long seed = seedRangeStart; seed < seedRangeStart + seedRangeLength; seed++) {
                min = Math.min(min, mapSeedToLocation(seed));
            }
        }

        return min;

//        return -1;
    }

    public static long mapSeedToLocation(long seedID) {
        long currentValue = seedID;

        // apply all mappings
        for (Map map : maps) {
            currentValue = map.mapValue(currentValue);
        }

        return currentValue;
    }

    public static long convertIntArrToLong(int[] arr) {
        long result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[arr.length - 1 - i] * Math.pow(10, i);
        }
        return result;
    }

    // very inefficient, but I don't have time to fix it
    public static int[] convertLongToIntArr(long l, int length) {
        String s = String.valueOf(l);
        int[] result = new int[length];
        for (int i = 0; i < s.length(); i++) {
            result[length - 1 - i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return result;
    }

    public static boolean checkRangesContain(long l) {
        for (seedRange range : seedRanges) {
            if (range.seedRangeStart() <= l && l <= range.seedRangeStart() + range.seedRangeLength()) {
                return true;
            }
        }
        return false;
    }

    static class Map {
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

    static class Range {
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

    static class IntervalLong {
        public final long left;
        public final long right;

        public IntervalLong(long x, long y) {
            this.left = Math.min(x, y);
            this.right = Math.max(x, y);
        }

        public boolean contains(long a) {
            return left <= a && a <= right;
        }

        public boolean contains(IntervalLong a) {
            return left <= a.left && a.right <= right;
        }

        public boolean overlaps(IntervalLong a) {
            return overlapsLower(a) || overlapsUpper(a);
        }

        public boolean overlapsLower(IntervalLong a) {
            return a.left <= left && left <= a.right && a.right <= right;
        }

        public boolean overlapsUpper(IntervalLong a) {
            return left <= a.left && a.left <= right && right <= a.right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntervalLong interval = (IntervalLong) o;
            return left == interval.left && right == interval.right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }

        @Override
        public String toString() {
            return "Interval{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}