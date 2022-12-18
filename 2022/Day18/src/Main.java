import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Integer[]> cubes;
    private static Integer[] smallestXYZ;
    private static int[] biggestXYZ;
    private static ArrayList<ArrayList<ArrayList<Boolean>>> xyzSpace;
    private static ArrayList<ArrayList<ArrayList<Boolean>>> xyzOuterAir;

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        loadData("input.txt");
        createSpace();
        insertCubesInSpace();

        System.out.println("=== Part 1 ===");
        System.out.print("Answer: ");
        System.out.println(calculateTotalArea());

        System.out.println();
    }

    private static void part2() {
        loadData("input.txt");

        createSpace();
        insertCubesInSpace();

        createOuterAir();
        insertAirInOuterAir();

        System.out.println("=== Part 2 ===");
        System.out.print("Answer: ");
        System.out.println(calculateTotalArea() - calculateInnerArea());
    }

    private static void loadData(String file) {
        cubes = new ArrayList<>();
        biggestXYZ = new int[3];
        smallestXYZ = new Integer[3];

        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String[] thisLine = myScanner.nextLine().split(",");

                Integer[] cube = new Integer[3];
                for (int i = 0; i < 3; i++) {
                    int thisXYZ = Integer.parseInt(thisLine[i]);

                    cube[i] = thisXYZ;
                    if (thisXYZ > biggestXYZ[i]) biggestXYZ[i] = thisXYZ;
                    if (smallestXYZ[i] == null || thisXYZ < smallestXYZ[i]) smallestXYZ[i] = thisXYZ;
                }

                cubes.add(cube);
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createSpace() {
        xyzSpace = new ArrayList<>();
        int biggestX = biggestXYZ[0], biggestY = biggestXYZ[1], biggestZ = biggestXYZ[2];

        for (int i = 0; i < biggestX + 1; i++) {
            ArrayList<ArrayList<Boolean>> thisXArray = new ArrayList<>();

            for (int j = 0; j < biggestY + 1; j++) {
                ArrayList<Boolean> thisYArray = new ArrayList<>();

                for (int k = 0; k < biggestZ + 1; k++) {
                    thisYArray.add(false);
                }
                thisXArray.add(thisYArray);
            }
            xyzSpace.add(thisXArray);
        }
    }

    private static void createOuterAir() {
        xyzOuterAir = new ArrayList<>();
        int biggestX = biggestXYZ[0], biggestY = biggestXYZ[1], biggestZ = biggestXYZ[2];

        for (int i = 0; i < biggestX + 1; i++) {
            ArrayList<ArrayList<Boolean>> thisXArray = new ArrayList<>();

            for (int j = 0; j < biggestY + 1; j++) {
                ArrayList<Boolean> thisYArray = new ArrayList<>();

                for (int k = 0; k < biggestZ + 1; k++) {
                    thisYArray.add(false);
                }
                thisXArray.add(thisYArray);
            }
            xyzOuterAir.add(thisXArray);
        }
    }

    private static void insertCubesInSpace() {
        for (Integer[] cube : cubes) {
            xyzSpace.get(cube[0]).get(cube[1]).set(cube[2], true);
        }
    }

    private static void insertAirInOuterAir() {
        for (int i = 0; i < biggestXYZ[0]; i++) {
            for (int j = 0; j < biggestXYZ[1]; j++) {
                if (!xyzSpace.get(i).get(j).get(0)) addTileToOuterAir(i, j, 0);
                if (!xyzSpace.get(i).get(j).get(biggestXYZ[2])) addTileToOuterAir(i, j, biggestXYZ[2]);
            }
        }

        for (int i = 0; i < biggestXYZ[0]; i++) {
            for (int j = 0; j < biggestXYZ[2]; j++) {
                if (!xyzSpace.get(i).get(0).get(j)) addTileToOuterAir(i, 0, j);
                if (!xyzSpace.get(i).get(biggestXYZ[1]).get(j)) addTileToOuterAir(i, biggestXYZ[1], j);
            }
        }

        for (int i = 0; i < biggestXYZ[1]; i++) {
            for (int j = 0; j < biggestXYZ[2]; j++) {
                if (!xyzSpace.get(0).get(i).get(j)) addTileToOuterAir(0, i, j);
                if (!xyzSpace.get(biggestXYZ[0]).get(i).get(j)) addTileToOuterAir(biggestXYZ[0], i, j);
            }
        }
    }

    private static void addTileToOuterAir(int x, int y, int z) {
        if (!(xyzOuterAir.get(x).get(y).get(z) || xyzSpace.get(x).get(y).get(z))) {
            xyzOuterAir.get(x).get(y).set(z, true);
            if (x - 1 > -1) addTileToOuterAir(x - 1, y, z);
            if (x + 1 < biggestXYZ[0] + 1) addTileToOuterAir(x + 1, y, z);

            if (y - 1 > -1) addTileToOuterAir(x, y - 1, z);
            if (y + 1 < biggestXYZ[1] + 1) addTileToOuterAir(x, y + 1, z);

            if (z - 1 > -1) addTileToOuterAir(x, y, z - 1);
            if (z + 1 < biggestXYZ[2] + 1) addTileToOuterAir(x, y, z + 1);
        }
    }

    private static int calculateTotalArea() {
        int surfaceArea = 0;

        for (Integer[] cube : cubes) {
            int thisX = cube[0], thisY = cube[1], thisZ = cube[2], thisCubeSurfaceArea = 6;

            if (thisZ + 1 <= biggestXYZ[2] && xyzSpace.get(thisX).get(thisY).get(thisZ + 1)) thisCubeSurfaceArea--;
            if (thisZ - 1 > -1 && xyzSpace.get(thisX).get(thisY).get(thisZ - 1)) thisCubeSurfaceArea--;
            if (thisY + 1 <= biggestXYZ[1] && xyzSpace.get(thisX).get(thisY + 1).get(thisZ)) thisCubeSurfaceArea--;
            if (thisY - 1 > -1 && xyzSpace.get(thisX).get(thisY - 1).get(thisZ)) thisCubeSurfaceArea--;
            if (thisX + 1 <= biggestXYZ[0] && xyzSpace.get(thisX + 1).get(thisY).get(thisZ)) thisCubeSurfaceArea--;
            if (thisX - 1 > -1 && xyzSpace.get(thisX - 1).get(thisY).get(thisZ)) thisCubeSurfaceArea--;

            surfaceArea += thisCubeSurfaceArea;
        }

        return surfaceArea;
    }

    private static int calculateInnerArea() {
        int innerArea = 0;

        for (int x = smallestXYZ[0] + 1; x < biggestXYZ[0]; x++) {
            for (int y = smallestXYZ[1] + 1; y < biggestXYZ[1]; y++) {
                for (int z = smallestXYZ[2] + 1; z < biggestXYZ[2]; z++) {
                    if (!(xyzSpace.get(x).get(y).get(z) || xyzOuterAir.get(x).get(y).get(z))) {
                        if (xyzSpace.get(x - 1).get(y).get(z)) innerArea++;
                        if (xyzSpace.get(x + 1).get(y).get(z)) innerArea++;
                        if (xyzSpace.get(x).get(y - 1).get(z)) innerArea++;
                        if (xyzSpace.get(x).get(y + 1).get(z)) innerArea++;
                        if (xyzSpace.get(x).get(y).get(z - 1)) innerArea++;
                        if (xyzSpace.get(x).get(y).get(z + 1)) innerArea++;
                    }
                }
            }
        }

        return innerArea;
    }
}