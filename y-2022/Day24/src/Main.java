import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static ArrayList<ArrayList<ArrayList<Tile>>> allMaps;
    private static ArrayList<ArrayList<Tile>> initialMap;
    private static Integer[][][] tileReachedWith;
    private static final ArrayList<Blizzard> allBlizzards = new ArrayList<>();
    private static int mapSizeY;
    private static int mapSizeX;
    private static int leastMoves = 100000;

    public static void main(String[] args) {
        part1();
        System.out.println();
        part2();
    }

    private static void part1() {
        loadData("input.txt");
        loadALlMaps();
        loadTileReachedWith();

        DFS(0, new int[]{0, 1}, mapSizeY - 1, 0);

        System.out.println("=== Part 1 ===\nAnswer: " + leastMoves);
    }

    private static void part2() {
        loadData("input.txt");
        loadALlMaps();
        loadTileReachedWith();

        int totalLeastMoves = 0;

        DFS(0, new int[]{0, 1}, mapSizeY - 1, totalLeastMoves);
        totalLeastMoves += leastMoves;
        leastMoves = 100000;
        loadTileReachedWith();

        DFS(0, new int[]{mapSizeY - 1, mapSizeX - 2}, 0, totalLeastMoves);
        totalLeastMoves += leastMoves;
        leastMoves = 100000;
        loadTileReachedWith();

        DFS(0, new int[]{0, 1}, mapSizeY - 1, totalLeastMoves);
        totalLeastMoves += leastMoves;

        System.out.println("=== Part 2 ===\nAnswer: " + totalLeastMoves);
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            initialMap = new ArrayList<>();
            int y = 0;
            while (myScanner.hasNextLine()) {
                ArrayList<Tile> row = new ArrayList<>();
                String[] thisLine = myScanner.nextLine().split("");

                for (int x = 0; x < thisLine.length; x++) {
                    String symbol = thisLine[x];
                    row.add(new Tile(y, x, symbol));
                }

                initialMap.add(row);
                y++;
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadALlMaps() {
        mapSizeY = initialMap.size();
        mapSizeX = initialMap.get(0).size();
        int yAxis = mapSizeY - 2;
        int xAxis = mapSizeX - 2;

        // expand if necessary
        ArrayList<Integer> commonMultiples = new ArrayList<>();
        while (yAxis % 2 == 0 && xAxis % 2 == 0) {
            yAxis /= 2;
            xAxis /= 2;
            commonMultiples.add(2);
        }
        while (yAxis % 3 == 0 && xAxis % 2 == 0) {
            yAxis /= 3;
            xAxis /= 3;
            commonMultiples.add(3);
        }
        while (yAxis % 5 == 0 && xAxis % 2 == 0) {
            yAxis /= 5;
            xAxis /= 5;
            commonMultiples.add(5);
        }
        while (yAxis % 7 == 0 && xAxis % 2 == 0) {
            yAxis /= 7;
            xAxis /= 7;
            commonMultiples.add(7);
        }
        while (yAxis % 11 == 0 && xAxis % 2 == 0) {
            yAxis /= 11;
            xAxis /= 11;
            commonMultiples.add(11);
        }

        int commonPart = 1;
        for (Integer commonFactor : commonMultiples) {
            commonPart *= commonFactor;
        }
        int GCF = ((mapSizeY - 2) * (mapSizeX - 2)) / commonPart;

        allMaps = new ArrayList<>();

        for (int i = 0; i < GCF; i++) {
            allMaps.add(copyMap(initialMap));
//            printCurrentState();
            moveBlizzards();
        }
    }

    private static void loadTileReachedWith() {
        int time = allMaps.size();

        tileReachedWith = new Integer[mapSizeY][mapSizeX][time];
    }

    private static void moveBlizzards() {
        for (Blizzard blizzard : allBlizzards) {
            blizzard.move();
        }
    }

    private static ArrayList<ArrayList<Tile>> copyMap(ArrayList<ArrayList<Tile>> mapToCopy) {
        ArrayList<ArrayList<Tile>> copiedMap = new ArrayList<>();
        int rowSize = mapToCopy.get(0).size();
        for (ArrayList<Tile> tiles : mapToCopy) {
            ArrayList<Tile> copiedRow = new ArrayList<>();
            for (int j = 0; j < rowSize; j++) {
                Tile originalTile = tiles.get(j);
                Tile copiedTile = new Tile(originalTile.coordinates[0], originalTile.coordinates[1], ".");
                if (originalTile.endOfMap) {
                    copiedTile.endOfMap = true;
                } else {
                    copiedTile.blizzardsOnTile.addAll(originalTile.blizzardsOnTile);
                }
                copiedRow.add(copiedTile);
            }
            copiedMap.add(copiedRow);
        }
        return copiedMap;
    }

    private static void DFS(int movesUpToNow, int[] coordinates, int goal, int carry) {
        int thisMapIndex = (movesUpToNow + carry) % allMaps.size();

        if (coordinates[0] == goal) {
            if (movesUpToNow < leastMoves) {
                leastMoves = movesUpToNow;
            }
            return;
        } else if (movesUpToNow >= leastMoves) {
            return;
        } else {
            Integer min = tileReachedWith[coordinates[0]][coordinates[1]][thisMapIndex];
            if (min == null) {
                min = movesUpToNow;
            } else if (movesUpToNow < min) {
                min = movesUpToNow;
            } else {
                return;
            }
            tileReachedWith[coordinates[0]][coordinates[1]][thisMapIndex] = min;
        }

        int nextMapIndex = thisMapIndex + 1;
        if (nextMapIndex == allMaps.size()) nextMapIndex = 0;
        ArrayList<ArrayList<Tile>> nextMap = allMaps.get(nextMapIndex);
        int[] currentCoordinates = new int[]{coordinates[0], coordinates[1]};
        Queue<int[]> scenarios = new LinkedList<>();

        Tile right = nextMap.get(currentCoordinates[0]).get(currentCoordinates[1] + 1);
        Tile down = null;
        if (coordinates[0] != mapSizeY - 1) {
            down = nextMap.get(currentCoordinates[0] + 1).get(currentCoordinates[1]);
        }
        Tile left = nextMap.get(currentCoordinates[0]).get(currentCoordinates[1] - 1);
        Tile up = null;
        if (coordinates[0] != 0) {
            up = nextMap.get(currentCoordinates[0] - 1).get(currentCoordinates[1]);
        }
        Tile same = nextMap.get(currentCoordinates[0]).get(currentCoordinates[1]);

        if (goal == 0) {
            if (left.blizzardsOnTile.size() == 0 && !left.endOfMap) {
                scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1] - 1});
            }
            if (up.blizzardsOnTile.size() == 0 && !up.endOfMap) {
                scenarios.add(new int[]{currentCoordinates[0] - 1, currentCoordinates[1]});
            }
            if (right.blizzardsOnTile.size() == 0 && !right.endOfMap) {
                scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1] + 1});
            }
            if (coordinates[0] != mapSizeY - 1 && (down.blizzardsOnTile.size() == 0 && !down.endOfMap)) {
                scenarios.add(new int[]{currentCoordinates[0] + 1, currentCoordinates[1]});
            }
        } else {
            if (right.blizzardsOnTile.size() == 0 && !right.endOfMap) {
                scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1] + 1});
            }
            if (coordinates[0] != mapSizeY - 1 && (down.blizzardsOnTile.size() == 0 && !down.endOfMap)) {
                scenarios.add(new int[]{currentCoordinates[0] + 1, currentCoordinates[1]});
            }
            if (left.blizzardsOnTile.size() == 0 && !left.endOfMap) {
                scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1] - 1});
            }
            if (coordinates[0] != 0 && (up.blizzardsOnTile.size() == 0 && !up.endOfMap)) {
                scenarios.add(new int[]{currentCoordinates[0] - 1, currentCoordinates[1]});
            }
        }
        if (same.blizzardsOnTile.size() == 0 && !same.endOfMap) {
            scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1]});
        }

        for (int[] scenario : scenarios) {
            DFS(movesUpToNow + 1, scenario, goal, carry);
        }
    }

    private static void printCurrentState(int[] myCoordinates) {
        for (int y = 0; y < mapSizeY; y++) {
            for (int x = 0; x < mapSizeX; x++) {
                if (y == myCoordinates[0] && x == myCoordinates[1]) {
                    System.out.print("E");
                } else {
                    Tile currentTile = initialMap.get(y).get(x);
                    if (currentTile.endOfMap) System.out.print("#");
                    else {
                        switch (currentTile.blizzardsOnTile.size()) {
                            case (0) -> System.out.print(".");
                            case (1) -> {
                                int direction = currentTile.blizzardsOnTile.get(0).direction;
                                String directionStr = " ";
                                switch (direction) {
                                    case (0) -> directionStr = "<";
                                    case (1) -> directionStr = ">";
                                    case (2) -> directionStr = "^";
                                    case (3) -> directionStr = "v";
                                }
                                System.out.print(directionStr);
                            }
                            default -> System.out.print(currentTile.blizzardsOnTile.size());
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    private static class Tile {
        public int[] coordinates;
        public LinkedList<Blizzard> blizzardsOnTile;
        public boolean endOfMap;

        public Tile(int y, int x, String symbol) {
            coordinates = new int[]{y, x};
            endOfMap = symbol.equals("#");

            blizzardsOnTile = new LinkedList<>();
            if (!(endOfMap || symbol.equals("."))) {
                Blizzard thisBlizzard = new Blizzard(symbol, y, x);
                allBlizzards.add(thisBlizzard);
                blizzardsOnTile.add(thisBlizzard);
            }
        }
    }

    private static class Blizzard {
        public int direction;
        public int[] coordinates;

        public Blizzard(String symbol, int initialY, int initialX) {
            switch (symbol) {
                case ("<") -> direction = 0;
                case (">") -> direction = 1;
                case ("^") -> direction = 2;
                case ("v") -> direction = 3;
            }

            coordinates = new int[]{initialY, initialX};
        }

        public void move() {
            LinkedList<Blizzard> blizzardsOnTile = initialMap.get(coordinates[0]).get(coordinates[1]).blizzardsOnTile;
            Iterator<Blizzard> myIterator = blizzardsOnTile.iterator();

            while (myIterator.hasNext()) {
                Blizzard temp = myIterator.next();
                if (temp.equals(this)) {
                    myIterator.remove();
                    break;
                }
            }

            switch (direction) {
                case (0) -> coordinates[1]--;
                case (1) -> coordinates[1]++;
                case (2) -> coordinates[0]--;
                case (3) -> coordinates[0]++;
            }

            if (coordinates[0] <= 0) coordinates[0] = mapSizeY - 2;
            else if (coordinates[0] >= mapSizeY - 1) coordinates[0] = 1;

            if (coordinates[1] <= 0) coordinates[1] = mapSizeX - 2;
            else if (coordinates[1] >= mapSizeX - 1) coordinates[1] = 1;

            initialMap.get(coordinates[0]).get(coordinates[1]).blizzardsOnTile.add(this);
        }
    }
}