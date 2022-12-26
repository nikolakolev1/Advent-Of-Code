import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static ArrayList<ArrayList<ArrayList<Tile>>> allMaps;
    private static ArrayList<ArrayList<Tile>> initialMap;
    private static final ArrayList<Blizzard> allBlizzards = new ArrayList<>();
    private static int[] myCoordinates;
    private static int[] goalCoordinates;
    // TODO: idea - do a DFS where the termination condition is if(myCoordinates == goalCoordinates)
    // RANT: IMPOSSIBLE TO WORK WHILE DAD SNORES NEXT TO MY HEAD

    public static void main(String[] args) {
        loadData("input2.txt");
        setStartAndGoalCoordinates();
        loadALlMaps();
        DFS(0, myCoordinates);


        System.out.println("Hello world!");
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
        int yAxis = initialMap.size() - 2;
        int xAxis = initialMap.get(0).size() - 2;

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
        int GCF = ((initialMap.size() - 2) * (initialMap.get(0).size() - 2)) / commonPart;

        allMaps = new ArrayList<>();

        for (int i = 0; i < GCF; i++) {
            allMaps.add(copyMap(initialMap));
            moveBlizzards();
        }
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
                copiedRow.add(tiles.get(j));
            }
            copiedMap.add(copiedRow);
        }
        return copiedMap;
    }

    private static void setStartAndGoalCoordinates() {
        myCoordinates = new int[]{0, 1};
        goalCoordinates = new int[]{initialMap.size() - 1, initialMap.get(0).size() - 1};
    }

    private static void DFS(int movesUpToNow, int[] coordinates) {
        // TODO: Stopped here - I believe that there is some issue with the
        //       generation of new maps because it says that on maps with index 1, 2, and 3
        //       there is one blizzard on [1, 1], while on map 1 there should be 0

        if (coordinates[0] == goalCoordinates[0] && coordinates[1] == goalCoordinates[1]) {
            System.out.println("Reached goal position with: " + movesUpToNow + " moves");
            return;
        } else if (movesUpToNow > 50) {
            System.out.print(":(");
            return;
        }

        ArrayList<ArrayList<Tile>> nextMap = allMaps.get((movesUpToNow % allMaps.size()) + 1);
        int[] currentCoordinates = new int[]{coordinates[0], coordinates[1]};
        Queue<int[]> scenarios = new LinkedList<>();

        Tile right = nextMap.get(currentCoordinates[0]).get(currentCoordinates[1] + 1);
        Tile down = nextMap.get(currentCoordinates[0] + 1).get(currentCoordinates[1]);
        Tile left = nextMap.get(currentCoordinates[0]).get(currentCoordinates[1] - 1);
        Tile up = null;
        if (coordinates[0] != 0) {
            up = nextMap.get(currentCoordinates[0] - 1).get(currentCoordinates[1]);
        }
        Tile same = nextMap.get(currentCoordinates[0]).get(currentCoordinates[1]);

        if (right.blizzardsOnTile.size() == 0 && !right.endOfMap) {
            scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1] + 1});
        }
        if (down.blizzardsOnTile.size() == 0 && !down.endOfMap) {
            scenarios.add(new int[]{currentCoordinates[0] + 1, currentCoordinates[1]});
        }
        if (left.blizzardsOnTile.size() == 0 && !left.endOfMap) {
            scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1] - 1});
        }
        if (coordinates[0] != 0 && (up.blizzardsOnTile.size() == 0 && up.endOfMap)) {
            scenarios.add(new int[]{currentCoordinates[0] - 1, currentCoordinates[1]});
        }
        if (same.blizzardsOnTile.size() == 0 && same.endOfMap) {
            scenarios.add(new int[]{currentCoordinates[0], currentCoordinates[1]});
        }

        for (int[] scenario : scenarios) {
            DFS(movesUpToNow + 1, scenario);
        }
    }

    private static void printCurrentState() {
        int mapY = initialMap.size();
        int mapX = initialMap.get(0).size();

        for (int y = 0; y < mapY; y++) {
            for (int x = 0; x < mapX; x++) {
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

            if (coordinates[0] <= 0) coordinates[0] = initialMap.size() - 2;
            else if (coordinates[0] >= initialMap.size() - 1) coordinates[0] = 1;

            if (coordinates[1] <= 0) coordinates[1] = initialMap.get(0).size() - 2;
            else if (coordinates[1] >= initialMap.get(0).size() - 1) coordinates[1] = 1;

            initialMap.get(coordinates[0]).get(coordinates[1]).blizzardsOnTile.add(this);
        }
    }
}