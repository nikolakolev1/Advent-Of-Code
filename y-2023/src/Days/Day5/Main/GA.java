package Days.Day5.Main;

import Days.Day5.Enums.*;
import Days.Day5.Problems.*;

import java.util.*;
import java.util.stream.IntStream;

// Author: Nikola Kolev
// Date: 05.11.2023
// Version: 3.0

/**
 * <p>
 * To be fixed || implemented in next version:
 * <ul>
 *     <li>Implement using a termination condition</li>
 *     <li>Finish nPointCrossover, linking it with the nPointCrossoverPoints global var</li>
 *     <li>Implement MUTATION of type Tsp, which has 50% change of performing Exchange or Inversion mutation</li>
 *     <li>Work on preventing premature convergence (check out: preselection, crowding, fitness sharing, incest prevention)</li>
 *     <li>Incest prevention can be implemented through a variable parents[] on an Main.Individual level</li>
 *     <li>Crossover Uniform Simple doesn't give good results for some reason - debug if there is time</li>
 *     <li>Clean up code (refactor, rename methods and vars, add comments, remove junk)</li>
 * </ul>
 */
public class GA {
    // ------------------------------------- Switches -------------------------------------
    protected static INDIVIDUAL_TYPE individualType;
    protected static SELECTION selection;
    protected static FITNESS_FUNC fitnessFunc;
    protected static CROSSOVER crossover;
    protected static MUTATION mutation;
    protected static MIN_MAX minOrMax;


    // ------------------------------------- Elitism -------------------------------------
    protected static boolean elitism;
    private static ArrayList<Individual> oldPopulationSorted;
    private static final int eliteIndividualsCount = 2;


    // ------------------------------------- Tournament sel - Ensure all selected become parents -------------------------------------
    private static boolean everyoneWasParent = false;
    private static HashSet<Integer> parents = new HashSet<>();


    // ------------------------------------- Settings -------------------------------------
    /**
     * BITS - number of bits in an individual
     * POPULATION_SIZE - number of individuals in the population (MUST BE DIVISIBLE BY 4)
     * TOURNAMENT_SIZE (often shown as 'k')- number of individuals in a tournament
     * <ul>
     *     <li> Used in tournament selection </li>
     *     <li> Must be >= 1 and <= POPULATION_SIZE (preferably neither, but in between) </li>
     * </ul>
     * MAX_GENERATION - number of generations the algorithm will run for
     */
    public static int BITS;
    protected static int POPULATION_SIZE;
    protected static int TOURNAMENT_SIZE;
    protected static int MAX_GENERATION;
    protected static double CROSSOVER_PROBABILITY, MUTATION_PROBABILITY;


    // ------------------------------------- Global population -------------------------------------
    protected static Individual[] population;
    private static double[] fitness;
    public static ArrayList<Double> prejudice;
    protected static Individual bestIndividual_EntireRun;


    // ------------------------------------- Other -------------------------------------
    private static int nPointCrossoverPoints;
    private static final double terminationConditionFitness = -1;


