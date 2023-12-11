package Days.Day5.Main;

/**
 * The individual class is the representation of a single individual in the population.
 */
public class Individual {
    public int[] individualI;
    public Double fitness;

    public Individual(int[] individual) {
        individualI = individual;
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