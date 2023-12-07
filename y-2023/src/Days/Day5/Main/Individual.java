package Days.Day5.Main;

import Days.Day5.Enums.INDIVIDUAL_TYPE;

/**
 * The individual class is the representation of a single individual in the population.
 */
public class Individual {
    public INDIVIDUAL_TYPE arrayType;
    public boolean[] individualB;
    public int[] individualI;
    public int length;
    public Double fitness;

    public Individual(int[] individual) {
        individualI = individual;
        this.length = individual.length;
        arrayType = GA.individualType;
    }

    public Individual copyItself() {
        return new Individual(individualI.clone());
    }

    public void print() {
        for (int bit : individualI) {
            System.out.print(bit);
        }
        System.out.println();
    }
}