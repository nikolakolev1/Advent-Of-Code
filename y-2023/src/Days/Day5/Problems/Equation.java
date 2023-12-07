package Days.Day5.Problems;

import Days.Day5.Main.GA;

import java.util.HashMap;

public class Equation {
    public static final int[] NUMBERS_TO_FIND = new int[]{3, 0, -4};
    public static HashMap<Double, Double> valuesAtPoints;

    public static boolean evaluateParameters() {
        return GA.BITS % NUMBERS_TO_FIND.length == 0;
    }

    public static void populateValuesAtPoints() {
        valuesAtPoints = quadraticEquationSolver(NUMBERS_TO_FIND);
    }

    public static HashMap<Double, Double> quadraticEquationSolver(int[] abc) {
        int howManyValues = 10;
        HashMap<Double, Double> hm = new HashMap<>();

        int a = abc[0];
        int b = abc[1];
        int c = abc[2];

        for (int x = -(howManyValues / 2); x <= (howManyValues / 2); x++) {
            double y = (a * (Math.pow(x, 2.0))) + (b * x) + c;
            hm.put((double) x, y);
        }

        return hm;
    }

    public static int binaryToDecimal(boolean[] binary) {
        int decimal = 0;
        boolean negative = false;

        for (int i = 0; i < binary.length; i++) {
            if (i != 0 && binary[i]) decimal += (int) Math.pow(2, binary.length - i - 1);
            else if (i == 0) negative = binary[i];
        }

        return negative ? (decimal * -1) : decimal;
    }
}