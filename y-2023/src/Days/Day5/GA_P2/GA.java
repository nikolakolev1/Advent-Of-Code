package Days.Day5.GA_P2;

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
    // ------------------------------------- Elitism -------------------------------------
    protected static boolean elitism = true;
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
    public static int BITS = 20;
    protected static int POPULATION_SIZE = 10000;
    protected static int TOURNAMENT_SIZE = 40;
    protected static int MAX_GENERATION = 50;
    protected static double CROSSOVER_PROBABILITY = 0.7, MUTATION_PROBABILITY = 0.3;


    // ------------------------------------- Global population -------------------------------------
    protected static Individual[] population;
    private static double[] fitness;
    protected static Individual bestIndividual_EntireRun;


    // ------------------------------------- Methods -------------------------------------
    public static long part2() {
        try {
            AocDay5.loadData(AocDay5.filename);

            // The main algorithm
            initialise();
            evaluate();

            runGA();

            return (long) GA.fitness(GA.bestIndividual_EntireRun);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

            // PLAYING AROUND: decrease the crossover and mutation probabilities
//            changeCrossoverMutationProbability(gen);
        }
    }


    // ------------------------------------- Initial random population -------------------------------------

    /**
     * Initialise the population with random individuals and record the best individual in it
     */
    private static void initialise() {
        population = randomPopulation(); // create a random population
        fitness = new double[POPULATION_SIZE]; // initialise the fitness array

        bestIndividual_EntireRun = population[0]; // initialise the best individual

        if (elitism) oldPopulationSorted = new ArrayList<>(); // keep track of the best parents if using elitism
    }

    private static Individual[] randomPopulation() {
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

    protected static double fitness(Individual individual) {
        long l = AocDay5.convertIntArrToLong(individual.individualI);

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
    private static Individual[] selection() {
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

    private static Individual findBestIndividual(int from, int to) {
        int bestInd_index = from;

        for (int i = from + 1; i < to; i++) {
            bestInd_index = compareIndividuals(bestInd_index, i);
        }

        return population[bestInd_index];
    }

    private static Individual findBestIndividual(int[] indexes) {
        int bestInd_index = indexes[0];

        for (int i = 1; i < indexes.length; i++) {
            bestInd_index = compareIndividuals(bestInd_index, indexes[i]);
        }

        return population[bestInd_index];
    }

    protected static Individual findBestIndividual() {
        return findBestIndividual(0, POPULATION_SIZE);
    }


    // ------------------------------------- Population evolution -------------------------------------

    /**
     * Crossover and mutation
     */
    private static Individual[] evolution(Individual[] parentPopulation) {
        Individual[] newPopulation = crossover(parentPopulation);
        mutation(newPopulation);

        return newPopulation;
    }

    /**
     * The getParent() method returns a parent based on a prejudice
     */
    private static Individual getParent(Individual[] parentPopulation) {
        return getParent_TournamentSel(parentPopulation);

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

    private static Individual[] crossover(Individual[] parentPopulation) {
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

        // reset the parents HashSet
        everyoneWasParent = false;
        parents = new HashSet<>();

        return newPopulation;
    }

    private static Individual[] crossoverIndividuals(Individual parent1, Individual parent2) {
        Individual p1copy = parent1.copyItself();
        Individual p2copy = parent2.copyItself();

        if (Math.random() < CROSSOVER_PROBABILITY) {
            int crossoverPoint = (int) (Math.random() * BITS);
            int[] temp = p1copy.individualI.clone();

            System.arraycopy(p2copy.individualI, crossoverPoint, p1copy.individualI, crossoverPoint, BITS - crossoverPoint);
            System.arraycopy(temp, crossoverPoint, p2copy.individualI, crossoverPoint, BITS - crossoverPoint);
        }

        return new Individual[]{p1copy, p2copy};
    }

    private static void mutation(Individual[] offspringPopulation) {
        for (Individual individual : offspringPopulation) {
            if (Math.random() < MUTATION_PROBABILITY) {
                double randOperation = Math.random();
                int randBitIndex = (int) (Math.random() * BITS);
                int randBit = individual.individualI[randBitIndex];
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

                individual.individualI[randBitIndex] = randBit;
            }
        }
    }


    // ------------------------------------- Elitism -------------------------------------
    private static void recordElite() throws Exception {
        if (population[0].fitness == null) throw new Exception("Fitness not calculated");

        oldPopulationSorted = new ArrayList<>(Arrays.asList(population));
        oldPopulationSorted.sort(Comparator.comparingDouble(o -> o.fitness));
    }

    private static void reintroduceElite() {
        for (int i = 0; i < eliteIndividualsCount; i++) {
            // choose a random individual from the new population
            int index = (int) (Math.random() * POPULATION_SIZE);

            // if the random individual is worse than the elite individual, replace it
            population[index] = compareIndividuals(population[index], oldPopulationSorted.get(i));
        }
    }

    // returns the better individual (NOT using the fitness[] array)
    private static Individual compareIndividuals(Individual individual1, Individual individual2) {
        return fitness(individual1) < fitness(individual2) ? individual1 : individual2;
    }

    // returns the index of the better individual (using the fitness[] array)
    private static int compareIndividuals(int ind1_index, int ind2_index) {
        return fitness[ind1_index] < fitness[ind2_index] ? ind1_index : ind2_index;
    }

    // returns true if the contender is better than the current
    private static boolean compareFitness(double current, double contender) {
        return contender < current;
    }


    // ------------------------------------- Other -------------------------------------
    private static void recordBestIndividual_EntireRun() {
        Individual bestInGeneration = findBestIndividual();

        bestIndividual_EntireRun = compareIndividuals(bestIndividual_EntireRun, bestInGeneration.copyItself());
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

/**
 * The individual class is the representation of a single individual in the population.
 */
class Individual {
    public int[] individualI;
    public Double fitness;

    public Individual(int[] individual) {
        individualI = individual;
    }

    public Individual copyItself() {
        return new Individual(individualI.clone());
    }
}