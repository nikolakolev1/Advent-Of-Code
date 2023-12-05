package Days.Day5;

import General.Day;
import General.Helper;
import General.IntervalLong;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * WRONG IDEA. Ranges cannot be merged top down. They have to be merged bottom up.
 * If you merge them top-down, you will lose information.
 * In the end there are very few ranges left, which makes it impossible to map the seeds to the soil.
 */
public class Day5_Part2 implements Day {
    private final ArrayList<Long> seedIDs = new ArrayList<>();
    private static final int DEST_RANGE_START = 0, SRC_RANGE_START = 1, RANGE_LENGTH = 2;
    private static final int HARDCODED_ALL_MAPS = 7;
    private final ArrayList<Map> maps = new ArrayList<>();

    public static void main(String[] args) {
        Day5_Part2 day5 = new Day5_Part2();
        day5.loadData(Helper.filename_test(5));
        System.out.println(day5.part1());
//        System.out.println(day5.part2());
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
                        // sort the map ranges by srcRangeStart
                        maps.get(currentMap).ranges.sort(Comparator.comparing(a -> a.source.left));

                        // stop if we are at the last map
                        if (currentMap == HARDCODED_ALL_MAPS - 1) {
                            break;
                        }

                        // move to the next map
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
    public long part1() {
        Map seedToSoilMap = mergeAllMaps(maps);

        long min = Long.MAX_VALUE;

        for (long seedId : seedIDs) {
            min = Math.min(min, mapSeedToLocation(seedId, seedToSoilMap));
        }

        return min;
    }

    @Override
    public long part2() {
        Map seedToSoilMap = mergeAllMaps(maps);

        long min = Long.MAX_VALUE;

        for (int i = 0; i < seedIDs.size(); i++) {
            long seedRangeStart = seedIDs.get(i);
            long seedRangeLength = seedIDs.get(++i);
            IntervalLong seedRange = new IntervalLong(seedRangeStart, seedRangeStart + seedRangeLength);

            for (Range range : seedToSoilMap.ranges) {
                if (range.source.overlaps(seedRange)) {
                    seedRangeStart = range.convertValue(seedRangeStart);
                    seedRangeLength = range.length;
                    break;
                }
            }

            for (long seed = seedRangeStart; seed < seedRangeStart + seedRangeLength; seed++) {
                min = Math.min(min, mapSeedToLocation(seed, seedToSoilMap));
            }
        }

        return min;
    }

    private long mapSeedToLocation(long seedID, Map map) {
        return map.mapValue(seedID);
    }

    /**
     * For each map, merge the ranges of the map with the ranges of the next map.
     * In the end, we will have a map that maps has the source ranges of the first map and the
     * destination ranges of the last map.
     * <p>
     * overlap options:
     * - range2 contains range1
     * r1: .....XXX.....
     * r2: ...XXXXXXX...
     * <p>
     * - range1 overlaps lower part of range2
     * r1: ..XXXX.......
     * r2: ....XXXXX...
     * <p>
     * - range1 overlaps upper part of range2
     * r1: .......XXXXX.
     * r2:.....XXXXX....
     * <p>
     * range1 contains range2
     * r1: ..XXXXXX....
     * r2: ....XXX.....
     */
    private Map mergeMaps(Map map1, Map map2) {
        Map mergedMap = new Map();

        for (Range range1 : map1.ranges) {
            for (Range range2 : map2.ranges) {
                /*
                 * range 1: 50 10 5
                 * range 2: 20 40 60
                 *
                 * => merged: 30 10 5
                 */
                // NOTE: this is correct
                if (range2.source.contains(range1.destination)) {
                    long difference = range1.destination.left - range2.source.left;
                    Range mergedRange = new Range(range2.destination.left + difference, range1.source.left, range1.length);
                    mergedMap.addRange(mergedRange);

                    break;
                }

                /*
                 * range 1: 50 10 5
                 * range 2: 20 52 60
                 *
                 * => merged: 20 12 3
                 */
                // NOTE: this is correct
                else if (range2.source.overlapsLower(range1.destination)) {
                    long difference = range2.source.left - range1.destination.left;
                    Range mergedRange = new Range(range2.destination.left, range1.source.left + difference, range1.length - difference);
                    mergedMap.addRange(mergedRange);
                }

                /*
                 * range 1: 50 10 5
                 * range 2: 20 47 7
                 *
                 * => merged: 20 10 2
                 */
                // NOTE: this is correct
                else if (range2.source.overlapsUpper(range1.destination)) {
                    long difference = range1.destination.left - range2.source.left;
                    Range mergedRange = new Range(range2.destination.left, range1.source.left, range1.length - difference);
                    mergedMap.addRange(mergedRange);
                }

                /*
                 * range 1: 50 10 5
                 * range 2: 20 51 3
                 *
                 * => merged: 20 11 3
                 */
                // NOTE: this is correct
                else if (range1.destination.contains(range2.source)) {
                    long difference = range2.source.left - range1.destination.left;
                    Range mergedRange = new Range(range2.destination.left, range1.source.left + difference, range2.length);
                    mergedMap.addRange(mergedRange);
                }
            }
        }

        return mergedMap;
    }

    private Map mergeAllMaps(ArrayList<Map> allMaps) {
        Map mergedMap = new Map();

        Map map1 = allMaps.get(0);
        for (int i = 1; i < allMaps.size() - 1; i++) {
            Map map2 = allMaps.get(i);

            mergedMap = mergeMaps(map1, map2);
            map1 = mergedMap;
        }

        return mergedMap;

//        Map a = mergeMaps(allMaps.get(0), allMaps.get(1));
//        Map b = mergeMaps(a, allMaps.get(2));
//        Map c = mergeMaps(b, allMaps.get(3));
//        Map d = mergeMaps(c, allMaps.get(4));
//        Map e = mergeMaps(d, allMaps.get(5));
//        Map f = mergeMaps(e, allMaps.get(6));

//        return f;
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