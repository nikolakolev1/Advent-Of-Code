package Days.Day5.Problems;

import Days.Day5.Main.GA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TSP {
    /**
     * An array of distances between each city
     * If there were three cities:
     * - TSPDistances[0] is the distance between city A and city B
     * - TSPDistances[1] is the distance between city A and city C
     * - TSPDistances[2] is the distance between city B and city C
     * and so on...
     */
    public static int[] costArray = new int[]{2, 7, 4, 11, 1, 17, 11, 3, 4, 9, 4, 13, 12, 3, 5, 7, 2, 14, 5, 4, 22, 15, 6, 8, 16, 10, 18, 1};
    public static String filename = "files/tspFiles/dantzig.tsp"; // groetschel.tsp & dantzig.tsp
    public static int[][] costMatrix; // Either use TspCostArray or TspFilename to create the TspCostMatrix
    protected static int SIZE;

    /**
     * Create a matrix for the Travelling Salesman Problem
     * Matrix would look like:
     * |AC BC CC|
     * |AB BB BC|
     * |AA AB AC|
     */
    public static void createMatrix() {
        int matrixSize = 3;
        while (true) {
            double trigSide = matrixSize - 1;
            double temp = trigSide * ((trigSide / 2) + 0.5);
            if (temp == costArray.length) break;
            else matrixSize++;
        }

        costMatrix = new int[matrixSize][matrixSize];

        int count = 0;

        for (int x = 0; x < matrixSize; x++) {
            for (int y = 0; y < matrixSize; y++) {
                if (x == y) costMatrix[x][y] = 0;
                else if (x < y) costMatrix[x][y] = costArray[count++];
                else costMatrix[x][y] = costMatrix[y][x];
            }
        }
    }

    // From other file
    public static void loadMatrix(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            int row = 0;
            int column = 0;
            boolean read = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DIMENSION")) {
                    String[] tokens = line.split(":");
                    SIZE = Integer.parseInt(tokens[1].trim());
                    costMatrix = new int[SIZE][SIZE];
                } else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                    String[] tokens = line.split(":");
                    if (tokens.length < 2 || !tokens[1].trim().equals("EXPLICIT")) {
                        throw new RuntimeException("Invalid EDGE_WEIGHT_TYPE: " + tokens[1]);
                    }
                } else if (line.startsWith("EDGE_WEIGHT_FORMAT")) {
                    String[] tokens = line.split(":");
                    if (tokens.length < 2 || !tokens[1].trim().equals("LOWER_DIAG_ROW")) {
                        throw new RuntimeException("Invalid EDGE_WEIGHT_FORMAT: " + tokens[1]);
                    }
                } else if (line.startsWith("EDGE_WEIGHT_SECTION")) {
                    read = true;
                } else if (line.startsWith("EOF")) {
                    break;
                } else if (read) {
                    String[] tokens = line.split("\\s");

                    for (String token : tokens) {
                        String v = token.trim();

                        if (!v.isEmpty()) {
                            int value = Integer.parseInt(token.trim());
                            costMatrix[row][column] = value;
                            column++;

                            if (value == 0) {
                                row++;
                                column = 0;
                            }
                        }
                    }
                }
            }

            reader.close();

            for (int i = 0; i < costMatrix.length; i++) {
                for (int j = (i + 1); j < costMatrix.length; j++) {
                    costMatrix[i][j] = costMatrix[j][i];
                }
            }

            GA.BITS = SIZE;
        } catch (IOException e) {
            throw new RuntimeException("Could not load file: " + filename, e);
        }
    }

    private static void printMatrix() {
        for (int y = 0; y < costMatrix.length; y++) {
            for (int[] tspMatrix : costMatrix) {
                System.out.print(tspMatrix[(costMatrix.length - 1) - y] + " ");
            }
            System.out.println();
        }
    }
}