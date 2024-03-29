import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static String[] inputStream;
    private static LinkedList<StringBuilder> chamber = new LinkedList<>();
    private static int highestRockIndex, rocksCounter, jetCounter, rocksSettled;
    private static int leftmost, rightmost, bottommost;
    private static int moves = 0;
    private static final int preCycleRocks = 236, cycleDurationRocks = 1720, singleCycleHeight = 2704; // hardcoded

    public static void main(String[] args) {
        loadData("input.txt");
        part1(2022);
        System.out.println();
        part2(1000000000000L);
    }

    private static void part1(int movesToCalc) {
        loadInitialChamber();

        while (rocksSettled < movesToCalc) {
            move();
        }

//        printChamber();
//        System.out.println();
        System.out.println("=== Part 1 ===");
        System.out.println("Height: " + (highestRockIndex + 1));
        System.out.println("Chamber size: " + chamber.size());
        System.out.println("Moves: " + moves);
    }

    /*
     * sequence:
     * |..####.|
     * |..##...|
     * |..##...|
     * |..#....|
     * |..#....|
     * |..#.#..|
     * |..#.#..|
     * |..###..|
     * |....#..|
     * |...###.|
     * |....#..|
     * |..####.|
     * |.##....|
     * |.##....|
     * |..#....|
     * |..#....|
     * |..#.#..|
     * |..#.#..|
     *
     * 2704 height difference (singleCycleHeight)
     * 1720 rock difference (cycleDurationRocks)
     *
     * 1st occurs at 236 rocks (preCycleRocks) && 375 height
     * 2nd occurs at 1956 rocks && 3079 height
     * 3rd occurs at 3676 rocks && 5783 height
     */
    private static void part2(long rocksToCalc) {
        long afterPreCycleRocks = rocksToCalc - preCycleRocks;
        long preCycleHeight = heightAfterRocks(preCycleRocks); // should be 375
        long cycleRepetition = afterPreCycleRocks / cycleDurationRocks;
        long totalCycleHeight = cycleRepetition * singleCycleHeight;
        long postCycleRocks = afterPreCycleRocks - (cycleRepetition * cycleDurationRocks);
        long postCycleHeight = heightAfterRocks2(postCycleRocks);

        System.out.println("=== Part 2 ===\nHeight: " + (preCycleHeight + totalCycleHeight + postCycleHeight));
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);
            inputStream = myScanner.nextLine().split("");
            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reset() {
        chamber = new LinkedList<>();
        loadInitialChamber();

        moves = 0;
        rocksSettled = 0;
    }

    private static void loadInitialChamber() {
        for (int i = 0; i < 3; i++) {
            chamber.add(new StringBuilder("......."));
        }
        highestRockIndex = -1;
        rocksCounter = 0;
        jetCounter = 0;
        assignEndPoints();
    }

    private static void printChamber() {
        for (int i = 0; i < chamber.size(); i++) {
            System.out.println("|" + chamber.get(chamber.size() - 1 - i) + "|");
        }
        System.out.println("+-------+");
    }

    private static void printChamberWithDetails() {
        int height = chamber.size();
        String heightStr;

        for (int i = 0; i < chamber.size(); i++) {
            heightStr = height + "";
            if (height < 10) heightStr += "  ";
            else if (height < 100) heightStr += " ";
            System.out.println(heightStr + "|" + chamber.get(chamber.size() - 1 - i) + "|");
            height--;
        }
        System.out.println("   +-------+");
    }

    private static void incrementRockCounter() {
        if (++rocksCounter == 5) rocksCounter = 0;
    }

    private static void assignEndPoints() {
        leftmost = 2;
        bottommost = highestRockIndex + 4;
        switch (rocksCounter) {
            case 0 -> rightmost = 5;
            case 1, 2 -> rightmost = 4;
            case 3 -> rightmost = 2;
            case 4 -> rightmost = 3;
        }
    }

    private static void move() {
        moveLeftOrRight();
        moveDown();
    }

    private static long heightAfterRocks(long moves) {
        reset();
        while (rocksSettled < moves) {
            move();
        }
        return highestRockIndex + 1;
    }

    private static long heightAfterRocks2(long moves) {
        long preHighestRock = heightAfterRocks(preCycleRocks);

        while (rocksSettled < (moves + preCycleRocks)) {
            move();
        }

        return (highestRockIndex + 1) - preHighestRock;
    }

    private static void moveLeftOrRight() {
        moves++;

        String leftOrRight = inputStream[jetCounter++];
        if (jetCounter == inputStream.length) jetCounter = 0;

        if (!(goingOutOfBounds(leftOrRight) || interferingWithRockLeftOrRight(leftOrRight))) {
            switch (leftOrRight) {
                case "<" -> {
                    leftmost -= 1;
                    rightmost -= 1;
                }
                case ">" -> {
                    leftmost += 1;
                    rightmost += 1;
                }
            }
        }
    }

    private static void moveDown() {
        if (interferingWithRockDown()) {
            checkIfNewHighest();

            StringBuilder bottom = chamber.get(bottommost);

            switch (rocksCounter) {
                case 0 -> {
                    for (int i = 0; i < 4; i++) {
                        bottom.setCharAt(leftmost + i, '#');
                    }
                }
                case 1 -> {
                    bottom.setCharAt(leftmost + 1, '#');
                    for (int i = 0; i < 3; i++) {
                        chamber.get(bottommost + 1).setCharAt(leftmost + i, '#');
                    }
                    chamber.get(bottommost + 2).setCharAt(leftmost + 1, '#');
                }
                case 2 -> {
                    for (int i = 0; i < 3; i++) {
                        bottom.setCharAt(leftmost + i, '#');
                    }
                    for (int i = 1; i < 3; i++) {
                        chamber.get(bottommost + i).setCharAt(leftmost + 2, '#');
                    }
                }
                case 3 -> {
                    for (int i = 0; i < 4; i++) {
                        chamber.get(bottommost + i).setCharAt(leftmost, '#');
                    }
                }
                case 4 -> {
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            chamber.get(bottommost + i).setCharAt(leftmost + j, '#');
                        }
                    }
                }
            }

            rocksSettled++;
            incrementRockCounter();
            assignEndPoints();
        } else {
            bottommost--;
        }
    }

    private static boolean goingOutOfBounds(String move) {
        if (move.equals("<")) {
            return leftmost == 0;
        } else return rightmost == 6;
    }

    private static boolean interferingWithRockLeftOrRight(String move) {
        if (bottommost < chamber.size() && move.equals("<")) {
            char leftBottom = chamber.get(bottommost).charAt(leftmost - 1);

            switch (rocksCounter) {
                case 0, 2 -> {
                    if (leftBottom == 35) return true;
                }
                case 1 -> {
                    if ((chamber.get(bottommost).charAt(leftmost) == 35)) {
                        return true;
                    }
                    if (bottommost + 1 < chamber.size()) {
                        if (chamber.get(bottommost + 1).charAt(leftmost - 1) == 35) {
                            return true;
                        }
                    }
                }

                case 3 -> {
                    for (int i = 0; i < 3; i++) {
                        if (bottommost + i < chamber.size()) {
                            if (chamber.get(bottommost + i).charAt(leftmost - 1) == 35) return true;
                        } else break;
                    }
                }
                case 4 -> {
                    for (int i = 0; i < 2; i++) {
                        if (bottommost + i < chamber.size()) {
                            if (chamber.get(bottommost + i).charAt(leftmost - 1) == 35) return true;
                        } else break;
                    }
                }
            }
        } else if (bottommost < chamber.size()) {
            char rightBottom = chamber.get(bottommost).charAt(rightmost + 1);

            switch (rocksCounter) {
                case 0 -> {
                    if (rightBottom == 35) return true;
                }
                case 1 -> {
                    if (chamber.get(bottommost).charAt(rightmost) == 35) return true;
                    if (bottommost + 1 < chamber.size()) {
                        if (chamber.get(bottommost + 1).charAt(rightmost + 1) == 35) {
                            return true;
                        }
                    }
                }
                case 2 -> {
                    for (int i = 0; i < 3; i++) {
                        if (bottommost + i < chamber.size()) {
                            if (chamber.get(bottommost + i).charAt(rightmost + 1) == 35) return true;
                        } else break;
                    }
                }
                case 3 -> {
                    for (int i = 0; i < 4; i++) {
                        if (bottommost + i < chamber.size()) {
                            if (chamber.get(bottommost + i).charAt(rightmost + 1) == 35) return true;
                        } else break;
                    }
                }
                case 4 -> {
                    for (int i = 0; i < 2; i++) {
                        if (bottommost + i < chamber.size()) {
                            if (chamber.get(bottommost + i).charAt(rightmost + 1) == 35) return true;
                        } else break;
                    }
                }
            }
        }

        return false;
    }

    private static boolean interferingWithRockDown() {
        if (bottommost == 0) return true;

        if (bottommost - 1 < chamber.size()) {
            String nextBottom = chamber.get(bottommost - 1).toString();

            switch (rocksCounter) {
                case 0 -> {
                    for (int i = 0; i < 4; i++) {
                        if (nextBottom.charAt(leftmost + i) == 35) return true;
                    }
                }
                case 1 -> {
                    for (int i = 0; i < 2; i++) {
                        if (nextBottom.charAt(leftmost + 1) == 35) return true;
                        if (bottommost < chamber.size()) {
                            if (chamber.get(bottommost).charAt(leftmost + (2 * i)) == 35) return true;
                        }
                    }
                }
                case 2 -> {
                    for (int i = 0; i < 3; i++) {
                        if (nextBottom.charAt(leftmost + i) == 35) return true;
                    }
                }
                case 3 -> {
                    if (nextBottom.charAt(leftmost) == 35) return true;
                }
                case 4 -> {
                    for (int i = 0; i < 2; i++) {
                        if (nextBottom.charAt(leftmost + i) == 35) return true;
                    }
                }
            }
        }

        return false;
    }

    private static void checkIfNewHighest() {
        int currentTop = 0;
        switch (rocksCounter) {
            case 0 -> currentTop = bottommost;
            case 1, 2 -> currentTop = bottommost + 2;
            case 3 -> currentTop = bottommost + 3;
            case 4 -> currentTop = bottommost + 1;
        }
        if (currentTop > highestRockIndex) highestRockIndex = currentTop;
        int difference = chamber.size() - currentTop;
        if (difference < 4) {
            int addRows = 4 - difference;
            for (int i = 0; i < addRows; i++) {
                chamber.add(new StringBuilder("......."));
            }
        }
    }
}