    // ------------------------------------- Methods -------------------------------------
    public static void main(String[] args) {
        try {
            for (int testNo = 0; testNo < 1; testNo++) {
                // Set all the switches and settings for the Main.GA to run
                // (I suggest using presets or the setSwitches() and setSettings() methods)
                Presets.preset("AocTorAocFtxAriEltAoc"); // BolTorMboSinUnfEltMed | BolTorLboSinUnfEltMed | BolTorQdeSinUnfEltQde | TspTorTspPmxInvEltTsp | FtxTorNvpFtxAriEltFtx

                // Main.Check if everything is set correctly
                Check.checkEverything();

                // The main algorithm
                initialise();
                evaluate();

                runGA();

                // Must do this at the end of each test
                Print.shortStats(testNo + 1);
                resetGlobals();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static long part2() {
        try {
            Presets.preset("AocTorAocFtxAriEltAoc"); // BolTorMboSinUnfEltMed | BolTorLboSinUnfEltMed | BolTorQdeSinUnfEltQde | TspTorTspPmxInvEltTsp | FtxTorNvpFtxAriEltFtx

            // Main.Check if everything is set correctly
            Check.checkEverything();

            // The main algorithm
            initialise();
            evaluate();

            runGA();

            return (long) GA.fitness(GA.bestIndividual_EntireRun);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Run the Main.GA for a given number of generations
     * 1. Selection
     * 2. Crossover
     * 3. Mutation
     * 4. Elitism
     * 5. Evaluate
     * 6. Termination check
     */
    private static void runGA() throws Exception {
        for (int gen = 0; gen < MAX_GENERATION; gen++) {
            Individual[] selected = selection(); // selection
            population = evolution(selected); // crossover and mutation

            if (elitism) reintroduceElite(); // elitism

            evaluate();

            // Must do this at the end of each generation
            recordBestIndividual_EntireRun();
//            Print.generationStats(gen + 1); // if (fitnessFunc != FITNESS_FUNC.Problems.SequentialCovering) Main.Print.generationStats(gen + 1);

            // Termination condition
            if (terminationConditionMet()) break;

            // PLAYING AROUND: decrease the crossover and mutation probabilities
//            changeCrossoverMutationProbability(gen);
        }
    }

    // For debugging purposes
    private static void test() throws Exception {
        Presets.preset("FtxTorNvpFtxAriEltMaxFtx");

        double fitness1 = FTTx.npv(new int[]{1, 1, 3, 2, 2, 2, 1, 2, 3, 3, 1, 1, 2, 3, 1, 2, 3, 2, 2, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 0, 2, 3, 0, 1, 3, 2, 1, 0, 1, 1, 0, 2, 2, 2, 2, 1, 0, 1, 2, 2, 1, 2, 1, 2, 2, 2, 2, 0, 1, 2, 3, 2, 2, 1, 3, 0, 2, 1, 2, 2, 2, 2, 2, 0, 2, 1, 1, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 2, 0, 1, 2, 1, 2, 2, 2, 2, 1, 2, 0, 2, 2, 1, 2, 2, 2, 1, 1, 0, 1, 1, 1, 2, 2, 2, 2, 1, 0, 0, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 1, 2, 2, 1, 15, 2, 1, 2, 2, 2, 0, 1, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 0, 1, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 1, 2, 2, 2, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 2, 0, 1, 0, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 2, 2, 1, 0, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 1, 1, 0, 2, 1, 0, 2, 2, 1, 1, 2, 2, 1, 2, 1, 1, 1, 2, 2, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 1, 1, 2, 2, 0, 2, 2, 1, 2, 0, 1, 1, 2, 2, 2, 0, 0, 2, 2, 1, 2, 2, 0, 1, 1, 2});
        double fitness2 = FTTx.npv(new int[]{1, 1, 3, 2, 2, 2, 1, 2, 3, 3, 1, 1, 2, 3, 1, 2, 3, 2, 2, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 0, 2, 3, 0, 1, 3, 2, 1, 0, 1, 1, 0, 2, 2, 2, 2, 1, 0, 1, 2, 2, 1, 2, 1, 2, 2, 2, 2, 0, 1, 2, 3, 2, 2, 1, 3, 0, 2, 1, 2, 2, 2, 2, 2, 0, 2, 1, 1, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 2, 0, 1, 2, 1, 2, 2, 2, 2, 1, 2, 0, 2, 2, 1, 2, 2, 2, 1, 1, 0, 1, 1, 1, 2, 2, 2, 2, 1, 0, 0, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 1, 2, 2, 1, 0, 2, 1, 2, 2, 2, 0, 1, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 0, 1, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 1, 2, 2, 2, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 2, 0, 1, 0, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 2, 2, 1, 0, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 1, 1, 0, 2, 1, 0, 2, 2, 1, 1, 2, 2, 1, 2, 1, 1, 1, 2, 2, 2, 1, 0, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 1, 1, 2, 2, 0, 2, 2, 1, 2, 0, 1, 1, 2, 2, 2, 0, 0, 2, 2, 1, 2, 2, 0, 1, 1, 2});
        double fitness3 = FTTx.npv(new int[]{1, 1, 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2, 1, 0, 2, 2, 0, 1, 2, 1, 1, 0, 1, 2, 0, 2, 2, 2, 2, 1, 0, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 0, 1, 1, 2, 2, 1, 1, 2, 0, 2, 1, 2, 2, 2, 2, 1, 0, 2, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 0, 1, 2, 1, 2, 2, 1, 2, 1, 2, 0, 3, 2, 1, 2, 1, 1, 2, 2, 0, 2, 1, 1, 2, 2, 2, 2, 1, 0, 0, 2, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 1, 2, 1, 0, 2, 1, 3, 2, 2, 0, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 1, 3, 2, 1, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 2, 6, 1, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 0, 1, 2, 2, 2, 2, 2, 2, 1, 1, 0, 2, 0, 1, 7, 2, 1, 2, 1, 2, 1, 1, 2, 2, 1, 2, 2, 2, 1, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 0, 2, 1, 0, 2, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 2, 1, 0, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 0, 2, 1, 2, 2, 0, 1, 1, 2, 2, 1, 0, 0, 2, 2, 2, 2, 2, 0, 1, 1, 2});
        double fitness4 = FTTx.npv(new int[]{1, 1, 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 3, 1, 2, 2, 2, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 11, 1, 2, 0, 1, 2, 1, 1, 12, 1, 2, 0, 2, 2, 2, 2, 1, 13, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 0, 1, 1, 2, 2, 1, 1, 2, 0, 2, 1, 2, 2, 2, 2, 1, 0, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 1, 2, 2, 2, 1, 2, 1, 2, 14, 1, 2, 1, 2, 1, 1, 2, 1, 2, 0, 3, 1, 1, 2, 2, 2, 2, 2, 12, 2, 2, 1, 2, 2, 1, 2, 1, 0, 14, 2, 5, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 2, 1, 14, 2, 1, 12, 1, 2, 6, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 11, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 3, 1, 2, 2, 7, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 0, 1, 2, 2, 2, 2, 2, 2, 1, 1, 0, 2, 0, 1, 6, 2, 1, 2, 1, 2, 1, 1, 2, 2, 1, 2, 2, 3, 1, 0, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 1, 10, 2, 1, 0, 2, 2, 1, 1, 2, 2, 1, 2, 1, 1, 1, 1, 2, 2, 1, 0, 2, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 2, 13, 2, 1, 2, 2, 0, 1, 1, 2, 2, 1, 12, 11, 2, 2, 2, 2, 2, 15, 2, 1, 2});
        double fitness5 = FTTx.npv(new int[]{1, 1, 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 3, 1, 2, 2, 2, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 0, 1, 2, 0, 1, 2, 1, 1, 0, 1, 2, 0, 2, 2, 2, 2, 1, 0, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 0, 1, 1, 2, 2, 1, 1, 2, 0, 2, 1, 2, 2, 2, 2, 1, 0, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 1, 2, 2, 2, 1, 2, 1, 2, 0, 1, 2, 1, 2, 1, 1, 2, 1, 2, 0, 3, 1, 1, 2, 2, 2, 2, 2, 0, 2, 2, 1, 2, 2, 1, 2, 1, 0, 0, 2, 5, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 2, 1, 0, 2, 1, 0, 1, 2, 6, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 0, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 3, 1, 2, 2, 7, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 0, 1, 2, 2, 2, 2, 2, 2, 1, 1, 0, 2, 0, 1, 6, 2, 1, 2, 1, 2, 1, 1, 2, 2, 1, 2, 2, 3, 1, 0, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 1, 0, 2, 1, 0, 2, 2, 1, 1, 2, 2, 1, 2, 1, 1, 1, 1, 2, 2, 1, 0, 2, 2, 2, 1, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 2, 0, 2, 1, 2, 2, 0, 1, 1, 2, 2, 1, 0, 0, 2, 2, 2, 2, 2, 0, 2, 1, 2});

        System.out.println(fitness1 + "\n" + fitness2 + "\n" + fitness3 + "\n" + fitness4 + "\n" + fitness5);
    }


    // ------------------------------------- Switches and settings -------------------------------------
    protected static void setSwitches(INDIVIDUAL_TYPE individualType,
                                      SELECTION selection,
                                      FITNESS_FUNC fitnessFunction,
                                      CROSSOVER crossover,
                                      MUTATION mutation,
                                      boolean elitism) throws Exception {
        GA.individualType = individualType;
        GA.selection = selection;
        GA.fitnessFunc = fitnessFunction;
        GA.crossover = crossover;
        GA.mutation = mutation;
        GA.elitism = elitism;
        GA.minOrMax = fitnessFunction.getMinMax();

        // variables that must be set, depending on the switches
        if (fitnessFunction == FITNESS_FUNC.QuadEquationBoolArray) {
            if (Equation.valuesAtPoints == null) Equation.populateValuesAtPoints();
        } else if (fitnessFunc == FITNESS_FUNC.Tsp) {
            if (TSP.costMatrix == null) {
                if (TSP.filename != null) TSP.loadMatrix(TSP.filename);
                else TSP.createMatrix();
            }
        } else if (fitnessFunc == FITNESS_FUNC.FTTxNVP) {
            if (FTTx.households == null) {
                if (FTTx.parametersFilename != null) {
                    FTTx.loadParameters();
                    FTTx.loadHouseholds();
                } else {
                    FTTx.defaultParams();
                }
            }
        } else if (fitnessFunction == FITNESS_FUNC.SequentialCovering) {
            if (SequentialCovering.filename == null) throw new Exception("Sequential Covering filename not set");
            else SequentialCovering.setTrainingData();
        } else if (fitnessFunction == FITNESS_FUNC.AocDay5) {
            if (AocDay5.filename == null) throw new Exception("AocDay5 filename not set");
            else AocDay5.loadData(AocDay5.filename);
        }
    }

    protected static void setSettings(int BITS,
                                      int POPULATION_SIZE,
                                      int TOURNAMENT_SIZE,
                                      int MAX_GENERATION,
                                      double CROSSOVER_PROBABILITY,
                                      double MUTATION_PROBABILITY) {
        GA.BITS = BITS;
        GA.POPULATION_SIZE = POPULATION_SIZE;
        GA.TOURNAMENT_SIZE = TOURNAMENT_SIZE;
        GA.MAX_GENERATION = MAX_GENERATION;
        GA.CROSSOVER_PROBABILITY = CROSSOVER_PROBABILITY;
        GA.MUTATION_PROBABILITY = MUTATION_PROBABILITY;
    }


    // ------------------------------------- Initial random population -------------------------------------

    /**
     * Initialise the population with random individuals and record the best individual in it
     */
    private static void initialise() throws Exception {
        population = randomPopulation(); // create a random population
        fitness = new double[POPULATION_SIZE]; // initialise the fitness array

        bestIndividual_EntireRun = population[0]; // initialise the best individual

        if (elitism) oldPopulationSorted = new ArrayList<>(); // keep track of the best parents if using elitism
    }

    private static Individual[] randomPopulation() throws Exception {
        switch (individualType) {
            case boolArray, intArray -> {
                return randPopulation_Normal();
            }
            case tspIntArray -> {
                return randPopulation_Tsp();
            }
            case fttxIntArray -> {
                return randPopulation_FTTx();
            }
            case aocIntArray -> {
                return randPopulation_AOC();
            }
            default -> throw new Exception("Select a valid individual type");
        }
    }

    private static Individual[] randPopulation_Normal() {
        Individual[] population = new Individual[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            // create a random individual at index i
            population[i] = new Individual(individualType, BITS);
        }

        return population;
    }

    private static Individual[] randPopulation_Tsp() {
        Individual[] population = new Individual[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            // create an ArrayList with all the cities (in order)
            ArrayList<Integer> individualI = new ArrayList<>();

            int city = 0;
            while (city < BITS) individualI.add(city++);

            // shuffle the ArrayList
            Collections.shuffle(individualI);

            // convert the ArrayList to int[]
            int[] individual = new int[BITS];
            city = 0;
            while (city < BITS) individual[city] = individualI.get(city++);

            population[i] = new Individual(individual);
        }

        return population;
    }

    private static Individual[] randPopulation_FTTx() {
        Individual[] population = new Individual[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] individualI = new int[FTTx.noOfAreas];

            for (int j = 0; j < individualI.length; j++) {
                individualI[j] = (int) (Math.random() * (FTTx.maxRolloutPeriod + 1));
            }

            population[i] = new Individual(individualI);
        }

        return population;
    }

    private static Individual[] randPopulation_AOC() {
        Individual[] population = new Individual[POPULATION_SIZE];

        Random random = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int rangeIndex = random.nextInt(AocDay5.seedRanges.size());

            long start = AocDay5.seedRanges.get(rangeIndex).seedRangeStart();
            long finish = start + AocDay5.seedRanges.get(rangeIndex).seedRangeLength();

            long individual = random.nextLong(start, finish);

            population[i] = new Individual(AocDay5.convertLongToIntArr(individual, BITS));
        }

        return population;
    }


    // ------------------------------------- Fitness functions - Evaluate -------------------------------------

    /**
     * Evaluate the fitness of each individual in the population
     */
    private static void evaluate() throws Exception {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitness[i] = fitness(population[i]);
            population[i].fitness = fitness[i];
        }

        if (elitism) recordElite();
    }

    protected static double fitness(Individual individual) throws Exception {
        switch (fitnessFunc) {
            case MostBitsOn -> {
                return fitness_MostGenesOn(individual.individualB);
            }
            case LeastBitsOn -> {
                return fitness_LeastGenesOn(individual.individualB);
            }
            case QuadEquationBoolArray -> {
                return fitness_QuadraticEquation(individual.individualB);
            }
            case Tsp -> {
                return fitness_Tsp(individual.individualI);
            }
            case FTTxNVP -> {
                return fitness_FTTx(individual.individualI);
            }
            case SequentialCovering -> {
                return fitness_SequentialCovering(individual.individualB);
            }
            case AocDay5 -> {
                return fitness_AocDay5(individual.individualI);
            }
            default -> throw new Exception("Select a valid fitness function");
        }
    }

    private static double fitness_MostGenesOn(boolean[] individual) {
        double fitness = 0.0;

        for (boolean bit : individual) {
            if (bit) fitness++;
        }

        return fitness;
    }

    private static double fitness_LeastGenesOn(boolean[] individual) {
        double fitness = 0.0;

        for (boolean bit : individual) {
            if (!bit) fitness++;
        }

        return fitness;
    }

    private static double fitness_QuadraticEquation(boolean[] individual) throws Exception {
        if (!Equation.evaluateParameters())
            throw new Exception("Set appropriate parameters for the quadratic equation");

        int numberLengthBits = BITS / Equation.NUMBERS_TO_FIND.length;
        int[] numbers = new int[Equation.NUMBERS_TO_FIND.length];

        for (int i = 0; i < Equation.NUMBERS_TO_FIND.length; i++) {
            int from = i * numberLengthBits;
            int to = from + numberLengthBits;
            numbers[i] = Equation.binaryToDecimal(Arrays.copyOfRange(individual, from, to));
        }

        double difference = 0.0;

        HashMap<Double, Double> results = Equation.quadraticEquationSolver(numbers);
        for (Double key : results.keySet()) {
            difference += Math.abs(Equation.valuesAtPoints.get(key) - results.get(key));
        }

        return difference;
    }

    private static double fitness_Tsp(int[] individual) {
        double fitness = 0.0;

        for (int i = 0; i < individual.length - 1; i++) {
            fitness += TSP.costMatrix[individual[i]][individual[i + 1]];
        }
        fitness += TSP.costMatrix[individual[individual.length - 1]][individual[0]];

        return fitness;
    }

    private static double fitness_FTTx(int[] individual) {
        return FTTx.npv(individual);
    }

    private static double fitness_SequentialCovering(boolean[] individual) {
        return SequentialCovering.fitness(individual);
    }

    private static double fitness_AocDay5(int[] individual) {
        long l = AocDay5.convertIntArrToLong(individual);

        if (AocDay5.checkRangesContain(l)) return AocDay5.mapSeedToLocation(l);
        else return Double.MAX_VALUE;
    }

    private static double totalFitness() {
        double totalFitness = 0.0;

        for (double fitness : fitness) {
            totalFitness += fitness;
        }

        return totalFitness;
    }

    protected static double averageFitness() {
        return totalFitness() / POPULATION_SIZE;
    }

    protected static double populationBestFitness() {
        double bestFitness = fitness[0];

        for (double fitness : fitness) {
            if (compareFitness(bestFitness, fitness)) {
                bestFitness = fitness;
            }
        }

        return bestFitness;
    }


    // ------------------------------------- Selection -------------------------------------
    private static Individual[] selection() throws Exception {
        switch (selection) {
            case Roulette -> {
                rouletteSelect();
                return population;
            }
            case Tournament -> {
                return tournamentSelect();
            }
            default -> throw new Exception("Select a valid selection method");
        }
    }

    // only works with positive fitness (and maximisation problems)
    private static void rouletteSelect() throws Exception {
        prejudice = new ArrayList<>();

        double cumulative = 0.0;
        double totalFitness = totalFitness();

        for (Individual individual : population) {
            cumulative += fitness(individual) / totalFitness;
            prejudice.add(cumulative);
        }
    }

    private static Individual[] tournamentSelect() {
        ArrayList<Individual> selectedIndividuals = new ArrayList<>();

        // create a shuffled array of indexes
        int[] shuffledIndexes = IntStream.range(0, POPULATION_SIZE).toArray();
        Random rand = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int randIndex = rand.nextInt(POPULATION_SIZE);
            int temp = shuffledIndexes[i];
            shuffledIndexes[i] = shuffledIndexes[randIndex];
            shuffledIndexes[randIndex] = temp;
        }

        // divide into groups
        for (int i = 0; i < population.length; i += TOURNAMENT_SIZE) {
            int[] group = Arrays.copyOfRange(shuffledIndexes, i, i + TOURNAMENT_SIZE);

            Individual mostFitInGroup = findBestIndividual(group);
            selectedIndividuals.add(mostFitInGroup.copyItself());
        }

        return selectedIndividuals.toArray(new Individual[0]);
    }

    // using the fitness[] array
    private static Individual findBestIndividual(int from, int to) {
        int bestInd_index = from;

        for (int i = from + 1; i < to; i++) {
            bestInd_index = compareIndividuals(bestInd_index, i);
        }

        return population[bestInd_index];
    }

    // using the fitness[] array
    private static Individual findBestIndividual(int[] indexes) {
        int bestInd_index = indexes[0];

        for (int i = 1; i < indexes.length; i++) {
            bestInd_index = compareIndividuals(bestInd_index, indexes[i]);
        }

        return population[bestInd_index];
    }

    // using the fitness[] array
    protected static Individual findBestIndividual() {
        return findBestIndividual(0, POPULATION_SIZE);
    }


    // ------------------------------------- Population evolution -------------------------------------

    /**
     * Crossover and mutation
     */
    private static Individual[] evolution(Individual[] parentPopulation) throws Exception {
        Individual[] newPopulation = crossover(parentPopulation);
        mutation(newPopulation);

        return newPopulation;
    }

    /**
     * The getParent() method returns a parent based on a prejudice
     */
    private static Individual getParent(Individual[] parentPopulation) throws Exception {
        if (selection == SELECTION.Tournament) {
            return getParent_TournamentSel(parentPopulation);
        } else {
            return getParent_Prejudice(parentPopulation);
        }
    }

    private static Individual getParent_TournamentSel(Individual[] parentPopulation) {
        int randIndex = new Random().nextInt(parentPopulation.length);

        if (!everyoneWasParent) {
            if (!parents.contains(randIndex)) {
                parents.add(randIndex);
                return parentPopulation[randIndex];
            }

            int closestIndexUp = closestIndexUp(randIndex, parentPopulation.length);
            if (closestIndexUp != -1) {
                parents.add(closestIndexUp);
                return parentPopulation[closestIndexUp];
            }

            int closestIndexDown = closestIndexDown(randIndex);
            if (closestIndexDown != -1) {
                parents.add(closestIndexDown);
                return parentPopulation[closestIndexDown];
            }

            everyoneWasParent = true;
        }

        return parentPopulation[randIndex];
    }

    private static int closestIndexUp(int index, int upperBound) {
        while (index < upperBound) {
            if (parents.contains(index)) index++;
            else return index;
        }

        return -1; // if the index is out of bounds
    }

    private static int closestIndexDown(int index) {
        while (index >= 0) {
            if (parents.contains(index)) index--;
            else return index;
        }

        return -1; // if the index is out of bounds
    }

    private static Individual getParent_Prejudice(Individual[] parentPopulation) throws Exception {
        double rand = Math.random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (rand < prejudice.get(i)) {
                return parentPopulation[i];
            }
        }

        throw new Exception("This line should not be reached, check for errors");
    }

