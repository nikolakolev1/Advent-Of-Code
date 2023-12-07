package Days.Day5.Problems;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Sequential covering procedure that uses a Main.GA to create rules.
 */
public class SequentialCovering {
    public static String filename = "files/sequentialCoveringFiles/weather.arff";
    public static ArrayList<boolean[]> trainingData;

    public static void setTrainingData() throws IOException {
        trainingData = Dataset.read(filename);
    }

    public static double fitness(boolean[] individual) {
        int truePositives = 0;
        int falsePositives = 0;

        // calculate how many true positives and false positives
        for (boolean[] instance : trainingData) {
            if (covers(individual, instance)) {
                // use target() to get the target value of the instance
                if (target(individual) == target(instance)) {
                    truePositives++;
                } else {
                    falsePositives++;
                }
            }
        }

        // calculate the precision (fitness)
        if (truePositives + falsePositives == 0) return 0;
        else {
            return (double) truePositives / (truePositives + falsePositives);
        }
    }

    private static boolean covers(boolean[] individual, boolean[] instance) {
        return Dataset.covers(individual, instance);
    }

    private static boolean target(boolean[] encoding) {
        return Dataset.target(encoding);
    }
}

