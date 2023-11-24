import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static ArrayList<Sensor> sensors;
    private static final int X = 0, Y = 1;
    private static boolean answerFoundP2 = false;
    private static int count = 0;
    private static int maxX = 0;

    public static void main(String[] args) {
        part1(2000000);
        System.out.println();
        part2(4000000);
    }

    private static void loadData(String file) {
        sensors = new ArrayList<>();

        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();
                thisLine = thisLine.replaceAll("Sensor at x=|: closest beacon is at | y=|x", "");

                String[] sensorAndBeacon = thisLine.split("=");
                String[] sensorCoordinates = sensorAndBeacon[0].split(",");
                String[] beaconCoordinates = sensorAndBeacon[1].split(",");

                Beacon closestBeacon = new Beacon(Integer.parseInt(beaconCoordinates[X]), Integer.parseInt(beaconCoordinates[Y]));
                sensors.add(new Sensor(Integer.parseInt(sensorCoordinates[X]), Integer.parseInt(sensorCoordinates[Y]), closestBeacon));
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part1(int rowNo) {
        loadData("input.txt");
        calculateForRow(rowNo, 1);
    }

    private static void part2(int maxXY) {
        loadData("input.txt");
        maxX = maxXY;
        for (count = 0; count <= maxXY; count++) {
            if (answerFoundP2) break;
            calculateForRow(count, 2);
        }
    }

    private static void calculateForRow(int rowNo, int part) {
        ArrayList<Integer> rowOutOfOrder = new ArrayList<>();

        for (Sensor sensor : sensors) {
            int sensorY = sensor.coordinates[Y];
            int distanceFromRow = Math.abs(sensorY - rowNo);

            noBeaconRange(sensor, distanceFromRow, rowOutOfOrder);
        }

        ArrayList<Integer> rowInOrder = reorder(rowOutOfOrder);
        int size = rowInOrder.size();

        if (part == 1) {
            System.out.println("=== Part 1 ===\nAnswer: " + answerP1(rowInOrder.get(size - 1), rowInOrder.get(size - 2)));
        } else if (answerFoundP2) {
            System.out.println("=== Part 2 ===\nAnswer: " + answerP2(rowInOrder.get(size - 2), rowInOrder.get(size - 1)));
        }
    }

    private static void noBeaconRange(Sensor sensor, int distanceFromRow, ArrayList<Integer> rowToEvaluate) {
        if (sensor.distanceToBeacon >= distanceFromRow) {
            int noBeaconsLeftRight = sensor.distanceToBeacon - distanceFromRow;
            int sensorX = sensor.coordinates[X];

            int noBeaconsFrom = sensorX - noBeaconsLeftRight;
            int noBeaconsTo = sensorX + noBeaconsLeftRight;

            rowToEvaluate.addAll(Arrays.asList(noBeaconsFrom, noBeaconsTo));
        }
    }

    private static ArrayList<Integer> reorder(ArrayList<Integer> rowOutOfOrder) {
        ArrayList<Double> temp = new ArrayList<>();

        for (int i = 0; i < rowOutOfOrder.size(); i++) {
            double index = rowOutOfOrder.get(i) > 0 ? (double) i / 100 : (double) (-i) / 100;
            temp.add(rowOutOfOrder.get(i++) + index);
        }

        Collections.sort(temp);

        ArrayList<Integer> rowReformatted = new ArrayList<>();
        for (int i = 0; i < (rowOutOfOrder.size() / 2); i++) {
            double index = temp.get(i) < 0 ? temp.get(i) - Math.ceil(temp.get(i)) : temp.get(i) - Math.floor(temp.get(i));
            index *= 100;
            int newIndex = (int) Math.round(Math.abs(index));

            rowReformatted.add(rowOutOfOrder.get(newIndex));
            rowReformatted.add(rowOutOfOrder.get(newIndex + 1));
        }

        for (int i = 2; i < rowOutOfOrder.size(); i++) {
            int currentLower = rowReformatted.get(i), currentUpper = rowReformatted.get(++i);
            int previousLower = rowReformatted.get(i - 3), previousUpper = rowReformatted.get(i - 2);

            if (currentLower <= previousLower) {
                if (currentUpper >= previousLower && currentUpper <= previousUpper) {
                    rowReformatted.set(i, previousUpper);
                }
            } else if (currentLower <= previousUpper) {
                rowReformatted.set(i - 1, previousLower);
                if (currentUpper <= previousUpper) {
                    rowReformatted.set(i, previousUpper);
                }
            } else if ((currentLower > 0 && currentLower <= maxX) && (currentLower > previousUpper + 1)) {
                rowReformatted.add(currentLower - 1);
                rowReformatted.add(count);
                answerFoundP2 = true;
                break;
            }
        }

        return rowReformatted;
    }

    private static int answerP1(int to, int from) {
        return to - from;
    }

    private static BigInteger answerP2(int x, int y) {
        return BigInteger.valueOf((x * 4000000L) + y);
    }
}

class Sensor {
    public final int[] coordinates;
    public int distanceToBeacon;

    public Sensor(int x, int y, Beacon closestBeacon) {
        coordinates = new int[]{x, y};
        distanceToBeacon = calcManhattanDist(closestBeacon.coordinates);
    }

    private int calcManhattanDist(int[] beaconCoordinates) {
        int sensorX = coordinates[0], sensorY = coordinates[1];
        int beaconX = beaconCoordinates[0], beaconY = beaconCoordinates[1];

        int xDiff = Math.abs(beaconX - sensorX);
        int yDiff = Math.abs(beaconY - sensorY);

        return xDiff + yDiff;
    }
}

class Beacon {
    public int[] coordinates;

    public Beacon(int x, int y) {
        coordinates = new int[]{x, y};
    }
}