    private static Individual[] crossover(Individual[] parentPopulation) throws Exception {
        Individual[] newPopulation = new Individual[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual parent1 = getParent(parentPopulation);
            Individual parent2 = getParent(parentPopulation);

            // decrease premature convergence
            while (parent1 == parent2) parent2 = getParent(parentPopulation); // make sure the parents are different

            Individual[] offsprings = crossoverIndividuals(parent1, parent2);

            newPopulation[i] = offsprings[0];
            newPopulation[++i] = offsprings[1];
        }

        // reset the parents HashSet if using tournament selection
        if (SELECTION.Tournament == selection) {
            everyoneWasParent = false;
            parents = new HashSet<>();
        }

        return newPopulation;
    }

    private static Individual[] crossoverIndividuals(Individual parent1, Individual parent2) throws Exception {
        Individual p1copy = parent1.copyItself();
        Individual p2copy = parent2.copyItself();

        switch (crossover) {
            case SinglePoint_Simple -> {
                return crossover_SinglePoint_Simple(p1copy, p2copy);
            }
            case nPoint_Simple -> {
                return crossover_nPoint_Simple(p1copy, p2copy);
            }
            case Uniform_Simple -> {
                return crossover_Uniform_Simple(p1copy, p2copy);
            }
            case PMX_Tsp -> {
                return crossover_PartiallyMapped_Tsp(p1copy, p2copy);
            }
            case SinglePoint_FTTx -> {
                return crossover_SinglePoint_FTTx(p1copy, p2copy);
            }
            default -> throw new Exception("Select a valid crossover method");
        }
    }

