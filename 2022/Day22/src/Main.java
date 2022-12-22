import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static ArrayList<ArrayList<String>> map;
    private static ArrayList<String[]> instructions;
    private static int direction = 4; // 1 - south; 2 - west; 3 - north; 4 - east
    private static int[] coordinates;
    private static int[] rowsAndColumnsIndices;

    public static void main(String[] args) {
        loadData("input.txt");
        setStartCoordinates();
        moveAll();
        calculateAnswer();

        System.out.println("row: " + (coordinates[0] + 1) + "\ncolumn: " + (coordinates[1] + 1));
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            boolean readMap = true;
            map = new ArrayList<>();
            instructions = new ArrayList<>();

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();

                if (thisLine.isEmpty()) {
                    readMap = false;
                    thisLine = myScanner.nextLine();
                }

                String[] thisLineArr = thisLine.split("");
                if (readMap) {
                    ArrayList<String> row = new ArrayList<>(Arrays.asList(thisLineArr));
                    map.add(row);
                } else {
                    StringBuilder intAsStr = new StringBuilder();
                    for (String character : thisLineArr) {
                        if (isNumeric(character)) {
                            intAsStr.append(character);
                        } else {
                            instructions.add(new String[]{intAsStr.toString(), character});
                            intAsStr = new StringBuilder();
                        }
                    }
                }
            }

            int rowMaxLength = 0;
            for (ArrayList<String> row : map) {
                int thisRowSize = row.size();
                if (thisRowSize > rowMaxLength) rowMaxLength = thisRowSize;
            }

            for (ArrayList<String> row : map) {
                int rowShorterWith = rowMaxLength - row.size();
                for (int i = 0; i < rowShorterWith; i++) {
                    row.add(" ");
                }
            }

            rowsAndColumnsIndices = new int[]{map.size() - 1, rowMaxLength - 1};

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private static void setStartCoordinates() {
        ArrayList<String> firstRow = map.get(0);
        int rowSize = firstRow.size();
        for (int i = 0; i < rowSize; i++) {
            if (firstRow.get(i).equals(".")) {
                coordinates = new int[]{0, i};
                break;
            }
        }
    }

    private static void moveAll() {
        for (String[] instruction : instructions) {
            move(instruction);
        }
    }

    private static void move(String[] instruction) {
        int distance = Integer.parseInt(instruction[0]);

        switch (direction) {
            case (1) -> {
                for (int i = 0; i < distance; i++) {
                    int previousCoordinate = coordinates[0];

                    if (coordinates[0] + 1 <= rowsAndColumnsIndices[0]) {
                        coordinates[0]++;
                    } else {
                        coordinates[0] = 0;
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                        if (!map.get(0).get(coordinates[1]).equals("#")) {
                            coordinates[0] = 0;
                            while (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                                coordinates[0]++;
                            }
                        } else {
                            coordinates[0] = previousCoordinate;
                        }
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals("#")) {
                        coordinates[0] = previousCoordinate;
                        break;
                    }
                }
            }
            case (2) -> {
                for (int i = 0; i < distance; i++) {
                    int previousCoordinate = coordinates[1];

                    if (coordinates[1] - 1 >= 0) {
                        coordinates[1]--;
                    } else {
                        coordinates[1] = rowsAndColumnsIndices[1];
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                        if (!map.get(coordinates[0]).get(rowsAndColumnsIndices[1]).equals("#")) {
                            coordinates[1] = rowsAndColumnsIndices[1];
                            while (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                                coordinates[1]--;
                            }
                        } else {
                            coordinates[1] = previousCoordinate;
                        }
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals("#")) {
                        coordinates[1] = previousCoordinate;
                        break;
                    }
                }
            }
            case (3) -> {
                for (int i = 0; i < distance; i++) {
                    int previousCoordinate = coordinates[0];

                    if (coordinates[0] - 1 >= 0) {
                        coordinates[0]--;
                    } else {
                        coordinates[0] = rowsAndColumnsIndices[0];
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                        if (!map.get(rowsAndColumnsIndices[0]).get(coordinates[1]).equals("#")) {
                            coordinates[0] = rowsAndColumnsIndices[0];
                            while (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                                coordinates[0]--;
                            }
                        } else {
                            coordinates[0] = previousCoordinate;
                        }
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals("#")) {
                        coordinates[0] = previousCoordinate;
                        break;
                    }
                }
            }
            case (4) -> {
                for (int i = 0; i < distance; i++) {
                    int previousCoordinate = coordinates[1];

                    if (coordinates[1] + 1 <= rowsAndColumnsIndices[1]) {
                        coordinates[1]++;
                    } else {
                        coordinates[1] = 0;
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                        if (!map.get(coordinates[0]).get(0).equals("#")) {
                            coordinates[1] = 0;
                            while (map.get(coordinates[0]).get(coordinates[1]).equals(" ")) {
                                coordinates[1]++;
                            }
                        } else {
                            coordinates[1] = previousCoordinate;
                        }
                    }

                    if (map.get(coordinates[0]).get(coordinates[1]).equals("#")) {
                        coordinates[1] = previousCoordinate;
                        break;
                    }
                }
            }
        }

        switch (instruction[1]) {
            case ("R") -> direction++;
            case ("L") -> direction--;
        }

        if (direction == 0) direction = 4;
        else if (direction == 5) direction = 1;
    }

    private static void calculateAnswer() {
        int answer = (1000 * (coordinates[0] + 1)) + (4 * (coordinates[1] + 1)) + direction;
        if (direction == 4) answer -= 4;

        System.out.println("Answer: " + answer);
    }

}