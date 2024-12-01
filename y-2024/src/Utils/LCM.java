package Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LCM {
    public static BigInteger findLCM(List<Integer> denominators) {
        ArrayList<Integer> commonMultiples = new ArrayList<>();
        int currentPrime = 2;

        while (!allAreOnes(denominators)) {
            if (anyIsDivisibleBy(denominators, currentPrime)) {
                commonMultiples.add(currentPrime);

                for (int i = 0; i < denominators.size(); i++) {
                    int dividend = denominators.get(i);
                    if (isDivisibleBy(dividend, currentPrime)) {
                        denominators.set(i, (dividend / currentPrime));
                    }
                }
            } else {
                currentPrime = getNextPrime(currentPrime);
            }
        }

        BigInteger lcm = new BigInteger("1");
        for (Integer commonMultiple : commonMultiples) {
            lcm = lcm.multiply(new BigInteger(String.valueOf(commonMultiple)));
        }

        return lcm;
    }

    private static int getNextPrime(int currentPrime) {
        int num = currentPrime + 1;

        while (!isPrime(num)) num++;

        return num;
    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }

        for (int i = 2; i <= num / 2; i++) {
            if ((num % i) == 0)
                return false;
        }
        return true;
    }

    private static boolean allAreOnes(List<Integer> list) {
        for (Integer i : list) {
            if (i != 1) return false;
        }

        return true;
    }

    private static boolean isDivisibleBy(int dividend, int divisor) {
        return dividend % divisor == 0;
    }

    private static boolean anyIsDivisibleBy(List<Integer> list, int divisor) {
        for (Integer i : list) {
            if (isDivisibleBy(i, divisor)) return true;
        }

        return false;
    }
}