    private static Individual[] crossover_SinglePoint_Simple(Individual parent1_Copy, Individual parent2_Copy) {
        if (Math.random() < CROSSOVER_PROBABILITY) {
            int crossoverPoint = (int) (Math.random() * BITS);
            boolean[] temp = parent1_Copy.individualB.clone();

            System.arraycopy(parent2_Copy.individualB, crossoverPoint, parent1_Copy.individualB, crossoverPoint, BITS - crossoverPoint);
            System.arraycopy(temp, crossoverPoint, parent2_Copy.individualB, crossoverPoint, BITS - crossoverPoint);
        }

        return new Individual[]{parent1_Copy, parent2_Copy};
    }

    private static Individual[] crossover_nPoint_Simple(Individual parent1_Copy, Individual parent2_Copy) {
        ArrayList<Integer> points = new ArrayList<>();
        Random rand = new Random();

        boolean[] parent1_array = parent1_Copy.individualB;
        boolean[] parent2_array = parent2_Copy.individualB;
        boolean[] temp = parent1_array.clone();

        points.add(rand.nextInt(0, BITS));
        while (points.size() < nPointCrossoverPoints) {
            int randomPoint = rand.nextInt(0, BITS);
            if (!points.contains(randomPoint)) points.add(randomPoint);
        }

        for (int point = 0; point < nPointCrossoverPoints; point++) {
            int from = points.get(point);

            if (point != nPointCrossoverPoints - 1) {
                int to = points.get(point + 1);

                System.arraycopy(parent2_array, from, parent1_array, from, (to - from));
                System.arraycopy(temp, from, parent1_array, from, (to - from));
            } else {
                System.arraycopy(parent2_array, from, parent1_array, from, (BITS - from));
                System.arraycopy(temp, from, parent1_array, from, (BITS - from));
            }
        }

        return new Individual[]{parent1_Copy, parent2_Copy};
    }

