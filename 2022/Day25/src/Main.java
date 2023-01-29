import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static ArrayList<Long> decimals;
    private static HashMap<Integer, Long> powersOf5;
    private static HashMap<Integer, Long> biggestDecimalWithRemaining;

    public static void main(String[] args) {
        loadPowersOf5();
        loadBiggestDecimalWithRemaining();
        loadData("input.txt");
        System.out.println("=== Final Day ===\nAnswer: " + convertToSNAFU(sumOfNumbersInDecimal()));
    }

    private static void loadPowersOf5() {
        powersOf5 = new HashMap<>();

        powersOf5.put(0, 1L);
        powersOf5.put(1, 5L);
        powersOf5.put(2, 25L);
        powersOf5.put(3, 125L);
        powersOf5.put(4, 625L);
        powersOf5.put(5, 3125L);
        powersOf5.put(6, 15625L);
        powersOf5.put(7, 78125L);
        powersOf5.put(8, 390625L);
        powersOf5.put(9, 1953125L);
        powersOf5.put(10, 9765625L);
        powersOf5.put(11, 48828125L);
        powersOf5.put(12, 244140625L);
        powersOf5.put(13, 1220703125L);
        powersOf5.put(14, 6103515625L);
        powersOf5.put(15, 30517578125L);
        powersOf5.put(16, 152587890625L);
        powersOf5.put(17, 762939453125L);
        powersOf5.put(18, 3814697265625L);
        powersOf5.put(19, 19073486328125L);
        powersOf5.put(20, 95367431640625L);
        powersOf5.put(21, 476837158203125L);
        powersOf5.put(22, 2384185791015625L);
    }

    private static void loadBiggestDecimalWithRemaining() {
        biggestDecimalWithRemaining = new HashMap<>();

        for (int i = 0; i < powersOf5.size(); i++) {
            if (i == 0) {
                biggestDecimalWithRemaining.put(i, 2L);
            } else {
                biggestDecimalWithRemaining.put(i, (powersOf5.get(i) * 2) + biggestDecimalWithRemaining.get(i - 1));
            }
        }
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            decimals = new ArrayList<>();
            while (myScanner.hasNextLine()) {
                String[] thisLine = myScanner.nextLine().split("");
                decimals.add(convertToDecimal(thisLine));
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static long convertToDecimal(String[] SNAFU) {
        long answer = 0;
        for (int i = 0; i < SNAFU.length; i++) {
            int correspondingPower = (SNAFU.length - 1) - i;

            if (SNAFU[i].equals("=")) {
                answer += powersOf5.get(correspondingPower) * -2;
            } else if (SNAFU[i].equals("-")) {
                answer += powersOf5.get(correspondingPower) * -1;
            } else {
                answer += powersOf5.get(correspondingPower) * Integer.parseInt(SNAFU[i]);
            }
        }
        return answer;
    }

    private static String convertToSNAFU(long decimal) {
        StringBuilder SNAFUResult = new StringBuilder();

        int startingIndex = identifySNAFULength(decimal);
        for (int i = startingIndex; i >= 0; i--) {
            long thisPowerOf5 = powersOf5.get(i);
            long biggestDecWithRemainingLowerLevel = 0L;
            if (i != 0) {
                biggestDecWithRemainingLowerLevel = biggestDecimalWithRemaining.get(i - 1);
            }
            boolean negativeDecimal = false;
            long positiveDecimal;
            if (decimal < 0) {
                negativeDecimal = true;
                positiveDecimal = -1 * decimal;
            } else {
                positiveDecimal = decimal;
            }

            if (decimal == 0 || positiveDecimal <= biggestDecWithRemainingLowerLevel) {
                SNAFUResult.append("0");
            } else {
                long difference = positiveDecimal - thisPowerOf5;
                if (difference < 0) difference *= -1;
                if (difference <= biggestDecWithRemainingLowerLevel) {
                    if (!negativeDecimal) {
                        SNAFUResult.append("1");
                        decimal -= thisPowerOf5;
                    } else {
                        SNAFUResult.append("-");
                        decimal += thisPowerOf5;
                    }
                } else {
                    if (!negativeDecimal) {
                        SNAFUResult.append("2");
                        decimal -= thisPowerOf5 * 2;
                    } else {
                        SNAFUResult.append("=");
                        decimal += thisPowerOf5 * 2;
                    }
                }
            }
        }

        return SNAFUResult.toString();
    }

    private static int identifySNAFULength(long decimal) {
        for (int i = powersOf5.size() - 1; i > 0; i--) {
            if (decimal >= 2 * powersOf5.get(i - 1)) {
                return i;
            }
        }
        return 0;
    }

    private static long sumOfNumbersInDecimal() {
        long decimalsSum = 0;
        for (Long decimal : decimals) {
            decimalsSum += decimal;
        }
        return decimalsSum;
    }
}