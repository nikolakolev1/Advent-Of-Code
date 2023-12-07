package Days.Day5.Main;

import Days.Day5.Enums.INDIVIDUAL_TYPE;
import Days.Day5.Enums.MIN_MAX;
import Days.Day5.Enums.SELECTION;
import Days.Day5.Problems.*;

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
        if (!checkIndividualAndFitnessMatch()) issue += "The individual type is not adequate for the problem\n";
        if (!checkIndividualAndCrossoverMatch()) issue += "The individual type is not adequate for the crossover\n";
        if (!checkIndividualAndMutationMatch()) issue += "The individual type is not adequate for the mutation\n";
        if (!checkProblemProvided()) issue += "The problem is not provided\n";
        if (!checkMinOrMaxAndFitnessMatch()) issue += "The fitness function is not adequate for the min/max\n";
        if (!checkMinOrMaxAndSelectionMatch()) issue += "The selection is not adequate for the min/max";

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

    private static boolean checkIndividualAndFitnessMatch() {
        switch (GA.fitnessFunc) {
            case MostBitsOn, LeastBitsOn, SequentialCovering -> {
                return GA.individualType == INDIVIDUAL_TYPE.boolArray;
            }
            case QuadEquationBoolArray -> {
                return GA.individualType == INDIVIDUAL_TYPE.boolArray && GA.BITS % Equation.NUMBERS_TO_FIND.length == 0;
            }
            case Tsp -> {
                return GA.individualType == INDIVIDUAL_TYPE.tspIntArray;
            }
            case FTTxNVP -> {
                return GA.individualType == INDIVIDUAL_TYPE.fttxIntArray;
            }
            case AocDay5 -> {
                return GA.individualType == INDIVIDUAL_TYPE.aocIntArray;
            }
            default -> {
                return false;
            }
        }
    }

    private static boolean checkIndividualAndCrossoverMatch() {
        switch (GA.crossover) {
            case SinglePoint_Simple, nPoint_Simple, Uniform_Simple -> {
                return GA.individualType == INDIVIDUAL_TYPE.boolArray;
            }
            case PMX_Tsp -> {
                return GA.individualType == INDIVIDUAL_TYPE.tspIntArray;
            }
            case SinglePoint_FTTx -> {
                return GA.individualType == INDIVIDUAL_TYPE.fttxIntArray || GA.individualType == INDIVIDUAL_TYPE.aocIntArray;
            }
            default -> {
                return false;
            }
        }
    }

    private static boolean checkIndividualAndMutationMatch() {
        switch (GA.mutation) {
            case Uniform_Bool, SinglePoint_Bool -> {
                return GA.individualType == INDIVIDUAL_TYPE.boolArray;
            }
            case Exchange_Tsp, Inversion_Tsp -> {
                return GA.individualType == INDIVIDUAL_TYPE.tspIntArray;
            }
            case Arithmetic_FTTx -> {
                return GA.individualType == INDIVIDUAL_TYPE.fttxIntArray || GA.individualType == INDIVIDUAL_TYPE.aocIntArray;
            }
            default -> {
                return false;
            }
        }
    }

    private static boolean checkProblemProvided() {
        switch (GA.fitnessFunc) {
            case QuadEquationBoolArray -> {
                return Equation.valuesAtPoints != null;
            }
            case Tsp -> {
                return TSP.costMatrix != null;
            }
            case FTTxNVP -> {
                return FTTx.households != null && FTTx.imitator != null;
            }
            case SequentialCovering -> {
                return SequentialCovering.trainingData != null;
            }
            case AocDay5 -> {
                return true; // I have no time to implement it rignt now
            }
            default -> {
                return true;
            }
        }
    }

    private static boolean checkMinOrMaxAndFitnessMatch() {
        switch (GA.fitnessFunc) {
            case MostBitsOn, LeastBitsOn, FTTxNVP, SequentialCovering -> {
                return GA.minOrMax == MIN_MAX.Max;
            }
            case QuadEquationBoolArray, Tsp, AocDay5 -> {
                return GA.minOrMax == MIN_MAX.Min;
            }
            default -> {
                return false;
            }
        }
    }

    private static boolean checkMinOrMaxAndSelectionMatch() {
        switch (GA.selection) {
            case Roulette -> {
                return GA.minOrMax == MIN_MAX.Max;
            }
            case Tournament -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}