    private static Individual[] crossover_Uniform_Simple(Individual parent1_Copy, Individual parent2_Copy) {
        boolean[] parent1_array = parent1_Copy.individualB;
        boolean[] parent2_array = parent2_Copy.individualB;
        boolean[] temp = parent1_Copy.individualB.clone();

        for (int bit = 0; bit < BITS; bit++) {
            if (Math.random() < CROSSOVER_PROBABILITY) {
                System.arraycopy(parent2_array, bit, parent1_array, bit, 1);
                System.arraycopy(temp, bit, parent1_array, bit, 1);
            }
        }

        return new Individual[]{parent1_Copy, parent2_Copy};
    }

    private static Individual[] crossover_PartiallyMapped_Tsp(Individual parent1_Copy, Individual parent2_Copy) {
        int[] parent1 = parent1_Copy.individualI;
        int[] parent2 = parent2_Copy.individualI;

        int[] offspring1 = parent1.clone();
        int[] offspring2 = parent2.clone();

        int crossoverPoint1 = (int) (Math.random() * BITS);
        int crossoverPoint2 = (int) (Math.random() * BITS);

        // make sure crossoverPoint1 <= crossoverPoint2
        if (crossoverPoint1 > crossoverPoint2) {
            int temp = crossoverPoint1;
            crossoverPoint1 = crossoverPoint2;
            crossoverPoint2 = temp;
        }

        // record who is given what, to fix the duplicates later
        ArrayList<Integer> parent1GaveOffspring2 = new ArrayList<>();
        ArrayList<Integer> parent2GaveOffspring1 = new ArrayList<>();

        // crossover
        for (int i = crossoverPoint1; i < crossoverPoint2 + 1; i++) {
            offspring1[i] = parent2[i];
            offspring2[i] = parent1[i];

            parent1GaveOffspring2.add(parent1[i]);
            parent2GaveOffspring1.add(parent2[i]);
        }

        // fix duplicate genes
        handleDuplicateGenes(offspring1, parent1GaveOffspring2, parent2GaveOffspring1, crossoverPoint1, crossoverPoint2);
        handleDuplicateGenes(offspring2, parent2GaveOffspring1, parent1GaveOffspring2, crossoverPoint1, crossoverPoint2);

        return new Individual[]{new Individual(offspring1), new Individual(offspring2)};
    }

