package Days.Day22;

import General.Day;
import General.Helper;
import Utils.Range;

import java.io.File;
import java.util.*;

/*
 * The sand has been falling as large compacted bricks of sand, piling up to form an impressive stack.
 * To make use of the sand, some of the bricks will need to be broken apart.
 */
public class Day22 implements Day {
    private List<List<List<Cuboid>>> grid;
    public List<Cuboid> bricks = new ArrayList<>();
    private final int minZ = 1;
    Set<Cuboid> essentialSupportingBricks = new HashSet<>();

    static class Coordinate {
        /*
         * x & y-axis (min = 0)
         * z - height (min = 1)
         */
        int x, y, z;

        public Coordinate(String[] coordinates) {
            this.x = Integer.parseInt(coordinates[0]);
            this.y = Integer.parseInt(coordinates[1]);
            this.z = Integer.parseInt(coordinates[2]);
        }
    }

    /*
     * The bricks
     * - Their position is given as two x,y,z coordinates - one for each end of the brick - separated by a tilde (~).
     * - A line like 2,2,2~2,2,2 means that both ends of the brick are at the same coordinate - that brick is a single cube.
     * - A line like 0,0,1~0,0,10 represents a ten-cube brick which is oriented vertically.
     */
    class Cuboid {
        Coordinate start, end;
        Range xRange, yRange, zRange;
        List<Cuboid> supportedBy = new ArrayList<>();
        List<Cuboid> supports = new ArrayList<>();

        public Cuboid(Coordinate start, Coordinate end) {
            this.start = start;
            this.end = end;

            this.xRange = new Range(start.x, end.x);
            this.yRange = new Range(start.y, end.y);
            this.zRange = new Range(start.z, end.z);
        }

        /*
         * Used to figure out where the brick will end up after it is dropped.
         */
        public void freeFall() {
            // If the brick is already on the ground, it does not move.
            if (Math.min(start.z, end.z) == minZ) {
                return;
            }

            // If the brick is resting on another brick, it does not move.
            for (int x = start.x; x <= end.x; x++) {
                for (int y = start.y; y <= end.y; y++) {
                    if (grid.get(x).get(y).get(Math.min(start.z, end.z) - 1) != null) {
                        return;
                    }
                }
            }

            // Otherwise, it falls by one position.
            start.z--;
            end.z--;

            // Recursion
            freeFall();
        }
    }

