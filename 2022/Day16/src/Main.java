import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static HashMap<String, Integer> valvesRates = new HashMap<>();
    public static ArrayList<Valve> allValves = new ArrayList<>();
    public static int maxFlowRate = 0;
    private static int mostPressuredReleased = 0;

    public static void main(String[] args) {
        part1();
    }

    private static void part1() {
        loadDate("input.txt");
        addNeighbours();
        allValves.get(0).setAllDistanceFromValve();
        addDistancesToValves();
        findBestSolution(30, allValves.get(21), 0, createFirstAllValvesReleased());

        System.out.println("Part1 - Most pressured released: " + mostPressuredReleased);
    }

    private static void loadDate(String file) {
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

                maxFlowRate += thisValve.flow_rate;
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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

    private static void addDistancesToValves() {
        for (Valve currentValve : allValves) {
            currentValve.setAllDistanceFromValve();
        }
    }

    private static boolean[] createFirstAllValvesReleased() {
        int allValvesSize = allValves.size();
        boolean[] allValvesReleased = new boolean[allValvesSize];

        for (int i = 0; i < allValvesSize; i++) {
            if (allValves.get(i).flow_rate == 0 || i == 0) allValvesReleased[i] = true;
        }

        return allValvesReleased;
    }

    private static void findBestSolution(int remainingMinutes, Valve currentValve, int releasedPressure, boolean[] allValvesReleased) {
        int allValvesSize = allValves.size();

        // check if all valves are opened
        boolean allReleased = true;
        for (Boolean b : allValvesReleased) {
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
                if (!allValvesReleased[i]) {
                    int newRemainingMinutes = remainingMinutes - currentValve.distanceFromValve.get(evaluatedValve.label) - 1;

                    // if time's up - check if a new best solution was found
                    if (newRemainingMinutes < 0) {
                        if (releasedPressure > mostPressuredReleased) mostPressuredReleased = releasedPressure;
                    } else if (evaluatedValve.flow_rate > 0) {
                        boolean[] newAllValvesReleased = new boolean[allValvesSize];
                        System.arraycopy(allValvesReleased, 0, newAllValvesReleased, 0, allValvesSize);
                        newAllValvesReleased[i] = true;
                        int newReleasedPressure = releasedPressure + (evaluatedValve.flow_rate * newRemainingMinutes);

                        findBestSolution(newRemainingMinutes, evaluatedValve, newReleasedPressure, newAllValvesReleased);
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
        public HashMap<String, Integer> distanceFromValve = new HashMap<>();

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

            while (distanceFromValve.size() < allValves.size()) {
                int neighboursNextRound = 0;

                for (int j = 0; j < numNeighbours; j++) {
                    Valve assessedValve = valvesQueue.poll();

                    assert assessedValve != null;
                    if (!distanceFromValve.containsKey(assessedValve.label)) {
                        distanceFromValve.put(assessedValve.label, distanceCounter);
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