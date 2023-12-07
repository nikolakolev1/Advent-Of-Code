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
            case ("Sml") -> smallPopulation();
            case ("Med") -> mediumPopulation();
            case ("Lrg") -> largePopulation();
            case ("Gen") -> manyGensPopulation();
            case ("Qde") -> qdePopulation();
            case ("Tsp") -> tspPopulation();
            case ("Ftx") -> ftxPopulation();
            case ("Sco") -> scoPopulation();
            case ("Aoc") -> aocPopulation();
            default -> throw new Exception("Error with the preset");
        }
    }

    private static void smallPopulation() {
        GA.setSettings(5, 12, 3, 5, 0.95, 0.03);
    }

    private static void mediumPopulation() {
        GA.setSettings(20, 32, 4, 10, 0.95, 0.03);
    }

    private static void largePopulation() {
        GA.setSettings(100, 400, 10, 40, 0.95, 0.03);
    }

    private static void manyGensPopulation() {
        GA.setSettings(100, 400, 10, 100, 0.95, 0.03);
    }

    private static void qdePopulation() {
        int BITS = Equation.NUMBERS_TO_FIND.length * 5;
        GA.setSettings(BITS, 32, 4, 20, 0.95, 0.03);
    }

    private static void tspPopulation() {
        int size = 3;

        if (TSP.filename != null) {
            size = GA.BITS;
        } else {
            while (true) {
                double trigSide = size - 1;
                double temp = trigSide * ((trigSide / 2) + 0.5);
                if (temp == TSP.costArray.length) break;
                else size++;
            }
        }

        GA.setSettings(size, 160, 8, 200, 0.9, 0.25);
    }

    private static void ftxPopulation() throws Exception {
        if (FTTx.noOfAreas == 0) throw new Exception("Problems.FTTx not initialised");
        else GA.setSettings(FTTx.noOfAreas, 280, 10, 600, 0.9, 0.5);
    }

    private static void scoPopulation() {
        int BITS = SequentialCovering.trainingData.get(0).length;
        GA.setSettings(BITS, 100, 4, 10, 0.9, 0.2);
    }

    private static void aocPopulation() {
        GA.setSettings(20, 2000, 20, 100, 0.7, 0.3);
    }
}