    private static void handleDuplicateGenes(int[] offspring, ArrayList<Integer> youGave, ArrayList<Integer> youReceived, int from, int to) {
        // fix duplicate genes in the beginning of the offspring DNA
        for (int i = 0; i < from; i++) {
            if (youReceived.contains(offspring[i])) {
                offspring[i] = youGave.get(youReceived.indexOf(offspring[i]));
                i = -1;
            }
        }

        // fix duplicate genes after the second crossover point
        for (int i = to + 1; i < offspring.length; i++) {
            if (youReceived.contains(offspring[i])) {
                offspring[i] = youGave.get(youReceived.indexOf(offspring[i]));
                i = to;
            }
        }
    }

    private static Individual[] crossover_SinglePoint_FTTx(Individual parent1_Copy, Individual parent2_Copy) {
        if (Math.random() < CROSSOVER_PROBABILITY) {
            int crossoverPoint = (int) (Math.random() * BITS);
            int[] temp = parent1_Copy.individualI.clone();

            System.arraycopy(parent2_Copy.individualI, crossoverPoint, parent1_Copy.individualI, crossoverPoint, BITS - crossoverPoint);
            System.arraycopy(temp, crossoverPoint, parent2_Copy.individualI, crossoverPoint, BITS - crossoverPoint);
        }

        return new Individual[]{parent1_Copy, parent2_Copy};
    }

