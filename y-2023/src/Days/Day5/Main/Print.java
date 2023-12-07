package Days.Day5.Main;

/**
 * The print class is used to help visualize the results of the genetic algorithm.
 * It is not necessary for the algorithm to run, but it is useful to see the results.
 */
public class Print {
    // Main.Print the important stats of a particular run
    public static void shortStats() throws Exception {
        System.out.println("Average fitness last population: " + GA.averageFitness());
        if (!GA.elitism) System.out.println("Best fitness last population: " + GA.populationBestFitness());
        System.out.println("Best fitness entire run: " + GA.fitness(GA.bestIndividual_EntireRun));
        System.out.print("Best individual entire run: ");
        GA.bestIndividual_EntireRun.print();
    }

    public static void shortStats(int testNumber) throws Exception {
        System.out.println("--------- TEST " + testNumber + " ---------\n");
        shortStats();
        System.out.println();
    }

    // Main.Print all the stats of a particular run
    public static void detailedStats() throws Exception {
        shortStats();
        settings();
    }

    public static void detailedStats(int testNumber) throws Exception {
        shortStats(testNumber);
        settings();
        System.out.println();
    }

    // Main.Print the fitness of the best individual of a particular generation
    public static void generationStats(int generation) throws Exception {
        System.out.println("Generation " + generation + " best individual: " + (long) GA.fitness(GA.findBestIndividual()));
    }

    // Visualize the global population (print all individuals' genes)
    public static void population() {
        for (int i = 0; i < GA.population.length; i++) {
            System.out.print(i + 1 + ": ");
            GA.population[i].print();
        }
        System.out.println();
    }

    // Visualize a given population
    public static void population(Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            System.out.print(i + 1 + ": ");
            population[i].print();
        }
        System.out.println();
    }

    // Main.Print the settings of a particular run
    public static void settings() {
        System.out.print("This was a Main.GA using " + GA.selection + " selection and the '" + GA.fitnessFunc + "' fitness function");
        if (GA.elitism) System.out.println(", with Elitism turned on.");
        else System.out.println(".");
    }
}