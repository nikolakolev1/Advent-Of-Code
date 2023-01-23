import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static ArrayList<ArrayList<String>> map;
    private static ArrayList<ArrayList<ArrayList<String>>> cubeSidesEx; // 0 - top; 1 - back; 2 - left; 3 - front; 4 - bottom ; 5 - right
    private static ArrayList<ArrayList<ArrayList<String>>> cubeSides; // 0 - top; 1 - right; 2 - front; 3 - left; 4 - bottom ; 5 - back
    private static final int TOP = 0, RIGHT = 1, FRONT = 2, LEFT = 3, BOTTOM = 4, BACK = 5;
    private static ArrayList<String[]> instructions;
    private static final int SOUTH = 1, WEST = 2, NORTH = 3, EAST = 4;
    private static int direction = EAST; // 1 - south; 2 - west; 3 - north; 4 - east
    private static int[] coordinatesMap, coordinatesCube = new int[]{0, 0, 0}; // y, x, side
    private static int[] rowsAndColumnsIndices;
    private static int cubeSideLengthEx, cubeSideLength, lastEx, last;

    public static void main(String[] args) {
        String file = "input.txt";
        loadData(file);
        part1();
        System.out.println();
        direction = EAST;
        if (file.equals("input2.txt")) {
            part2Ex();
        } else {
            part2();
        }
    }

    private static void part1() {
        System.out.println("=== Part 1 ===");

        setStartCoordinates();
        moveAllOnMap();
        calculateAnswer();
    }

    private static void part2Ex() {
        System.out.println("=== Part 2 Ex ===");

        moveAllOnCubeEx();
        calculateAnswerCubeEx();
    }

    private static void part2() {
        System.out.println("=== Part 2 ===");

        moveAllOnCube();
        calculateAnswerCube();
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
                    instructions.add(new String[]{intAsStr.toString(), ""});
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

            if (file.equals("input2.txt")) {
                setCubeSideLengthEx();
                setCubeSidesEx();
            } else {
                setCubeSideLength();
                setCubeSides();
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setCubeSideLengthEx() {
        for (int i = 0; i <= rowsAndColumnsIndices[0]; i++) {
            if (!map.get(i).get(0).equals(" ")) {
                cubeSideLengthEx = i;
                break;
            }
        }

        lastEx = cubeSideLengthEx - 1;
    }

    private static void setCubeSidesEx() {
        cubeSidesEx = new ArrayList<>();

        cubeSidesEx.add(setTopSideEx());
        cubeSidesEx.add(setBackSideEx());
        cubeSidesEx.add(setLeftSideEx());
        cubeSidesEx.add(setFrontSideEx());
        cubeSidesEx.add(setBottomSideEx());
        cubeSidesEx.add(setRightSideEx());
    }

    private static void setCubeSideLength() {
        for (int i = 0; i <= rowsAndColumnsIndices[0]; i++) {
            if (!map.get(i).get(0).equals(" ")) {
                cubeSideLength = i / 2;
                break;
            }
        }

        last = cubeSideLength - 1;
    }

    private static void setCubeSides() {
        cubeSides = new ArrayList<>();

        cubeSides.add(setTopSide());
        cubeSides.add(setRightSide());
        cubeSides.add(setFrontSide());
        cubeSides.add(setLeftSide());
        cubeSides.add(setBottomSide());
        cubeSides.add(setBackSide());
    }

    private static ArrayList<ArrayList<String>> setTopSideEx() {
        int xStart = cubeSideLengthEx * 2;
        int xEnd = cubeSideLengthEx * 3;
        ArrayList<ArrayList<String>> topSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLengthEx; i++) {
            topSide.add(new ArrayList<>(map.get(i).subList(xStart, xEnd)));
        }

        return topSide;
    }

    private static ArrayList<ArrayList<String>> setBackSideEx() {
        int yOffset = cubeSideLengthEx;
        int xStart = 0;
        int xEnd = cubeSideLengthEx;
        ArrayList<ArrayList<String>> backSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLengthEx; i++) {
            backSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return backSide;
    }

    private static ArrayList<ArrayList<String>> setLeftSideEx() {
        int yOffset = cubeSideLengthEx;
        int xStart = cubeSideLengthEx;
        int xEnd = cubeSideLengthEx * 2;
        ArrayList<ArrayList<String>> leftSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLengthEx; i++) {
            leftSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return leftSide;
    }

    private static ArrayList<ArrayList<String>> setFrontSideEx() {
        int yOffset = cubeSideLengthEx;
        int xStart = cubeSideLengthEx * 2;
        int xEnd = cubeSideLengthEx * 3;
        ArrayList<ArrayList<String>> frontSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLengthEx; i++) {
            frontSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return frontSide;
    }

    private static ArrayList<ArrayList<String>> setBottomSideEx() {
        int yOffset = cubeSideLengthEx * 2;
        int xStart = cubeSideLengthEx * 2;
        int xEnd = cubeSideLengthEx * 3;
        ArrayList<ArrayList<String>> bottomSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLengthEx; i++) {
            bottomSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return bottomSide;
    }

    private static ArrayList<ArrayList<String>> setRightSideEx() {
        int yOffset = cubeSideLengthEx * 2;
        int xStart = cubeSideLengthEx * 3;
        int xEnd = cubeSideLengthEx * 4;
        ArrayList<ArrayList<String>> rightSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLengthEx; i++) {
            rightSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return rightSide;
    }

    private static ArrayList<ArrayList<String>> setTopSide() {
        int xStart = cubeSideLength;
        int xEnd = cubeSideLength * 2;
        ArrayList<ArrayList<String>> topSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLength; i++) {
            topSide.add(new ArrayList<>(map.get(i).subList(xStart, xEnd)));
        }

        return topSide;
    }

    private static ArrayList<ArrayList<String>> setRightSide() {
        int xStart = cubeSideLength * 2;
        int xEnd = cubeSideLength * 3;
        ArrayList<ArrayList<String>> rightSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLength; i++) {
            rightSide.add(new ArrayList<>(map.get(i).subList(xStart, xEnd)));
        }

        return rightSide;
    }

    private static ArrayList<ArrayList<String>> setFrontSide() {
        int yOffset = cubeSideLength;
        int xStart = cubeSideLength;
        int xEnd = cubeSideLength * 2;
        ArrayList<ArrayList<String>> frontSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLength; i++) {
            frontSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return frontSide;
    }

    private static ArrayList<ArrayList<String>> setLeftSide() {
        int yOffset = cubeSideLength * 2;
        int xStart = 0;
        int xEnd = cubeSideLength;
        ArrayList<ArrayList<String>> leftSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLength; i++) {
            leftSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return leftSide;
    }

    private static ArrayList<ArrayList<String>> setBottomSide() {
        int yOffset = cubeSideLength * 2;
        int xStart = cubeSideLength;
        int xEnd = cubeSideLength * 2;
        ArrayList<ArrayList<String>> bottomSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLength; i++) {
            bottomSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return bottomSide;
    }

    private static ArrayList<ArrayList<String>> setBackSide() {
        int yOffset = cubeSideLength * 3;
        int xStart = 0;
        int xEnd = cubeSideLength;
        ArrayList<ArrayList<String>> backSide = new ArrayList<>();

        for (int i = 0; i < cubeSideLength; i++) {
            backSide.add(new ArrayList<>(map.get(i + yOffset).subList(xStart, xEnd)));
        }

        return backSide;
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private static void setStartCoordinates() {
        ArrayList<String> firstRow = map.get(0);
        int rowSize = firstRow.size();
        for (int i = 0; i < rowSize; i++) {
            if (firstRow.get(i).equals(".")) {
                coordinatesMap = new int[]{0, i};
                break;
            }
        }
    }

    private static void moveAllOnMap() {
        for (String[] instruction : instructions) {
            moveOnMap(instruction);
        }
    }

    private static void moveOnMap(String[] instruction) {
        int distance = Integer.parseInt(instruction[0]);

        switch (direction) {
            case (SOUTH) -> moveSouth(distance);
            case (WEST) -> moveWest(distance);
            case (NORTH) -> moveNorth(distance);
            case (EAST) -> moveEast(distance);
        }

        switch (instruction[1]) {
            case ("R") -> direction++;
            case ("L") -> direction--;
        }

        if (direction == 0) direction = EAST;
        else if (direction == 5) direction = SOUTH;
    }

    private static void moveSouth(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinate = coordinatesMap[0];

            if (coordinatesMap[0] + 1 <= rowsAndColumnsIndices[0]) coordinatesMap[0]++;
            else coordinatesMap[0] = 0;

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                if (!map.get(0).get(coordinatesMap[1]).equals("#")) {
                    coordinatesMap[0] = 0;
                    while (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                        coordinatesMap[0]++;
                    }
                } else {
                    coordinatesMap[0] = previousCoordinate;
                }
            }

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals("#")) {
                coordinatesMap[0] = previousCoordinate;
                break;
            }
        }
    }

    private static void moveWest(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinate = coordinatesMap[1];

            if (coordinatesMap[1] - 1 >= 0) coordinatesMap[1]--;
            else coordinatesMap[1] = rowsAndColumnsIndices[1];

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                if (!map.get(coordinatesMap[0]).get(rowsAndColumnsIndices[1]).equals("#")) {
                    coordinatesMap[1] = rowsAndColumnsIndices[1];
                    while (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                        coordinatesMap[1]--;
                    }
                } else {
                    coordinatesMap[1] = previousCoordinate;
                }
            }

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals("#")) {
                coordinatesMap[1] = previousCoordinate;
                break;
            }
        }
    }

    private static void moveNorth(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinate = coordinatesMap[0];

            if (coordinatesMap[0] - 1 >= 0) coordinatesMap[0]--;
            else coordinatesMap[0] = rowsAndColumnsIndices[0];

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                if (!map.get(rowsAndColumnsIndices[0]).get(coordinatesMap[1]).equals("#")) {
                    coordinatesMap[0] = rowsAndColumnsIndices[0];
                    while (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                        coordinatesMap[0]--;
                    }
                } else {
                    coordinatesMap[0] = previousCoordinate;
                }
            }

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals("#")) {
                coordinatesMap[0] = previousCoordinate;
                break;
            }
        }
    }

    private static void moveEast(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinate = coordinatesMap[1];

            if (coordinatesMap[1] + 1 <= rowsAndColumnsIndices[1]) coordinatesMap[1]++;
            else coordinatesMap[1] = 0;

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                if (!map.get(coordinatesMap[0]).get(0).equals("#")) {
                    coordinatesMap[1] = 0;
                    while (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals(" ")) {
                        coordinatesMap[1]++;
                    }
                } else {
                    coordinatesMap[1] = previousCoordinate;
                }
            }

            if (map.get(coordinatesMap[0]).get(coordinatesMap[1]).equals("#")) {
                coordinatesMap[1] = previousCoordinate;
                break;
            }
        }
    }

    private static void moveAllOnCubeEx() {
        for (String[] instruction : instructions) {
            moveOnCubeEx(instruction);
        }
    }

    private static void moveOnCubeEx(String[] instruction) {
        int distance = Integer.parseInt(instruction[0]);

        switch (direction) {
            case (SOUTH) -> moveSouthOnCubeEx(distance);
            case (WEST) -> moveWestOnCubeEx(distance);
            case (NORTH) -> moveNorthOnCubeEx(distance);
            case (EAST) -> moveEastOnCubeEx(distance);
        }

        switch (instruction[1]) {
            case ("R") -> direction++;
            case ("L") -> direction--;
        }

        if (direction <= 0) direction = EAST;
        else if (direction >= 5) direction = SOUTH;
    }

    private static void continueMovingOnCubeEx(int distance) {
        switch (direction) {
            case (SOUTH) -> moveSouthOnCubeEx(distance);
            case (WEST) -> moveWestOnCubeEx(distance);
            case (NORTH) -> moveNorthOnCubeEx(distance);
            case (EAST) -> moveEastOnCubeEx(distance);
        }
    }

    private static void moveSouthOnCubeEx(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateY = coordinatesCube[0];
            ArrayList<ArrayList<String>> thisSide = cubeSidesEx.get(coordinatesCube[2]);

            if (coordinatesCube[0] + 1 <= lastEx) {
                coordinatesCube[0]++;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[0] = previousCoordinateY;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (0) -> newCubeCoordinates = new int[]{0, coordinatesCube[1], 3};
                    case (1) -> {
                        newCubeCoordinates = new int[]{lastEx, coordinatesCube[1], 4};
                        newDirection = NORTH;
                    }
                    case (2) -> {
                        newCubeCoordinates = new int[]{lastEx - coordinatesCube[1], 0, 4};
                        newDirection = EAST;
                    }
                    case (3) -> newCubeCoordinates = new int[]{0, coordinatesCube[1], 4};
                    case (4) -> {
                        newCubeCoordinates = new int[]{lastEx, lastEx - coordinatesCube[1], 1};
                        newDirection = NORTH;
                    }
                    case (5) -> {
                        newCubeCoordinates = new int[]{lastEx - coordinatesCube[1], 0, 2};
                        newDirection = EAST;
                    }
                }

                if (cubeSidesEx.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCubeEx((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveWestOnCubeEx(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateX = coordinatesCube[1];
            ArrayList<ArrayList<String>> thisSide = cubeSidesEx.get(coordinatesCube[2]);

            if (coordinatesCube[1] - 1 >= 0) {
                coordinatesCube[1]--;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[1] = previousCoordinateX;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (0) -> {
                        newCubeCoordinates = new int[]{0, coordinatesCube[0], 2};
                        newDirection = SOUTH;
                    }
                    case (1) -> {
                        newCubeCoordinates = new int[]{lastEx, lastEx - coordinatesCube[0], 5};
                        newDirection = NORTH;
                    }
                    case (2) -> newCubeCoordinates = new int[]{coordinatesCube[0], lastEx, 1};
                    case (3) -> newCubeCoordinates = new int[]{coordinatesCube[0], lastEx, 2};
                    case (4) -> {
                        newCubeCoordinates = new int[]{lastEx, lastEx - coordinatesCube[0], 2};
                        newDirection = NORTH;
                    }
                    case (5) -> newCubeCoordinates = new int[]{coordinatesCube[0], lastEx, 4};
                }

                if (cubeSidesEx.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCubeEx((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveNorthOnCubeEx(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateY = coordinatesCube[0];
            ArrayList<ArrayList<String>> thisSide = cubeSidesEx.get(coordinatesCube[2]);

            if (coordinatesCube[0] - 1 >= 0) {
                coordinatesCube[0]--;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[0] = previousCoordinateY;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (0) -> {
                        newCubeCoordinates = new int[]{0, lastEx - coordinatesCube[1], 1};
                        newDirection = SOUTH;
                    }
                    case (1) -> {
                        newCubeCoordinates = new int[]{0, lastEx - coordinatesCube[1], 0};
                        newDirection = SOUTH;
                    }
                    case (2) -> {
                        newCubeCoordinates = new int[]{coordinatesCube[1], 0, 0};
                        newDirection = EAST;
                    }
                    case (3) -> newCubeCoordinates = new int[]{lastEx, coordinatesCube[1], 0};
                    case (4) -> newCubeCoordinates = new int[]{lastEx, coordinatesCube[1], 3};
                    case (5) -> {
                        newCubeCoordinates = new int[]{lastEx - coordinatesCube[1], lastEx, 3};
                        newDirection = WEST;
                    }
                }

                if (cubeSidesEx.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCubeEx((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveEastOnCubeEx(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateX = coordinatesCube[1];
            ArrayList<ArrayList<String>> thisSide = cubeSidesEx.get(coordinatesCube[2]);

            if (coordinatesCube[1] + 1 <= lastEx) {
                coordinatesCube[1]++;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[1] = previousCoordinateX;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (0) -> {
                        newCubeCoordinates = new int[]{0, coordinatesCube[0], 5};
                        newDirection = WEST;
                    }
                    case (1) -> newCubeCoordinates = new int[]{coordinatesCube[0], 0, 2};
                    case (2) -> newCubeCoordinates = new int[]{coordinatesCube[0], lastEx, 3};
                    case (3) -> {
                        newCubeCoordinates = new int[]{0, lastEx - coordinatesCube[0], 5};
                        newDirection = SOUTH;
                    }
                    case (4) -> newCubeCoordinates = new int[]{lastEx, lastEx - coordinatesCube[0], 5};
                    case (5) -> {
                        newCubeCoordinates = new int[]{coordinatesCube[0], lastEx, 0};
                        newDirection = WEST;
                    }
                }

                if (cubeSidesEx.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCubeEx((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveAllOnCube() {
        for (String[] instruction : instructions) {
            moveOnCube(instruction);
//            printCube();
//            System.out.println();
        }
    }

    private static void moveOnCube(String[] instruction) {
        int distance = Integer.parseInt(instruction[0]);

        switch (direction) {
            case (SOUTH) -> moveSouthOnCube(distance);
            case (WEST) -> moveWestOnCube(distance);
            case (NORTH) -> moveNorthOnCube(distance);
            case (EAST) -> moveEastOnCube(distance);
        }

        switch (instruction[1]) {
            case ("R") -> direction++;
            case ("L") -> direction--;
        }

        if (direction <= 0) direction = EAST;
        else if (direction >= 5) direction = SOUTH;
    }

    private static void continueMovingOnCube(int distance) {
        switch (direction) {
            case (SOUTH) -> moveSouthOnCube(distance);
            case (WEST) -> moveWestOnCube(distance);
            case (NORTH) -> moveNorthOnCube(distance);
            case (EAST) -> moveEastOnCube(distance);
        }
    }

    private static void moveSouthOnCube(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateY = coordinatesCube[0];
            ArrayList<ArrayList<String>> thisSide = cubeSides.get(coordinatesCube[2]);

            if (coordinatesCube[0] + 1 <= last) {
                coordinatesCube[0]++;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[0] = previousCoordinateY;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (TOP) -> newCubeCoordinates = new int[]{0, coordinatesCube[1], FRONT};
                    case (RIGHT) -> {
                        newCubeCoordinates = new int[]{coordinatesCube[1], last, FRONT};
                        newDirection = WEST;
                    }
                    case (FRONT) -> newCubeCoordinates = new int[]{0, coordinatesCube[1], BOTTOM};
                    case (LEFT) -> newCubeCoordinates = new int[]{0, coordinatesCube[1], BACK};
                    case (BOTTOM) -> {
                        newCubeCoordinates = new int[]{coordinatesCube[1], last, BACK};
                        newDirection = WEST;
                    }
                    case (BACK) -> newCubeCoordinates = new int[]{0, coordinatesCube[1], RIGHT};
                }

                if (cubeSides.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCube((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveWestOnCube(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateX = coordinatesCube[1];
            ArrayList<ArrayList<String>> thisSide = cubeSides.get(coordinatesCube[2]);

            if (coordinatesCube[1] - 1 >= 0) {
                coordinatesCube[1]--;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[1] = previousCoordinateX;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (TOP) -> {
                        newCubeCoordinates = new int[]{last - coordinatesCube[0], 0, LEFT};
                        newDirection = EAST;
                    }
                    case (RIGHT) -> newCubeCoordinates = new int[]{coordinatesCube[0], last, TOP};
                    case (FRONT) -> {
                        newCubeCoordinates = new int[]{0, coordinatesCube[0], LEFT};
                        newDirection = SOUTH;
                    }
                    case (LEFT) -> {
                        newCubeCoordinates = new int[]{last - coordinatesCube[0], 0, TOP};
                        newDirection = EAST;
                    }
                    case (BOTTOM) -> newCubeCoordinates = new int[]{coordinatesCube[0], last, LEFT};
                    case (BACK) -> {
                        newCubeCoordinates = new int[]{0, coordinatesCube[0], TOP};
                        newDirection = SOUTH;
                    }
                }

                if (cubeSides.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCube((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveNorthOnCube(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateY = coordinatesCube[0];
            ArrayList<ArrayList<String>> thisSide = cubeSides.get(coordinatesCube[2]);

            if (coordinatesCube[0] - 1 >= 0) {
                coordinatesCube[0]--;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[0] = previousCoordinateY;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (TOP) -> {
                        newCubeCoordinates = new int[]{coordinatesCube[1], 0, BACK};
                        newDirection = EAST;
                    }
                    case (RIGHT) -> newCubeCoordinates = new int[]{last, coordinatesCube[1], BACK};
                    case (FRONT) -> newCubeCoordinates = new int[]{last, coordinatesCube[1], TOP};
                    case (LEFT) -> {
                        newCubeCoordinates = new int[]{coordinatesCube[1], 0, FRONT};
                        newDirection = EAST;
                    }
                    case (BOTTOM) -> newCubeCoordinates = new int[]{last, coordinatesCube[1], FRONT};
                    case (BACK) -> newCubeCoordinates = new int[]{last, coordinatesCube[1], LEFT};
                }

                if (cubeSides.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCube((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void moveEastOnCube(int distance) {
        for (int i = 0; i < distance; i++) {
            int previousCoordinateX = coordinatesCube[1];
            ArrayList<ArrayList<String>> thisSide = cubeSides.get(coordinatesCube[2]);

            if (coordinatesCube[1] + 1 <= last) {
                coordinatesCube[1]++;
                if (thisSide.get(coordinatesCube[0]).get(coordinatesCube[1]).equals("#")) {
                    coordinatesCube[1] = previousCoordinateX;
                    break;
                }
            } else {
                int[] newCubeCoordinates = new int[]{};
                int newDirection = direction;
                switch (coordinatesCube[2]) {
                    case (TOP) -> newCubeCoordinates = new int[]{coordinatesCube[0], 0, RIGHT};
                    case (RIGHT) -> {
                        newCubeCoordinates = new int[]{last - coordinatesCube[0], last, BOTTOM};
                        newDirection = WEST;
                    }
                    case (FRONT) -> {
                        newCubeCoordinates = new int[]{last, coordinatesCube[0], RIGHT};
                        newDirection = NORTH;
                    }
                    case (LEFT) -> newCubeCoordinates = new int[]{coordinatesCube[0], 0, BOTTOM};
                    case (BOTTOM) -> {
                        newCubeCoordinates = new int[]{last - coordinatesCube[0], last, RIGHT};
                        newDirection = WEST;
                    }
                    case (BACK) -> {
                        newCubeCoordinates = new int[]{last, coordinatesCube[0], BOTTOM};
                        newDirection = NORTH;
                    }
                }

                if (cubeSides.get(newCubeCoordinates[2]).get(newCubeCoordinates[0]).get(newCubeCoordinates[1]).equals(".")) {
                    coordinatesCube = newCubeCoordinates;
                    direction = newDirection;
                    continueMovingOnCube((distance - 1) - i);
                }
                break;
            }
        }
    }

    private static void calculateAnswer() {
        int answer = (1000 * (coordinatesMap[0] + 1)) + (4 * (coordinatesMap[1] + 1)) + direction;
        if (direction == 4) answer -= 4;

        System.out.println("Answer: " + answer);
    }

    private static void calculateAnswerCubeEx() {
        int y = coordinatesCube[0];
        int x = coordinatesCube[1];
        switch (coordinatesCube[2]) {
            case (0) -> x += 2 * cubeSideLengthEx;
            case (1) -> y += cubeSideLengthEx;
            case (2) -> {
                y += cubeSideLengthEx;
                x += cubeSideLengthEx;
            }
            case (3) -> {
                y += cubeSideLengthEx;
                x += 2 * cubeSideLengthEx;
            }
            case (4) -> {
                y += 2 * cubeSideLengthEx;
                x += 2 * cubeSideLengthEx;
            }
            case (5) -> {
                y += 2 * cubeSideLengthEx;
                x += 3 * cubeSideLengthEx;
            }
        }

        int answer = (1000 * (y + 1)) + (4 * (x + 1)) + direction;
        if (direction == 4) answer -= 4;

        System.out.println("Answer: " + answer);
    }

    private static void calculateAnswerCube() {
        int y = coordinatesCube[0];
        int x = coordinatesCube[1];
        switch (coordinatesCube[2]) {
            case (TOP) -> x += cubeSideLength;
            case (RIGHT) -> x += 2 * cubeSideLength;
            case (FRONT) -> {
                y += cubeSideLength;
                x += cubeSideLength;
            }
            case (LEFT) -> y += 2 * cubeSideLength;
            case (BOTTOM) -> {
                y += 2 * cubeSideLength;
                x += cubeSideLength;
            }
            case (BACK) -> y += 3 * cubeSideLength;
        }

        int answer = (1000 * (y + 1)) + (4 * (x + 1)) + direction;
        if (direction == 4) answer -= 4;

        System.out.println("Answer: " + answer);
    }

    private static void printMap() {
        int rows = map.size();
        for (int i = 0; i < rows; i++) {
            int rowLength = map.get(0).size();
            for (int j = 0; j < rowLength; j++) {
                if (coordinatesMap[0] == i && coordinatesMap[1] == j) {
                    switch (direction) {
                        case SOUTH -> System.out.print("v");
                        case WEST -> System.out.print("<");
                        case NORTH -> System.out.print("^");
                        case EAST -> System.out.print(">");
                    }
                } else System.out.print(map.get(i).get(j));
            }
            System.out.println();
        }
    }

    private static void printCube() {
        int sideIndex = coordinatesCube[2];
        ArrayList<ArrayList<String>> side = cubeSides.get(sideIndex);

        int rows = side.size();
        for (int i = 0; i < rows; i++) {
            int rowLength = side.get(0).size();
            for (int j = 0; j < rowLength; j++) {
                if (coordinatesCube[0] == i && coordinatesCube[1] == j) {
                    switch (direction) {
                        case SOUTH -> System.out.print("v");
                        case WEST -> System.out.print("<");
                        case NORTH -> System.out.print("^");
                        case EAST -> System.out.print(">");
                    }
                } else System.out.print(side.get(i).get(j));
            }
            System.out.println();
        }
    }
}