package Days.Day5.Main;

import Days.Day5.Enums.INDIVIDUAL_TYPE;

import java.util.Random;

/**
 * The individual class is the representation of a single individual in the population.
 */
public class Individual {
    public INDIVIDUAL_TYPE arrayType;
    public boolean[] individualB;
    public int[] individualI;
    public int length;
    public Double fitness;

    public Individual(boolean[] individual) {
        individualB = individual;
        this.length = individual.length;
        arrayType = INDIVIDUAL_TYPE.boolArray;
    }

    public Individual(int[] individual) {
        individualI = individual;
        this.length = individual.length;
        arrayType = GA.individualType;
    }

    // This constructor doesn't work for the Problems.TSP problem
    public Individual(INDIVIDUAL_TYPE arrayType, int BITS) {
        Random rand = new Random();

        this.arrayType = arrayType;
        length = BITS;
        switch (arrayType) {
            case boolArray -> {
                individualB = new boolean[BITS];
                for (int i = 0; i < BITS; i++) {
                    individualB[i] = rand.nextBoolean();
                }
            }
            case intArray -> {
                individualI = new int[BITS];
                for (int i = 0; i < BITS; i++) {
                    individualI[i] = rand.nextInt();
                }
            }
            default -> {

            }
        }
    }

    public Individual copyItself() {
        switch (arrayType) {
            case boolArray -> {
                return new Individual(individualB.clone());
            }
            case intArray, tspIntArray, fttxIntArray, aocIntArray -> {
                return new Individual(individualI.clone());
            }
            default -> throw new IllegalStateException("Unexpected value: " + arrayType);
        }
    }

    public void print() {
        switch (arrayType) {
            case boolArray -> {
                for (boolean bit : individualB) {
                    System.out.print(bit ? "1" : "0");
                }
                System.out.println();
            }
            case intArray, tspIntArray, fttxIntArray -> {
                for (int bit : individualI) {
                    System.out.print(bit + " ");
                }
                System.out.println();
            }
            case aocIntArray -> {
                for (int bit : individualI) {
                    System.out.print(bit);
                }
                System.out.println();
            }
            default -> throw new IllegalStateException("Unexpected value: " + arrayType);
        }
    }
}