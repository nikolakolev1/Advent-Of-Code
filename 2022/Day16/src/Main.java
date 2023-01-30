import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static HashMap<String, Integer> valvesRates = new HashMap<>();
    public static ArrayList<Valve> allValves = new ArrayList<>();
    private static int mostPressuredReleased = 0;

    public static void main(String[] args) {
        part1();
        mostPressuredReleased = 0;
        System.out.println();
        part2();
    }

    private static void part1() {
        loadData("input.txt");
        addNeighbours();
        allValves.get(0).setAllDistanceFromValve();
        setDistances();
        findBestSolution(30, getValve("AA"), 0, createReleasedValves(), 0);

        System.out.println("=== Part 1 ===\nMost pressured released: " + mostPressuredReleased);
    }

    // Takes around 50 seconds to execute and works only when there are more valves that a single person can open
    // Memoization should help, but it is challenging to implement
    private static void part2() {
        findBestSolution(26, getValve("AA"), 0, createReleasedValves(), 1);

        System.out.println("=== Part 2 ===\nMost pressured released: " + mostPressuredReleased);
    }

    // loads the input
    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine().replaceAll("Valve | has flow rate| tunnels lead to valves | tunnel leads to valve ", "");

                String[] thisLineParts = thisLine.split(";");
                String[] thisValveInfo = thisLineParts[0].split("=");
                String[] thisValveNeighbours = thisLineParts[1].split(", ");

                Valve thisValve = new Valve(thisValveInfo[0], Integer.parseInt(thisValveInfo[1]), thisValveNeighbours);
                valvesRates.put(thisValve.label, thisValve.flow_rate);
                allValves.add(thisValve);
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Valve getValve(String valveLabel) {
        for (Valve valve : allValves) {
            if (valve.label.equals(valveLabel)) return valve;
        }
        return allValves.get(0); // this shouldn't be reached
    }

    // sets each node's neighbouring nodes (each valve's neighbouring valves)
    private static void addNeighbours() {
        for (Valve currentValve : allValves) {
            for (String neighbourLabel : currentValve.neighboursLabels) {
                for (Valve inspectedValve : allValves) {
                    if (inspectedValve.label.equals(neighbourLabel)) {
                        currentValve.neighbours.add(inspectedValve);
                        break;
                    }
                }
            }
        }
    }

    // sets the distances between the nodes in the graph (the valves in the maze)
    private static void setDistances() {
        for (Valve currentValve : allValves) {
            currentValve.setAllDistanceFromValve();
        }
    }

    // create the initial instance of releasedValves[] and set all the 0 flowing valves to 'opened'
    private static boolean[] createReleasedValves() {
        boolean[] releasedValves = new boolean[allValves.size()];

        for (int i = 0; i < allValves.size(); i++) {
            if (allValves.get(i).flow_rate == 0) releasedValves[i] = true;
        }

        return releasedValves;
    }

    // dfs for the best solution
    private static void findBestSolution(int remainingTime, Valve currentValve, int releasedPressure, boolean[] releasedValves, int helpers) {
        int allValvesSize = allValves.size();

        // check if all valves are opened
        boolean allReleased = true;
        for (Boolean b : releasedValves) {
            if (!b) {
                allReleased = false;
                break;
            }
        }

        // if yes - check if a new best solution was found
        if (allReleased) {
            if (releasedPressure > mostPressuredReleased) mostPressuredReleased = releasedPressure;
        } else { // else - continue looping
            for (int i = 0; i < allValvesSize; i++) {
                Valve evaluatedValve = allValves.get(i);

                // if the next valve is not opened yet, recurse down the path of opening it
                if (!releasedValves[i]) {
                    int newRemainingTime = remainingTime - currentValve.distanceToValve.get(evaluatedValve.label) - 1;

                    // if time's up - check if a new best solution was found
                    if (newRemainingTime < 0) {
                        if (--helpers < 0) {
                            if (releasedPressure > mostPressuredReleased) mostPressuredReleased = releasedPressure;
                        } else {
                            findBestSolution(26, getValve("AA"), releasedPressure, releasedValves, helpers);
                        }
                    } else { // if (evaluatedValve.flow_rate > 0) {
                        boolean[] newReleasedValves = new boolean[allValvesSize];
                        System.arraycopy(releasedValves, 0, newReleasedValves, 0, allValvesSize);
                        newReleasedValves[i] = true;
                        int newReleasedPressure = releasedPressure + (evaluatedValve.flow_rate * newRemainingTime);

                        findBestSolution(newRemainingTime, evaluatedValve, newReleasedPressure, newReleasedValves, helpers);
                    }
                }
            }
        }
    }

    static class Valve {
        public String label;
        public int flow_rate;
        public String[] neighboursLabels;
        public ArrayList<Valve> neighbours = new ArrayList<>();
        public HashMap<String, Integer> distanceToValve = new HashMap<>();

        public Valve(String label, int flow_rate, String[] neighbours) {
            this.label = label;
            this.flow_rate = flow_rate;
            this.neighboursLabels = neighbours;
        }

        public void setAllDistanceFromValve() {
            Queue<Valve> valvesQueue = new LinkedList<>();
            int distanceCounter = 0;
            int numNeighbours = 1;
            valvesQueue.add(this);

            while (distanceToValve.size() < allValves.size()) {
                int neighboursNextRound = 0;

                for (int j = 0; j < numNeighbours; j++) {
                    Valve assessedValve = valvesQueue.poll();

                    assert assessedValve != null;
                    if (!distanceToValve.containsKey(assessedValve.label)) {
                        distanceToValve.put(assessedValve.label, distanceCounter);
                    }

                    for (Valve assessedValveNeighbour : assessedValve.neighbours) {
                        if (!valvesQueue.contains(assessedValveNeighbour)) {
                            valvesQueue.add(assessedValveNeighbour);
                            neighboursNextRound++;
                        }
                    }
                }

                numNeighbours = neighboursNextRound;
                distanceCounter++;
            }
        }
    }
}