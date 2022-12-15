import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static ArrayList<Sensor> sensors;

    public static void main(String[] args) {
        part1();

//        loadData("input2.txt");
//        calculateForRow(10);
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

                Beacon closestBeacon = new Beacon(Integer.parseInt(beaconCoordinates[0]), Integer.parseInt(beaconCoordinates[1]));
                sensors.add(new Sensor(Integer.parseInt(sensorCoordinates[0]), Integer.parseInt(sensorCoordinates[1]), closestBeacon));
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void calculateForRow(int rowNumber) {
        ArrayList<Integer> rowToEvaluate = new ArrayList<>();

        // main loop
        for (Sensor currentSensor : sensors) {
            int distanceFromRow = currentSensor.coordinates.y - rowNumber;
            if (distanceFromRow < 0) distanceFromRow *= -1;

            // range from which to which there are no beacons on that row
            if (currentSensor.manhattanDistance >= distanceFromRow) {
                int noBeaconsEitherDirection = currentSensor.manhattanDistance - distanceFromRow;

                int noBeaconsFrom = currentSensor.coordinates.x - noBeaconsEitherDirection;
                int noBeaconsTo = currentSensor.coordinates.x + noBeaconsEitherDirection;

                rowToEvaluate.add(noBeaconsFrom);
                rowToEvaluate.add(noBeaconsTo);
            }
        }

        // evaluate information (very inefficiently)
        ArrayList<Double> temp = new ArrayList<>();

        int rowSize = rowToEvaluate.size();
        for (int i = 0; i < rowSize; i++) {
            double index = (double) i / 100;
            if (rowToEvaluate.get(i) < 0) index *= -1;
            temp.add(rowToEvaluate.get(i++) + index);
        }

        Collections.sort(temp);

        ArrayList<Integer> rowReformatted = new ArrayList<>();
        for (int i = 0; i < (rowSize / 2); i++) {
            double index = temp.get(i) < 0 ? temp.get(i) - Math.ceil(temp.get(i)) : temp.get(i) - Math.floor(temp.get(i));
            index *= index > 0 ? 100 : -100;
            int newIndex = (int) Math.round(index);

            rowReformatted.add(rowToEvaluate.get(newIndex));
            rowReformatted.add(rowToEvaluate.get(newIndex + 1));
        }

        for (int i = 2; i < rowSize; i++) {
            if (i % 2 == 0) {
                int currentLower = rowReformatted.get(i);
                int previousLower = rowReformatted.get(i - 2);
                int previousUpper = rowReformatted.get(i - 1);

                if (currentLower > previousLower && currentLower < previousUpper) {
                    rowReformatted.set(i, previousLower);
                }

                if (previousUpper == currentLower) {
                    rowReformatted.set(i, previousLower);
                }
            } else {
                int currentUpper = rowReformatted.get(i);
                int previousLower = rowReformatted.get(i - 3);
                int previousUpper = rowReformatted.get(i - 2);

                if (currentUpper < previousUpper && currentUpper > previousLower) {
                    rowReformatted.set(i, previousUpper);
                }
            }
        }

        System.out.println("Part1: " + (rowReformatted.get(rowSize - 1) - rowReformatted.get(rowSize - 2)));
    }

    private static void part1() {
        loadData("input.txt");
        calculateForRow(2000000);
    }
}

class Sensor {
    public final Coordinates coordinates;
    public int manhattanDistance;

    public Sensor(int xCoordinate, int yCoordinate, Beacon closestBeacon) {
        coordinates = new Coordinates(xCoordinate, yCoordinate);

        manhattanDistance = calcManhattanDist(closestBeacon.coordinates.x, closestBeacon.coordinates.y);
    }

    public int calcManhattanDist(int otherX, int otherY) {
        int xDiff = otherX - coordinates.x;
        int yDiff = otherY - coordinates.y;

        if (xDiff < 0) xDiff *= -1;
        if (yDiff < 0) yDiff *= -1;

        return xDiff + yDiff;
    }
}

class Beacon {
    public Coordinates coordinates;

    public Beacon(int xCoordinate, int yCoordinate) {
        coordinates = new Coordinates(xCoordinate, yCoordinate);
    }
}

class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}