import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static ArrayList<ArrayList<Tile>>[] allMaps;
    private static ArrayList<ArrayList<Tile>> initialMap;
    private static ArrayList<Blizzard> allBlizzards;
    private static int[] myCoordinates; // TODO: Set initial coordinates and then update them
    private static int[] goalCoordinates; // TODO: Set the final coordinates
    // TODO: idea - do a DFS where the termination condition is if(myCoordinates == goalCoordinates)

    public static void main(String[] args) {
        loadData("input2.txt");

        System.out.println("Hello world!");
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            initialMap = new ArrayList<>();
            int x = 0;
            while (myScanner.hasNextLine()) {
                ArrayList<Tile> row = new ArrayList<>();
                String[] thisLine = myScanner.nextLine().split("");

                for (int y = 0; y < thisLine.length; y++) {
                    String symbol = thisLine[y];
                    row.add(new Tile(x, y, symbol));
                }

                x++;
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadALlMaps() {
        int xAxis = initialMap.size() - 2;
        int yAxis = initialMap.get(0).size() - 2;

        // expand if necessary
        ArrayList<Integer> commonMultiples = new ArrayList<>();
        while (xAxis % 2 == 0 && yAxis % 2 == 0) {
            xAxis /= 2;
            yAxis /= 2;
            commonMultiples.add(2);
        }
        while (xAxis % 3 == 0 && yAxis % 2 == 0) {
            xAxis /= 3;
            yAxis /= 3;
            commonMultiples.add(3);
        }
        while (xAxis % 5 == 0 && yAxis % 2 == 0) {
            xAxis /= 5;
            yAxis /= 5;
            commonMultiples.add(5);
        }
        while (xAxis % 7 == 0 && yAxis % 2 == 0) {
            xAxis /= 7;
            yAxis /= 7;
            commonMultiples.add(7);
        }
        while (xAxis % 11 == 0 && yAxis % 2 == 0) {
            xAxis /= 11;
            yAxis /= 11;
            commonMultiples.add(11);
        }

        int GCF = 1;
        for (Integer commonFactor : commonMultiples) {
            GCF *= commonFactor;
        }

        // TODO: Write a loop that loops GCF times and calculates all the maps that are going to loop
    }

    private static void moveBlizzards(ArrayList<ArrayList<Tile>> currentMap) {
        for (ArrayList<Tile> row : currentMap) {
            for (Tile tile : row) {
                while (!tile.blizzardsOnTile.isEmpty()) {
                    // TODO: Stopped here
                    //       Maybe change so that moveBlizzards goes over the allBlizzards list and moves all of them
                }
            }
        }
    }

    private static class Tile {
        public int[] coordinates;
        public LinkedList<Blizzard> blizzardsOnTile;
        public boolean endOfMap;

        public Tile(int x, int y, String symbol) {
            coordinates = new int[]{x, y};
            endOfMap = symbol.equals("#");

            allBlizzards = new ArrayList<>();
            blizzardsOnTile = new LinkedList<>();
            if (!(endOfMap || symbol.equals("."))) {
                Blizzard thisBlizzard = new Blizzard(symbol, x, y);
                allBlizzards.add(thisBlizzard);
                blizzardsOnTile.add(thisBlizzard);
            }
        }
    }

    private static class Blizzard {
        public int direction;
        public int[] coordinates;

        public Blizzard(String symbol, int initialX, int initialY) {
            switch (symbol) {
                case ("<") -> direction = 0;
                case (">") -> direction = 1;
                case ("^") -> direction = 2;
                case ("v") -> direction = 3;
            }

            coordinates = new int[]{initialX, initialY};
        }
    }
}