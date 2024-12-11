package Days.Day11;

import General.Day;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day11 implements Day {
    private final Map<BigInteger, Long> stoneCount = new HashMap<>();
    private final Map<BigInteger, BigInteger> memory = new HashMap<>();
    private static final String filename = "data/day11/input.txt";
    private boolean part1 = false;

    public static void main(String[] args) {
        Day day11 = new Day11();
        day11.loadData(filename);
        System.out.println(day11.part1());
        System.out.println(day11.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                Arrays.stream(scanner.nextLine().split(" ")).map(BigInteger::new).forEach(stone -> stoneCount.put(stone, 1L));
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        part1 = true;

        // Every time you blink, the stones change.
        int blinks = 25;
        for (int i = 0; i < blinks; i++) {
            applyRules();
        }

        return stoneCount.values().stream().mapToLong(Long::longValue).sum() + "";
    }

    @Override
    public String part2() {
        resetForP2();

        // Every time you blink, the stones change.
        int blinks = 75;
        for (int i = 0; i < blinks; i++) {
            applyRules();
        }
        return stoneCount.values().stream().mapToLong(Long::longValue).sum() + "";
    }

    private void applyRules() {
        Map<BigInteger, Long> clonedMap = new HashMap<>(stoneCount);
        for (Map.Entry<BigInteger, Long> entry : clonedMap.entrySet()) {
            BigInteger stone = entry.getKey();

            if (stone.equals(BigInteger.ZERO)) {
                rule1(clonedMap);
            } else if (stone.toString().length() % 2 == 0) {
                rule2(clonedMap, stone);
            } else {
                rule3(clonedMap, stone);
            }
        }
    }

    /*
     * If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
     */
    private void rule1(Map<BigInteger, Long> clonedMap) {
        long increment = clonedMap.get(BigInteger.ZERO);

        stoneCount.put(BigInteger.ZERO, stoneCount.get(BigInteger.ZERO) - increment);
        stoneCount.put(BigInteger.ONE, stoneCount.getOrDefault(BigInteger.ONE, 0L) + increment);
    }

    /*
     * If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
     * The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved
     * on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
     */
    private void rule2(Map<BigInteger, Long> clonedMap, BigInteger stone) {
        int half = stone.toString().length() / 2;
        BigInteger left = new BigInteger(stone.toString().substring(0, half));
        BigInteger right = new BigInteger(stone.toString().substring(half));

        long increment = clonedMap.get(stone);

        stoneCount.put(left, stoneCount.getOrDefault(left, 0L) + increment);
        stoneCount.put(right, stoneCount.getOrDefault(right, 0L) + increment);
        stoneCount.put(stone, stoneCount.get(stone) - increment);
    }

    /*
     * If none of the other rules apply, the stone is replaced by a new stone;
     * the old stone's number multiplied by 2024 is engraved on the new stone.
     */
    private void rule3(Map<BigInteger, Long> clonedMap, BigInteger stone) {
        memory.putIfAbsent(stone, stone.multiply(new BigInteger("2024")));

        long increment = clonedMap.get(stone);

        stoneCount.put(memory.get(stone), stoneCount.getOrDefault(memory.get(stone), 0L) + increment);
        stoneCount.put(stone, stoneCount.get(stone) - increment);
    }

    // Reset the stone count if part 1 was run
    private void resetForP2() {
        if (part1) {
            stoneCount.clear();
            loadData(filename);
        }
    }
}