    private static void mutation(Individual[] offspringPopulation) {
        for (Individual individual : offspringPopulation) {
            if (mutation == MUTATION.Uniform_Bool) mutateUniform(individual.individualB);
            else if (Math.random() < MUTATION_PROBABILITY) {
                switch (mutation) {
                    case SinglePoint_Bool -> mutationSinglePoint(individual.individualB);
                    case Exchange_Tsp -> mutateExchange(individual.individualI);
                    case Inversion_Tsp -> mutateInversion(individual.individualI);
                    case Arithmetic_FTTx -> mutateArithmeticFTTx(individual.individualI);
                    default -> throw new IllegalStateException("Unexpected value: " + mutation);
                }
            }
        }
    }

    // Simple mutation on a bit level - many bits in an individual may mutate (for boolean arrays)
    private static void mutateUniform(boolean[] individualB) {
        for (int bit = 0; bit < individualB.length; bit++) {
            if (Math.random() < MUTATION_PROBABILITY) {
                individualB[bit] = !individualB[bit];
            }
        }
    }

    // Simple mutation on an individual level - max 1 bit in an individual may mutate (for boolean arrays)
    private static void mutationSinglePoint(boolean[] individualB) {
        int mutationPoint = (int) (Math.random() * BITS);
        individualB[mutationPoint] = !individualB[mutationPoint];
    }

    // (for Problems.TSP)
    private static void mutateExchange(int[] individualI) {
        // choose two random bits
        int bit1 = (int) (Math.random() * BITS);
        int bit2 = (int) (Math.random() * BITS);

        // swap them
        int temp = individualI[bit1];
        individualI[bit1] = individualI[bit2];
        individualI[bit2] = temp;
    }

    // (for Problems.TSP)
    private static void mutateInversion(int[] individualI) {
        // choose two random bits
        int from = (int) (Math.random() * BITS);
        int to = (int) (Math.random() * BITS);

        // make sure from <= to
        if (from > to) {
            int temp = from;
            from = to;
            to = temp;
        }

        // extract the sub-array
        int[] temp = new int[to - from];
        System.arraycopy(individualI, from, temp, 0, to - from);

        // insert the sub-array back, but in reverse order
        for (int i = 0; i < temp.length; i++) {
            individualI[from + i] = temp[(temp.length - 1) - i];
        }
    }