    public static void main(String[] args) {
        Day day22 = new Day22();
        day22.loadData(Helper.filename(22));
        System.out.println(day22.part1());
        System.out.println(day22.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String[] coordinates = scanner.nextLine().split("~");

                String[] start = coordinates[0].split(",");
                String[] end = coordinates[1].split(",");

                bricks.add(new Cuboid(new Coordinate(start), new Coordinate(end)));
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Part 1: How many non-essential supporting bricks there are?
    @Override
    public String part1() {
        intiGrid(); // Initialize the grid
        sortBricks(); // Sort the bricks by z-axis
        settleBricks(); // Settle the bricks
        assignSupported(); // Populate the supportedBy list for each brick

        for (Cuboid brick : bricks) {
            if (brick.supportedBy.size() == 1) {
                essentialSupportingBricks.add(brick.supportedBy.getFirst());
            }
        }

        return String.valueOf(bricks.size() - essentialSupportingBricks.size());
    }

    // Part 2: Chained reaction
    @Override
    public String part2() {
        int sum = 0;

        List<Cuboid> essentialSupportingBricks = new ArrayList<>(this.essentialSupportingBricks);

        for (int i = 0; i < essentialSupportingBricks.size(); i++) {
            Cuboid currentSupportingBrick = essentialSupportingBricks.get(essentialSupportingBricks.size() - 1 - i);

            Set<Cuboid> fallingBricks = new HashSet<>();
            fallingBricks.add(currentSupportingBrick);
            Queue<Cuboid> queue = new LinkedList<>(currentSupportingBrick.supports);

            while (!queue.isEmpty()) {
                Cuboid brick = queue.poll();

                boolean fall = true;
                for (Cuboid supportingBrick : brick.supportedBy) {
                    if (!fallingBricks.contains(supportingBrick)) {
                        fall = false;
                        break;
                    }
                }

                if (fall) {
                    fallingBricks.add(brick);

                    for (Cuboid supportingBrick : brick.supports) {
                        if (!queue.contains(supportingBrick)) {
                            queue.add(supportingBrick);
                        }
                    }
                }
            }

            sum += fallingBricks.size();
        }

        return String.valueOf(sum - essentialSupportingBricks.size());
    }

    /*
     * Initialize the List<List<List<Boolean>>> grid, considering the falling bricks coordinates.
     */
    private void intiGrid() {
        // Find max x, y, z
        int maxX = 0, maxY = 0, maxZ = 0;

        for (Cuboid brick : bricks) {
            maxX = Math.max(maxX, Math.max(brick.start.x, brick.end.x));
            maxY = Math.max(maxY, Math.max(brick.start.y, brick.end.y));
            maxZ = Math.max(maxZ, Math.max(brick.start.z, brick.end.z));
        }

        // Build the 3D grid structure
        grid = new ArrayList<>(maxX);
        for (int x = 0; x <= maxX; x++) {
            grid.add(new ArrayList<>(maxY));
            for (int y = 0; y <= maxY; y++) {
                grid.get(x).add(new ArrayList<>(maxZ));
                for (int z = 0; z < maxZ; z++) {
                    grid.get(x).get(y).add(null);
                }
            }
        }
    }

    /*
     * Sort the bricks by z-axis.
     * Since initially the bricks are still falling, we need to let them settle first.
     * To do that -> move them down until they hit another brick or the bottom.
     * They must be moved down in order of their z-axis.
     */
    private void sortBricks() {
        bricks.sort(Comparator.comparingInt(a -> Math.min(a.start.z, a.end.z)));
    }

    /*
     * Because the snapshot was taken while the bricks were still falling, some bricks will still be in the air;
     * you'll need to start by figuring out where they will end up.
     */
    private void settleBricks() {
        for (Cuboid brick : bricks) {
            brick.freeFall();
            markInGrid(brick);
        }
    }

    /*
     * Mark (set to true) the grid with the brick coordinates.
     */
    private void markInGrid(Cuboid brick) {
        for (int x = brick.start.x; x <= brick.end.x; x++) {
            for (int y = brick.start.y; y <= brick.end.y; y++) {
                for (int z = brick.start.z; z <= brick.end.z; z++) {
                    grid.get(x).get(y).set(z, brick);
                }
            }
        }
    }

    private void assignSupported() {
        for (Cuboid brick : bricks) {
            int minZ = Math.min(brick.start.z, brick.end.z);

            for (int x = brick.start.x; x <= brick.end.x; x++) {
                for (int y = brick.start.y; y <= brick.end.y; y++) {
                    if (grid.get(x).get(y).get(minZ - 1) != null) {
                        Cuboid supportingBrick = grid.get(x).get(y).get(minZ - 1);

                        if (!brick.supportedBy.contains(supportingBrick)) {
                            brick.supportedBy.add(supportingBrick);
                        }

                        if (!supportingBrick.supports.contains(brick)) {
                            supportingBrick.supports.add(brick);
                        }
                    }
                }
            }
        }
    }

    /*
     * Prints a slice of the grid in the x-z plane.
     * '?' means bricks are hidden behind other bricks at that location
     */
    private void print2DGrid_xz() {
        HashMap<Cuboid, Character> names = new HashMap<>();
        for (Cuboid brick : bricks) {
            names.put(brick, (char) (names.size() + 65));
        }

        int maxZ = grid.getFirst().getFirst().size();

        for (int z = maxZ - 1; z >= 0; z--) {
            for (List<List<Cuboid>> x : grid) {
                if (z < minZ) {
                    System.out.print("-");
                    continue;
                }

                Set<Cuboid> cuboidSet = new HashSet<>();

                for (List<Cuboid> y : x) {
                    if (y.get(z) != null) {
                        cuboidSet.add(y.get(z));
                    }
                }

                if (cuboidSet.isEmpty()) {
                    System.out.print(".");
                } else if (cuboidSet.size() == 1) {
                    System.out.print(names.get(cuboidSet.iterator().next()));
                } else {
                    System.out.print("?");
                }
            }
            System.out.println();
        }
    }
}