import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Integer[]> blueprints;
    private static int[] blueprintResults;
    private static int part;

    public static void main(String[] args) {
        part1();

        System.out.println();

        part2();
        System.out.println();
    }

    private static void part1() {
        part = 1;
        loadData("input.txt");

        int blueprintsSize = blueprints.size();
        int allQualityLevels = 0;
        for (int i = 0; i < blueprintsSize; i++) {
            // blueprintIndex = blueprintNumber - 1 | turn = minute - 1 | goFor = 1: ore; 2; clay; 3: obsidian; 4: geode
            dfs(i, 2, 1, new int[]{1, 0, 0, 0}, new int[4]);
            dfs(i, 1, 1, new int[]{1, 0, 0, 0}, new int[4]);
            allQualityLevels += ((i + 1) * blueprintResults[i]);
        }

        System.out.println("=== Part 1 ===\nAnswer: " + allQualityLevels);
    }

    // Run it after part 1
    private static void part2() {
        part = 2;
        int blueprintsSize = blueprints.size();
        if (blueprintsSize == 30) {
            for (int i = blueprintsSize; i > 3; i--) {
                blueprints.remove(i - 1);
            }
            blueprintResults = new int[3];

            blueprintsSize = 3;
            for (int i = 0; i < blueprintsSize; i++) {
                // blueprintIndex = blueprintNumber - 1 | turn = minute - 1 | goFor = 1: ore; 2; clay; 3: obsidian; 4: geode
                dfs(i, 2, 1, new int[]{1, 0, 0, 0}, new int[4]);
                dfs(i, 1, 1, new int[]{1, 0, 0, 0}, new int[4]);
            }

            int result = 1;
            for (int i : blueprintResults) {
                result *= i;
            }

            System.out.println("=== Part 2 ===\nAnswer: " + result);
        } else {
            System.out.println("Run 'part2()' after running 'part1()', please.");
        }
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            blueprints = new ArrayList<>();
            while (myScanner.hasNextLine()) {
                String[] thisLine = myScanner.nextLine().split("Blueprint |: Each ore robot costs | ore. Each clay robot costs | ore. Each obsidian robot costs | ore and | clay. Each geode robot costs | obsidian.");

                Integer[] thisLineIntegers = new Integer[thisLine.length - 1];
                for (int i = 1; i < thisLine.length; i++) thisLineIntegers[i - 1] = Integer.parseInt(thisLine[i]);

                blueprints.add(thisLineIntegers);
            }
            blueprintResults = new int[blueprints.size()];

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // (blueprint index) 1, (goFor 1 ore) 4, (goFor 2 ore) 2, (goFor 3 ore) 3, (goFor 3 clay) 14,
    // (goFor 4 ore) 2, (goFor 4 obs) 7
    private static void dfs(int blueprintIndex, int goFor, int turn, int[] robots, int[] resources) {
        int partMax = part == 1 ? 24 : 32;

        int[] newRobots = new int[robots.length];
        System.arraycopy(robots, 0, newRobots, 0, robots.length);
        int[] newResources = new int[resources.length];
        System.arraycopy(resources, 0, newResources, 0, resources.length);

        if (turn <= partMax) {
//            resourcesGathering(newRobots, newResources);
            robotPurchasing(blueprintIndex, goFor, turn, newRobots, newResources);
        }
    }

    // robots = {1:ore, 2:clay, 3:obs, 4:geode}
    // resources = {ore, clay, obs, geode}
    private static void resourcesGathering(int[] robots, int[] resources) {
        for (int i = 0; i < robots.length; i++) {
            resources[i] += robots[i];
        }
    }

    private static void robotPurchasing(int blueprintIndex, int goFor, int turn, int[] robots, int[] resources) {
        int partMax = part == 1 ? 24 : 32;

        // termination condition
        if (turn == partMax) { // return because you start from 0 (if you started for 1 would do resource gathering;
            resourcesGathering(robots, resources);
            if (blueprintResults[blueprintIndex] < resources[3]) blueprintResults[blueprintIndex] = resources[3];
        } else if (turn < partMax) {
            Integer[] currentBlueprint = blueprints.get(blueprintIndex);

            switch (goFor) {
                case 1 -> {
                    if (resources[0] < currentBlueprint[1]) {
                        while (resources[0] < currentBlueprint[1]) {
                            resourcesGathering(robots, resources);
                            if (turn <= partMax - 1) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                        }
                    }

                    resources[0] -= currentBlueprint[1];
                    robots[0]++;
                }

                case 2 -> {
                    if (resources[0] < currentBlueprint[2]) {
                        while (resources[0] < currentBlueprint[2]) {
                            resourcesGathering(robots, resources);
                            if (turn <= partMax - 1) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                        }
                    }

                    resources[0] -= currentBlueprint[2];
                    robots[1]++;
                }

                case 3 -> {
                    if (resources[0] < currentBlueprint[3] || resources[1] < currentBlueprint[4]) {
                        while (resources[0] < currentBlueprint[3] || resources[1] < currentBlueprint[4]) {
                            resourcesGathering(robots, resources);
                            if (turn <= partMax - 1) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                        }
                    }

                    resources[0] -= currentBlueprint[3];
                    resources[1] -= currentBlueprint[4];
                    robots[2]++;
                }

                case 4 -> {
                    if (resources[0] < currentBlueprint[5] || resources[2] < currentBlueprint[6]) {
                        while (resources[0] < currentBlueprint[5] || resources[2] < currentBlueprint[6]) {
                            resourcesGathering(robots, resources);
                            if (turn <= partMax - 1) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                        }
                    }

                    resources[0] -= currentBlueprint[5];
                    resources[2] -= currentBlueprint[6];
                    robots[3]++;
                }
            }

            int[] temp = new int[robots.length];
            System.arraycopy(robots, 0, temp, 0, temp.length);
            temp[goFor - 1] = robots[goFor - 1] - 1;
            resourcesGathering(temp, resources);

            // go for geode
            // if obsidian (and clay) gathering possible
            // and obsidian for geode is no more than a few turns away (or the time is almost up)
            if (robots[2] > 0 && (turn > 19 || robots[2] > currentBlueprint[6] / 5)) {
                dfs(blueprintIndex, 4, turn + 1, robots, resources);
            }

            // go for obsidian
            // if clay gathering (only) possible
            if (robots[1] > 0) {
                // and clay for geode is no more than a few turns away
                if (robots[1] > currentBlueprint[4] / 5)
                    dfs(blueprintIndex, 3, turn + 1, robots, resources);
            }

            // go for clay
            // if clay robots are more than two moves away form obsidian
            if (robots[1] <= currentBlueprint[4]) { // restricting condition (lessen possibilities)
                dfs(blueprintIndex, 2, turn + 1, robots, resources);
            }

            // go for ore
            // if you have less than the max number of ores for clay
            // and turn is less than 12
            if (robots[0] <= currentBlueprint[3] && turn < 12) { // restricting condition (lessen possibilities)
                dfs(blueprintIndex, 1, turn + 1, robots, resources);
            }
        } else {
            System.out.println("ERROR: This shouldn't be reached.");
        }
    }
}