    private static void mutateArithmeticFTTx(int[] individualI) {
        double randOperation = Math.random();
        int randBitIndex = (int) (Math.random() * BITS);
        int randBit = individualI[randBitIndex];
        int randNewPeriod = (int) (Math.random() * FTTx.maxRolloutPeriod);

        double addition = 0.2;
        double subtraction = 0.4;
        double multiplication = 0.6;
        double division = 0.8;

        if (randOperation < addition) {
            if (randBit < FTTx.maxRolloutPeriod) randBit++;
            else randBit--;
        } else if (randOperation < subtraction) {
            if (randBit > 0) randBit--;
            else randBit++;
        } else if (randOperation < multiplication) {
            if (randBit == 0) randBit = 1;
            else if ((randBit * 2) <= FTTx.maxRolloutPeriod) randBit *= 2;
            else if (randBit != FTTx.maxRolloutPeriod) randBit += 1;
            else randBit = 0;
        } else if (randOperation < division) {
            if (randBit == 0) randBit = FTTx.maxRolloutPeriod;
            else if (randBit > 0) randBit /= 2;
            else randBit = randNewPeriod;
        } else { // random
            randBit = randNewPeriod;
        }

        individualI[randBitIndex] = randBit;
    }


    // ------------------------------------- Elitism -------------------------------------
    private static void recordElite() throws Exception {
        if (population[0].fitness == null) throw new Exception("Fitness not calculated");

        oldPopulationSorted = new ArrayList<>(Arrays.asList(population));
        oldPopulationSorted.sort((o1, o2) -> {
            if (minOrMax == MIN_MAX.Min) {
                return Double.compare(o1.fitness, o2.fitness);
            } else {
                return Double.compare(o2.fitness, o1.fitness);
            }
        });
    }

    private static void reintroduceElite() throws Exception {
        for (int i = 0; i < eliteIndividualsCount; i++) {
            // choose a random individual from the new population
            int index = (int) (Math.random() * POPULATION_SIZE);

            // if the random individual is worse than the elite individual, replace it
            population[index] = compareIndividuals(population[index], oldPopulationSorted.get(i));
        }
    }

    // returns the better individual (NOT using the fitness[] array)
    private static Individual compareIndividuals(Individual individual1, Individual individual2) throws Exception {
        if (minOrMax == MIN_MAX.Min) {
            return fitness(individual1) < fitness(individual2) ? individual1 : individual2;
        } else {
            return fitness(individual1) > fitness(individual2) ? individual1 : individual2;
        }
    }

    // returns the index of the better individual (using the fitness[] array)
    private static int compareIndividuals(int ind1_index, int ind2_index) {
        if (minOrMax == MIN_MAX.Min) {
            return fitness[ind1_index] < fitness[ind2_index] ? ind1_index : ind2_index;
        } else {
            return fitness[ind1_index] > fitness[ind2_index] ? ind1_index : ind2_index;
        }
    }

    // returns true if the contender is better than the current
    private static boolean compareFitness(double current, double contender) {
        if (minOrMax == MIN_MAX.Min) {
            return contender < current;
        } else {
            return contender > current;
        }
    }


    // ------------------------------------- Other -------------------------------------
    private static void resetGlobals() {
        population = null;
        fitness = null;
        if (selection == SELECTION.Roulette) prejudice = null;
        bestIndividual_EntireRun = null;

        oldPopulationSorted = null;
        if (selection == SELECTION.Tournament) everyoneWasParent = false;
        parents = new HashSet<>();
    }

    private static void recordBestIndividual_EntireRun() throws Exception {
        Individual bestInGeneration = findBestIndividual();

        bestIndividual_EntireRun = compareIndividuals(bestIndividual_EntireRun, bestInGeneration.copyItself());
    }

    private static boolean terminationConditionMet() {
        if (populationBestFitness() == terminationConditionFitness) {
            System.out.println("Termination condition met");
            return true;
        } else return false;
    }

    private static void changeCrossoverMutationProbability(int gen) {
        if (MAX_GENERATION >= 100) {
            // 25% - decrease crossover probability
            if (gen == (MAX_GENERATION / 4)) {
                CROSSOVER_PROBABILITY = (CROSSOVER_PROBABILITY - 0.1 >= 0) ? (CROSSOVER_PROBABILITY - 0.1) : 0;
            }

            // 50% - decrease crossover probability and increase mutation probability
            else if (gen == MAX_GENERATION / 2) {
                CROSSOVER_PROBABILITY /= 2;
                MUTATION_PROBABILITY = (MUTATION_PROBABILITY * 2 <= 1) ? (MUTATION_PROBABILITY * 2) : 1;
            }

            // 75% - increase mutation probability
            else if (gen == (MAX_GENERATION / 4) * 3) {
                MUTATION_PROBABILITY = (MUTATION_PROBABILITY + 0.1 <= 1) ? (MUTATION_PROBABILITY + 0.1) : 1;
            }
        }
    }
}