package Days.Day5.Main;

/**
 * The print class is used to help visualize the results of the genetic algorithm.
 * It is not necessary for the algorithm to run, but it is useful to see the results.
 */
public class Print {
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

    // Main.Print the settings of a particular run
    public static void settings() {
        System.out.print("This was a Main.GA using " + GA.selection + " selection and the '" + GA.fitnessFunc + "' fitness function");
        if (GA.elitism) System.out.println(", with Elitism turned on.");
        else System.out.println(".");
    }
}