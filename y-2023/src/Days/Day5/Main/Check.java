package Days.Day5.Main;

import Days.Day5.Enums.SELECTION;

/**
 * This class is used to check if ALL the parameters are set correctly,
 * so that the Main.GA can run.
 */
public class Check {
    public static void checkEverything() throws Exception {
        String issue = "";

        if (!checkEverythingIsSet()) issue += "Not everything is set\n";
        if (!checkPopSizeDivisibleBy4()) issue += "Population size must be divisible by 4\n";
        if (!checkTournamentSize())
            issue += "Tournament size must be > 1 and < POP_SIZE AND BE a dividend of POP_SIZE\n";
        if (!checkCrossoverProb()) issue += "Crossover probability must be between 0 and 1\n";
        if (!checkMutationProb()) issue += "Mutation probability must be between 0 and 1\n";

        if (!issue.isEmpty()) throw new Exception(issue);
    }

    private static boolean checkEverythingIsSet() {
        return GA.individualType != null &&
                GA.selection != null &&
                GA.fitnessFunc != null &&
                GA.crossover != null &&
                GA.mutation != null &&
                GA.minOrMax != null;
    }

    private static boolean checkPopSizeDivisibleBy4() {
        return GA.POPULATION_SIZE % 4 == 0;
    }

    private static boolean checkTournamentSize() {
        if (GA.selection == SELECTION.Tournament) {
            return (GA.TOURNAMENT_SIZE > 0 && GA.TOURNAMENT_SIZE < GA.POPULATION_SIZE) && GA.POPULATION_SIZE % GA.TOURNAMENT_SIZE == 0;
        } else return true;
    }

    private static boolean checkCrossoverProb() {
        return GA.CROSSOVER_PROBABILITY >= 0.0 && GA.CROSSOVER_PROBABILITY <= 1.0;
    }

    private static boolean checkMutationProb() {
        return GA.MUTATION_PROBABILITY >= 0.0 && GA.MUTATION_PROBABILITY <= 1.0;
    }
}