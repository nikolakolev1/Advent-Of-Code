package Days.Day9;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day9 implements Day {
    private final List<Integer> files = new ArrayList<>();
    private final List<Integer> freeSpace = new ArrayList<>();

    // Part 1 and Part 2 are solved in a totally different way.
    // They are both correct, but the second solution is more beautiful imo because it uses a disk map.
    public static void main(String[] args) {
        Day day9 = new Day9();
        day9.loadData("data/day9/input.txt");
        System.out.println(day9.part1());
        System.out.println(day9.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                int[] split = Arrays.stream(scanner.nextLine().split("")).mapToInt(Integer::parseInt).toArray();

                for (int i = 0; i < split.length; i++) {
                    if (i % 2 == 0) {
                        files.add(split[i]);
                    } else {
                        freeSpace.add(split[i]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        long sum = 0;

        int index = 0;
        int blockId = 0, blockId2 = files.size() - 1;
        int remainingBlocksFromEnd = files.get(blockId2);

        outerLoop:
        for (int i = 0; i < files.size(); i++) {
            //  Parse the file blocks at the current index
            int consecutiveBlocks = files.get(i);
            if (blockId == blockId2) consecutiveBlocks = remainingBlocksFromEnd;

            for (int j = 0; j < consecutiveBlocks; j++, index++) {
                sum += (long) index * blockId;
            }

            if (blockId == blockId2) break;
            blockId++;
            // ------------------------------------------------ //

            //  Parse the free space blocks at the current index (move file blocks from the free space)
            int consecutiveFreeSpace = freeSpace.get(i);
            for (int j = 0; j < consecutiveFreeSpace; ) {
                while (remainingBlocksFromEnd > 0 && j < consecutiveFreeSpace) {
                    sum += (long) index * blockId2;

                    index++;
                    j++;
                    remainingBlocksFromEnd--;
                }

                if (remainingBlocksFromEnd == 0) {
                    blockId2--;
                    remainingBlocksFromEnd = files.get(blockId2);

                    if (blockId2 < blockId) break outerLoop;
                }
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        int[] diskMap = buildDiskMap();
        int index = diskMap.length;

        for (int i = 0; i < files.size(); i++) {
            int[] chunk = getNextChunk(diskMap, index);
            int consecutiveBlocks = chunk[1];
            index = chunk[2];

            int freeSpaceIndex = findFreeSpace(diskMap, consecutiveBlocks);

            if (freeSpaceIndex != -1 && freeSpaceIndex < index) {
                move(diskMap, consecutiveBlocks, index, freeSpaceIndex);
            }
        }

        return String.valueOf(calculateSum(diskMap));
    }

    private int[] buildDiskMap() {
        int sumFiles = files.stream().mapToInt(Integer::intValue).sum();
        int sumFreeSpace = freeSpace.stream().mapToInt(Integer::intValue).sum();

        int[] diskMap = new int[sumFiles + sumFreeSpace];

        int passedBlocks = 0;
        int blockId = 0;

        for (int i = 0; i < files.size(); i++) {
            int consecutiveBlocks = files.get(i);
            for (int j = 0; j < consecutiveBlocks; j++) {
                diskMap[j + passedBlocks] = blockId;
            }
            blockId++;

            if (i == freeSpace.size()) break;

            int consecutiveFreeSpace = freeSpace.get(i);
            for (int j = 0; j < consecutiveFreeSpace; j++) {
                diskMap[consecutiveBlocks + j + passedBlocks] = -1;
            }

            passedBlocks += consecutiveBlocks + consecutiveFreeSpace;
        }

        return diskMap;
    }

    /**
     * Retrieves the next chunk of consecutive blocks from the disk map starting from the given index.
     * The chunk is represented as an array where:
     * - chunk[0] is the block ID
     * - chunk[1] is the number of consecutive blocks
     * - chunk[2] is the updated index to continue the search from
     * <p>
     * Important: The search is performed in reverse order.
     *
     * @param diskMap the array representing the disk map
     * @param index   the starting index to search from
     * @return an array representing the next chunk of consecutive blocks
     */
    private int[] getNextChunk(int[] diskMap, int index) {
        int[] chunk = new int[3];
        int blockId = -1;
        int consecutiveBlocks = 0;

        for (int i = index - 1; i >= 0; i--) {
            if (diskMap[i] == -1 && blockId != -1) {
                index = i;
                break;
            } else if (diskMap[i] != -1) {
                if ((blockId == -1) || (blockId == diskMap[i])) {
                    blockId = diskMap[i];
                    consecutiveBlocks++;
                } else {
                    index = i;
                    break;
                }
            }
        }

        chunk[0] = blockId;
        chunk[1] = consecutiveBlocks;
        chunk[2] = index + 1;

        return chunk;
    }

    /**
     * Finds the starting index of the first block of free space in the disk map
     * that is at least the specified size.
     *
     * @param diskMap the array representing the disk map
     * @param size    the required size of the free space block
     * @return the starting index of the free space block, or -1 if no suitable block is found
     */
    private int findFreeSpace(int[] diskMap, int size) {
        int consecutiveFreeSpace = 0;
        int freeSpaceIndex = -1;

        for (int i = 0; i < diskMap.length; i++) {
            if (diskMap[i] == -1) {
                if (consecutiveFreeSpace == 0) freeSpaceIndex = i;
                consecutiveFreeSpace++;
                if (consecutiveFreeSpace == size) return freeSpaceIndex;
            } else {
                consecutiveFreeSpace = 0;
            }
        }

        return -1;
    }

    /**
     * Moves a block of data from one position to another in the disk map.
     *
     * @param diskMap           the array representing the disk map
     * @param consecutiveBlocks the number of consecutive blocks to move
     * @param from              the starting index of the block to move
     * @param to                the target index to move the block to
     */
    private void move(int[] diskMap, int consecutiveBlocks, int from, int to) {
        for (int i = 0; i < consecutiveBlocks; i++) {
            diskMap[to + i] = diskMap[from + i];
            diskMap[from + i] = -1;
        }
    }

    private long calculateSum(int[] diskMap) {
        long sum = 0;
        for (int i = 0; i < diskMap.length; i++) {
            if (diskMap[i] != -1) sum += (long) i * diskMap[i];
        }
        return sum;
    }

    private void printDiskMap(int[] diskMap) {
        for (int block : diskMap) {
            if (block == -1) System.out.print(".");
            else System.out.print(block);
        }
        System.out.println();
    }
}