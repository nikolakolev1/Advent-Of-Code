import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<ArrayList<Elf>> map;
    private static int direction; // 0 - North; 1 - South; 2 - West; 3 - East
    private static ArrayList<Elf> elves;
    private static boolean elvesMovedThisRound = true;

    public static void main(String[] args) {
        part1();
        System.out.println();
        part2();
    }

    private static void part1() {
        loadData("input.txt");

        for (int i = 0; i < 10; i++) {
            allElvesMove();
        }

        System.out.println("=== Part 1 ===\nAnswer: " + calculateAnswer());
    }

    private static void part2() {
        loadData("input.txt");

        int moves = 0;
        while (elvesMovedThisRound) {
            allElvesMove();
            moves++;
        }
        System.out.println("=== Part 2 ===\nAnswer: " + moves);
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            boolean isFirstLine = true;
            int addLength = 0;
            int rowLength = 0;
            int rowIndex = 0;
            map = new ArrayList<>();
            elves = new ArrayList<>();
            direction = 0;

            while (myScanner.hasNextLine()) {
                String[] thisLine = myScanner.nextLine().split("");

                if (isFirstLine) {
                    addLength = thisLine.length;
                    rowLength = thisLine.length + (2 * addLength);
                    addBlankRows(addLength, rowLength);
                    rowIndex += addLength;
                    isFirstLine = false;
                }

                ArrayList<Elf> row = new ArrayList<>();
                setAllToNull(row, rowLength);
                for (int i = 0; i < thisLine.length; i++) {
                    if (thisLine[i].equals("#")) {
                        Elf newElf = new Elf(rowIndex, addLength + i);

                        row.set(addLength + i, newElf);
                        elves.add(newElf);
                    }
                }
                map.add(row);
                rowIndex++;
            }
            addBlankRows(addLength, rowLength);

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setAllToNull(ArrayList<Elf> row, int rowLength) {
        for (int j = 0; j < rowLength; j++) {
            row.add(null);
        }
    }

    private static void addBlankRows(int twoThirdsLength, int rowLength) {
        for (int i = 0; i < twoThirdsLength; i++) {
            ArrayList<Elf> row = new ArrayList<>();
            setAllToNull(row, rowLength);
            map.add(row);
        }
    }

    private static void allElvesMove() {
        elvesMovedThisRound = false;

        for (ArrayList<Elf> row : map) {
            for (Elf elf : row) {
                if (elf != null && elf.checkIfToMove()) {
                    switch (direction) {
                        case 0 -> {
                            if (elf.checkNorth()) {
                                elf.setCourseNorth();
                            } else if (elf.checkSouth()) {
                                elf.setCourseSouth();
                            } else if (elf.checkWest()) {
                                elf.setCourseWest();
                            } else if (elf.checkEast()) {
                                elf.setCourseEast();
                            } else elf.moveThisTurn = false;
                        }
                        case 1 -> {
                            if (elf.checkSouth()) {
                                elf.setCourseSouth();
                            } else if (elf.checkWest()) {
                                elf.setCourseWest();
                            } else if (elf.checkEast()) {
                                elf.setCourseEast();
                            } else if (elf.checkNorth()) {
                                elf.setCourseNorth();
                            } else elf.moveThisTurn = false;
                        }
                        case 2 -> {
                            if (elf.checkWest()) {
                                elf.setCourseWest();
                            } else if (elf.checkEast()) {
                                elf.setCourseEast();
                            } else if (elf.checkNorth()) {
                                elf.setCourseNorth();
                            } else if (elf.checkSouth()) {
                                elf.setCourseSouth();
                            } else elf.moveThisTurn = false;
                        }
                        case 3 -> {
                            if (elf.checkEast()) {
                                elf.setCourseEast();
                            } else if (elf.checkNorth()) {
                                elf.setCourseNorth();
                            } else if (elf.checkSouth()) {
                                elf.setCourseSouth();
                            } else if (elf.checkWest()) {
                                elf.setCourseWest();
                            } else elf.moveThisTurn = false;
                        }
                    }

                    elvesMovedThisRound = true;
                }
            }
        }

        for (Elf elf : elves) {
            if (elf.moveThisTurn) {
                if (map.get(elf.nextCoordinates[0]).get(elf.nextCoordinates[1]) == null) {
                    updateMap(elf);
                    elf.moveToNextCoordinates();
                } else {
                    Elf elfToReturn = map.get(elf.nextCoordinates[0]).get(elf.nextCoordinates[1]);
                    returnMapToPrevState(elf, elfToReturn);
                }
            }
            elf.moveThisTurn = false;
        }

        if (++direction == 4) direction = 0;
    }

    private static void updateMap(Elf elf) {
        map.get(elf.nextCoordinates[0]).set(elf.nextCoordinates[1], elf);
        map.get(elf.previousCoordinates[0]).set(elf.previousCoordinates[1], null);
    }

    private static void returnMapToPrevState(Elf elf, Elf elfToReturn) {
        map.get(elf.nextCoordinates[0]).set(elf.nextCoordinates[1], null);
        map.get(elfToReturn.previousCoordinates[0]).set(elfToReturn.previousCoordinates[1], elfToReturn);
        elfToReturn.coordinates[0] = elfToReturn.previousCoordinates[0];
        elfToReturn.coordinates[1] = elfToReturn.previousCoordinates[1];
    }

    private static int calculateAnswer() {
        int emptyTiles = 0;
        int[] minXAndMinY = new int[]{100, 100};
        int[] maxXAndMaxY = new int[]{0, 0};

        for (Elf elf : elves) {
            if (elf.coordinates[0] < minXAndMinY[0]) minXAndMinY[0] = elf.coordinates[0];
            if (elf.coordinates[1] < minXAndMinY[1]) minXAndMinY[1] = elf.coordinates[1];
            if (elf.coordinates[0] > maxXAndMaxY[0]) maxXAndMaxY[0] = elf.coordinates[0];
            if (elf.coordinates[1] > maxXAndMaxY[1]) maxXAndMaxY[1] = elf.coordinates[1];
        }

        for (int i = minXAndMinY[0]; i <= maxXAndMaxY[0]; i++) {
            for (int j = minXAndMinY[1]; j <= maxXAndMaxY[1]; j++) {
                if (map.get(i).get(j) == null) emptyTiles++;
            }
        }

        return emptyTiles;
    }

    private static void printMap() {
        for (ArrayList<Elf> row : map) {
            for (Elf elf : row) {
                if (elf == null) System.out.print(".");
                else System.out.print("#");
            }
            System.out.println();
        }
    }

    private static class Elf {
        public int[] coordinates;
        public int[] previousCoordinates;
        public int[] nextCoordinates;
        public boolean moveThisTurn;

        private Elf(int rowCoordinate, int columnCoordinate) {
            coordinates = new int[]{rowCoordinate, columnCoordinate};
            previousCoordinates = new int[2];
            nextCoordinates = new int[2];
        }

        private boolean checkIfToMove() {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (((coordinates[0] - 1) < 0 || (coordinates[0] + 1) == map.size()) ||
                            (coordinates[1] - 1) < 0 || (coordinates[1] + 1) == map.get(0).size()) {
                        System.out.println("Elf goes out of bounds");
                        return false;
                    } else {
                        // TODO: Last added these
                        if ((i != 0 || j != 0) && map.get(coordinates[0] + i).get(coordinates[1] + j) != null) {
                            moveThisTurn = true;
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean checkNorth() {
            for (int i = -1; i <= 1; i++) {
                if (map.get(coordinates[0] - 1).get(coordinates[1] + i) != null) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkSouth() {
            for (int i = -1; i <= 1; i++) {
                if (map.get(coordinates[0] + 1).get(coordinates[1] + i) != null) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkWest() {
            for (int i = -1; i <= 1; i++) {
                if (map.get(coordinates[0] + i).get(coordinates[1] - 1) != null) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkEast() {
            for (int i = -1; i <= 1; i++) {
                if (map.get(coordinates[0] + i).get(coordinates[1] + 1) != null) {
                    return false;
                }
            }
            return true;
        }

        private void setCourseNorth() {
            rememberCurrentCoordinates();

            nextCoordinates[0] = coordinates[0] - 1;
            nextCoordinates[1] = coordinates[1];
        }

        private void setCourseSouth() {
            rememberCurrentCoordinates();

            nextCoordinates[0] = coordinates[0] + 1;
            nextCoordinates[1] = coordinates[1];
        }

        private void setCourseWest() {
            rememberCurrentCoordinates();

            nextCoordinates[0] = coordinates[0];
            nextCoordinates[1] = coordinates[1] - 1;
        }

        private void setCourseEast() {
            rememberCurrentCoordinates();

            nextCoordinates[0] = coordinates[0];
            nextCoordinates[1] = coordinates[1] + 1;
        }

        private void moveToNextCoordinates() {
            coordinates[0] = nextCoordinates[0];
            coordinates[1] = nextCoordinates[1];
        }

        private void rememberCurrentCoordinates() {
            previousCoordinates[0] = coordinates[0];
            previousCoordinates[1] = coordinates[1];
        }
    }
}