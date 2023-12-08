package Days.Day5.Main;

import Days.Day5.Enums.*;
import Days.Day5.Problems.Equation;
import Days.Day5.Problems.FTTx;
import Days.Day5.Problems.SequentialCovering;
import Days.Day5.Problems.TSP;

public class Presets {
    public static void preset(String letterCode) throws Exception {
        INDIVIDUAL_TYPE ind;
        SELECTION sel;
        FITNESS_FUNC fit;
        CROSSOVER cross;
        MUTATION mutate;
        boolean elite;

        switch (letterCode.substring(0, 3)) {
            case ("Aoc") -> ind = INDIVIDUAL_TYPE.aocIntArray;
            default -> throw new Exception("Error with the preset (individual type)");
        }
        switch (letterCode.substring(3, 6)) {
            case ("Tor") -> sel = SELECTION.Tournament;
            default -> throw new Exception("Error with the preset (selection type)");
        }
        switch (letterCode.substring(6, 9)) {
            case ("Aoc") -> fit = FITNESS_FUNC.AocDay5;
            default -> throw new Exception("Error with the preset (fitness function)");
        }
        switch (letterCode.substring(9, 12)) {
            case ("Ftx") -> cross = CROSSOVER.SinglePoint_FTTx;
            default -> throw new Exception("Error with the preset (crossover)");
        }
        switch (letterCode.substring(12, 15)) {
            case ("Ari") -> mutate = MUTATION.Arithmetic_FTTx;
            default -> throw new Exception("Error with the preset (mutation)");
        }
        switch (letterCode.substring(15, 18)) {
            case ("Noe") -> elite = false;
            case ("Elt") -> elite = true;
            default -> throw new Exception("Error with the preset (elitism)");
        }

        GA.setSwitches(ind, sel, fit, cross, mutate, elite);

        switch (letterCode.substring(18, 21)) {
            case ("Aoc") -> aocPopulation();
            default -> throw new Exception("Error with the preset");
        }
    }

    private static void aocPopulation() {
        GA.setSettings(20, 2000, 20, 100, 0.7, 0.3);
    }
}