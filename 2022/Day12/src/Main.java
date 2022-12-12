import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<ArrayList<Tile>> map = new ArrayList<>();
    private static final int[][] startAndFinish = new int[2][2];
    private static int lineCounter = -1;
    public static ArrayList<Tile> calculateNext = new ArrayList<>();
    public static int calcNextCounter = 0;
    private static int moveCounter = 1;

    public static void main(String[] args) {
        loadData();
        provideTilesWithNeighbours();
        calculateNext.add(map.get(startAndFinish[1][0]).get(startAndFinish[1][1]));
        calcDistanceToStart();

        printStepsMap();

        System.out.println("Finished in " + moveCounter + " moves.");
    }

    // for visualization (looks cool btw)
    private static void printHeightMap() {
        System.out.println();
        int eachRowSize = map.get(0).size();
        for (ArrayList<Tile> tiles : map) {
            for (int j = 0; j < eachRowSize; j++) {
                if (tiles.get(j) != null) {
                    int numToPrint = tiles.get(j).height;
                    if (numToPrint < 10) {
                        System.out.print(" " + numToPrint + " ");
                    } else {
                        System.out.print(tiles.get(j).height + " ");
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // for visualization (looks cool btw)
    private static void printStepsMap() {
        System.out.println();
        int eachRowSize = map.get(0).size();
        for (ArrayList<Tile> tiles : map) {
            for (int j = 0; j < eachRowSize; j++) {
                if (tiles.get(j) != null) {
                    if (tiles.get(j).isStart) {
                        System.out.print(" S  ");
                    } else {
                        if (tiles.get(j).distanceToFinal == null) {
                            System.out.print(" -  ");
                        } else {
                            int numToPrint = tiles.get(j).distanceToFinal;
                            if (numToPrint < 10) {
                                System.out.print(" " + numToPrint + "  ");
                            } else if (numToPrint < 100) {
                                System.out.print(" " + numToPrint + " ");
                            } else {
                                System.out.print(numToPrint + " ");
                            }
                        }
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void loadData() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();
                ArrayList<Tile> thisLineArrList = new ArrayList<>();
                lineCounter++;
                for (int i = 0; i < thisLine.length(); i++) {
                    if (Character.isUpperCase(thisLine.charAt(i))) {
                        if (thisLine.charAt(i) == 83) { // if "S"
                            startAndFinish[0] = new int[]{lineCounter, i};
                            thisLineArrList.add(new Tile(1, true));
                        } else { // if "E"
                            startAndFinish[1] = new int[]{lineCounter, i};
                            thisLineArrList.add(new Tile(26, 0));
                        }
                    } else {
                        thisLineArrList.add(new Tile(thisLine.charAt(i) - 96));
                    }
                }
                map.add(thisLineArrList);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // This includes: an array for all neighbours, an AL for the ones that can go to it and an AL for the ones that it can go to
    private static void provideTilesWithNeighbours() {
        for (int i = 0; i < map.size(); i++) {
            ArrayList<Tile> currentArrL = map.get(i);
            int currentArrLength = currentArrL.size();
            for (int j = 0; j < currentArrLength; j++) {
                Tile currentTile = map.get(i).get(j);
                if (currentTile != null) {
                    boolean topmost = false;
                    boolean bottommost = false;
                    boolean leftmost = false;
                    boolean rightmost = false;

                    if (i == 0) {
                        topmost = true;
                    } else if (i == map.size() - 1) {
                        bottommost = true;
                    }

                    if (j == 0) {
                        leftmost = true;
                    } else if (j == currentArrLength - 1) {
                        rightmost = true;
                    }

                    if (!topmost) currentTile.neighbours[0] = (map.get(i - 1).get(j));
                    if (!bottommost) currentTile.neighbours[1] = (map.get(i + 1).get(j));
                    if (!leftmost) currentTile.neighbours[2] = (map.get(i).get(j - 1));
                    if (!rightmost) currentTile.neighbours[3] = (map.get(i).get(j + 1));
                }
            }
        }

        provideTilesWithNeighboursTo();
        provideTilesWithNeighboursFrom();
    }

    private static void provideTilesWithNeighboursTo() {
        for (ArrayList<Tile> currentArrL : map) {
            for (Tile currentTile : currentArrL) {
                if (currentTile != null) {
                    for (int k = 0; k < currentTile.neighbours.length; k++) {
                        Tile neighbour = currentTile.neighbours[k];
                        if (neighbour != null) {
                            int heightDifference = neighbour.height - currentTile.height;
                            if (heightDifference <= 1) currentTile.neighboursTo.add(neighbour);
                        }
                    }
                }
            }
        }
    }

    private static void provideTilesWithNeighboursFrom() {
        for (ArrayList<Tile> currentArrL : map) {
            for (Tile currentTile : currentArrL) {
                if (currentTile != null) {
                    for (int k = 0; k < currentTile.neighbours.length; k++) {
                        Tile neighbour = currentTile.neighbours[k];
                        if (neighbour != null) {
                            int heightDifference = neighbour.height - currentTile.height;
                            if (heightDifference >= -1) currentTile.neighboursFrom.add(neighbour);
                        }
                    }
                }
            }
        }
    }

    // Calculates the distance from the final to every tile, including the start
    private static void calcDistanceToStart() {
        while (calculateNext.size() > calcNextCounter) {
            Tile tileToCalc = calculateNext.get(calcNextCounter++);
            addToCalcNext(tileToCalc);

            if (tileToCalc.isStart) {
                return;
            }

            for (int i = 0; i < tileToCalc.neighboursTo.size(); i++) {
                Tile nextWillBeAt = tileToCalc.neighboursTo.get(i);
                if (nextWillBeAt.distanceToFinal != null) {
                    if (tileToCalc.distanceToFinal == null || tileToCalc.distanceToFinal > nextWillBeAt.distanceToFinal) {
                        tileToCalc.distanceToFinal = nextWillBeAt.distanceToFinal + 1;
                        System.out.println(moveCounter++ + ": Calculated tile with height " + tileToCalc.height);
                        tileToCalc.explored = true;
                    }
                }
            }

        }
    }

    // Gathers the tiles to be calculated in the next turn
    private static void addToCalcNext(Tile tileToCalc) {
        int neighboursAmount = tileToCalc.neighboursFrom.size();
        for (int i = 0; i < neighboursAmount; i++) {
            if (!tileToCalc.neighboursFrom.get(i).explored && !calculateNext.contains(tileToCalc.neighboursFrom.get(i))) {
                calculateNext.add(tileToCalc.neighboursFrom.get(i));
            }
        }
    }
}

class Tile {
    int height;
    Tile[] neighbours = new Tile[4];
    ArrayList<Tile> neighboursTo = new ArrayList<>();
    ArrayList<Tile> neighboursFrom = new ArrayList<>();
    Integer distanceToFinal;
    boolean isStart = false;
    boolean explored = false;

    Tile(int height) {
        this.height = height;
    }

    Tile(int height, int distanceToFinal) {
        this.height = height;
        this.distanceToFinal = distanceToFinal;
    }

    Tile(int height, boolean isStart) {
        this.height = height;
        this.isStart = isStart;
    }
}