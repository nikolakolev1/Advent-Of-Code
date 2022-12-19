import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Integer[]> blueprints;
    private static int[] blueprintResults;

    public static void main(String[] args) {
        loadData("input2.txt");

        int blueprintIndexTemp = 0;

        // TODO: for loop the length of blueprints
        // blueprintIndex = blueprintNumber - 1 | turn = minute - 1 | goFor = 1: ore; 2; clay; 3: obsidian; 4: geode
        dfs(blueprintIndexTemp, 2, 1, new int[]{1, 0, 0, 0}, new int[4]);
        dfs(blueprintIndexTemp, 1, 1, new int[]{1, 0, 0, 0}, new int[4]);

        System.out.println(blueprintResults[blueprintIndexTemp]);
        System.out.println("Hello world!");
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
        int[] newRobots = new int[robots.length];
        System.arraycopy(robots, 0, newRobots, 0, robots.length);
        int[] newResources = new int[resources.length];
        System.arraycopy(resources, 0, newResources, 0, resources.length);

        if (turn <= 24) {
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
        resourcesGathering(robots, resources);

        // termination condition
        if (turn == 24) { // return because you start from 0 (if you started for 1 would do resource gathering;
            if (blueprintResults[blueprintIndex] < resources[3]) blueprintResults[blueprintIndex] = resources[3];
        } else if (turn < 24) {
            Integer[] currentBlueprint = blueprints.get(blueprintIndex);

            switch (goFor) {
                case 1 -> {
                    if (resources[0] < currentBlueprint[1]) {
                        while (resources[0] < currentBlueprint[1]) {
                            if (turn <= 24) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                            resourcesGathering(robots, resources);
                        }
                    }

                    resources[0] -= currentBlueprint[1];
                    robots[0]++;
                }

                case 2 -> {
                    if (resources[0] < currentBlueprint[2]) {
                        while (resources[0] < currentBlueprint[2]) {
                            if (turn <= 24) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                            resourcesGathering(robots, resources);
                        }
                    }

                    resources[0] -= currentBlueprint[2];
                    robots[1]++;
                }

                case 3 -> {
                    if (resources[0] < currentBlueprint[3] || resources[1] < currentBlueprint[4]) {
                        while (resources[0] < currentBlueprint[3] || resources[1] < currentBlueprint[4]) {
                            if (turn <= 24) turn++;
                            else {
                                if (blueprintResults[blueprintIndex] < resources[3])
                                    blueprintResults[blueprintIndex] = resources[3];
                                return;
                            }
                            resourcesGathering(robots, resources);
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
                            if (turn < 24) turn++;
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

            int turnP1 = turn + 1;
            // go for geode
            // if obsidian (and clay) gathering possible
            // and obsidian for geode is no more than a few turns away
            if (robots[2] > 0 && robots[2] > currentBlueprint[6] / 5) {
                dfs(blueprintIndex, 4, turnP1, robots, resources);
            }

            // go for obsidian
            // if clay gathering (only) possible
            // and more obsidian robots are required
            if (robots[1] > 0 && robots[2] < currentBlueprint[6] / 3) {
                // and clay for geode is no more than a few turns away
                if (robots[1] > currentBlueprint[4] / 5)
                    dfs(blueprintIndex, 3, turnP1, robots, resources);
            }

            // go for clay
            // if clay robots are more than two moves away form obsidian
            // and turn is less than 15
            if (robots[1] < currentBlueprint[4] / 2 && turn < 15) { // restricting condition (lessen possibilities)
                dfs(blueprintIndex, 2, turnP1, robots, resources);
            }

            // go for ore
            // if you have less than the max number of ores for clay
            // and turn is less than 12
            if (robots[0] < currentBlueprint[3] && turn < 12) { // restricting condition (lessen possibilities)
                dfs(blueprintIndex, 1, turnP1, robots, resources);
            }
        } else {
            System.out.println("ERROR: This shouldn't be reached.");
        }
    }
}