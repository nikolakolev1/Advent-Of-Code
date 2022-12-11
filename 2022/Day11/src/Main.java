import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    private static final ArrayList<Monkey> monkeys = new ArrayList<>();

    public static void main(String[] args) {
        String part = "Part 2"; // "Part 1" || "Part 2"

        createMonkeys(part);

        // do 20 turns
        for (int i = 0; i < 100; i++) {
            turn();
        }

        calculateMonkeyBusiness(part);
    }

    private static void createMonkeys(String part) {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();

                if (thisLine.startsWith("Monkey")) {
                    ArrayList<Integer> items = new ArrayList<>();
                    thisLine = myScanner.nextLine().replaceAll(" ", "");
                    StringBuilder number = new StringBuilder();
                    for (int i = 14; i < thisLine.length(); i++) {
                        if (Character.isDigit(thisLine.charAt(i))) {
                            number.append(thisLine.charAt(i));
                        } else {
                            items.add(Integer.parseInt(number.toString()));
                            number = new StringBuilder();
                        }
                    }
                    items.add(Integer.parseInt(number.toString()));

                    String[] operation = new String[2];
                    thisLine = myScanner.nextLine();
                    if (Character.isDigit(thisLine.charAt(thisLine.length() - 1))) { // if not */+ old
                        operation[0] = thisLine.substring(thisLine.length() - 3, thisLine.length() - 2);
                        operation[1] = thisLine.substring(thisLine.length() - 1);
                        if (Character.isDigit(thisLine.charAt(thisLine.length() - 2))) {
                            operation[0] = thisLine.substring(thisLine.length() - 4, thisLine.length() - 3);
                            operation[1] = thisLine.substring(thisLine.length() - 2);
                        }
                    } else {
                        operation[0] = thisLine.substring(thisLine.length() - 5, thisLine.length() - 4);
                        operation[1] = "old";
                    }


                    thisLine = myScanner.nextLine();
                    int testDivisibleBy = Character.isDigit(thisLine.charAt(thisLine.length() - 2)) ?
                            Integer.parseInt(thisLine.substring(thisLine.length() - 2)) : Integer.parseInt(thisLine.substring(thisLine.length() - 1));

                    int[] testOutcomeToMonkey = new int[2];
                    thisLine = myScanner.nextLine();
                    testOutcomeToMonkey[0] = Integer.parseInt(thisLine.substring(thisLine.length() - 1));
                    thisLine = myScanner.nextLine();
                    testOutcomeToMonkey[1] = Integer.parseInt(thisLine.substring(thisLine.length() - 1));

                    Monkey newMonkey = new Monkey(part, items, operation, testDivisibleBy, testOutcomeToMonkey);
                    monkeys.add(newMonkey);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void turn() {
        for (Monkey monkey : monkeys) {
            int monkeyItemCount = monkey.getQueueLength();
            for (int i = 0; i < monkeyItemCount; i++) {
                BigInteger[] toDoThisTurn = monkey.throwItemToMonkey();
                if (toDoThisTurn != null) {
                    monkeys.get(toDoThisTurn[1].intValue()).addItem(toDoThisTurn[0]);
                }
            }
        }
    }

    private static void calculateMonkeyBusiness(String part) {
        BigInteger mostInspectedItems1 = new BigInteger("0");
        BigInteger mostInspectedItems2 = new BigInteger("0");
        for (Monkey monkey : monkeys) {
            BigInteger currentMonkeyInspectedItems = new BigInteger(String.valueOf(monkey.getInspectedItems()));
            if (currentMonkeyInspectedItems.compareTo(mostInspectedItems1) > 0) {
                mostInspectedItems2 = mostInspectedItems1;
                mostInspectedItems1 = currentMonkeyInspectedItems;
            } else if (currentMonkeyInspectedItems.compareTo(mostInspectedItems2) > 0) {
                mostInspectedItems2 = currentMonkeyInspectedItems;
            }
        }
        System.out.println(part + ": " + (mostInspectedItems1.multiply(mostInspectedItems2)));
    }
}

class Monkey {
    private final String part;
    private final Queue<BigInteger> items = new LinkedList<>();
    private final String[] operation;
    private final int testDivisibleBy;
    private final int[] testOutcomeToMonkey;
    private int inspectedItems = 0;

    public Monkey(String part, ArrayList<Integer> items, String[] operation, int testDivisibleBy, int[] testOutcomeToMonkey) {
        this.part = part;
        for (Integer number : items) {
            this.items.add(BigInteger.valueOf(number));
        }
        this.operation = operation;
        this.testDivisibleBy = testDivisibleBy;
        this.testOutcomeToMonkey = testOutcomeToMonkey;

        BigInteger[] primesAsBigInt = new BigInteger[10];
        primesAsBigInt[0] = new BigInteger("23");
        primesAsBigInt[1] = new BigInteger("29");
        primesAsBigInt[2] = new BigInteger("31");
        primesAsBigInt[3] = new BigInteger("37");
        primesAsBigInt[4] = new BigInteger("41");
        primesAsBigInt[5] = new BigInteger("43");
        primesAsBigInt[6] = new BigInteger("47");
        primesAsBigInt[7] = new BigInteger("53");
        primesAsBigInt[8] = new BigInteger("59");
        primesAsBigInt[9] = new BigInteger("61");
    }

    public void addItem(BigInteger item) {
        items.add(item);
    }

    public BigInteger[] throwItemToMonkey() {
        BigInteger itemWorryLevel = topItemNewWorryLevel();
        if (itemWorryLevel != null) {
            inspectedItems++;
            if (part.equals("Part 1")) {
                itemWorryLevel = itemWorryLevel.divide(BigInteger.valueOf(3));
            }
            if (itemWorryLevel.mod(BigInteger.valueOf(testDivisibleBy)).equals(BigInteger.valueOf(0))) {
                return new BigInteger[]{itemWorryLevel, BigInteger.valueOf(testOutcomeToMonkey[0])};
            } else {
                return new BigInteger[]{itemWorryLevel, BigInteger.valueOf(testOutcomeToMonkey[1])};
            }
        }
        return null;
    }

    private BigInteger topItemNewWorryLevel() {
        if (!items.isEmpty()) {
            if (operation[0].equals("+")) {
                BigInteger temp = operation[1].equals("old") ? items.peek().add(items.poll()) : items.poll().add(BigInteger.valueOf(Integer.parseInt(operation[1])));

//                for (int i = 0; i < primesAsBigInt.length; i++) {
//                    if (temp.mod(primesAsBigInt[i]).equals(BigInteger.ZERO)) {
//                        temp = temp.divide(primesAsBigInt[i]);
//                        break;
//                    }
//                }

                return temp;

            } else {
//                BigInteger temp1 = items.peek();
                BigInteger temp = operation[1].equals("old") ? items.peek().multiply(items.poll()) : items.poll().multiply(BigInteger.valueOf(Integer.parseInt(operation[1])));

//                for (int i = 0; i < primesAsBigInt.length; i++) {
//                    if (temp.mod(primesAsBigInt[i]).equals(BigInteger.ZERO)) {
//                        temp = temp.divide(primesAsBigInt[i]);
//                        break;
//                    }
//                }

                return temp;
//
//                if (Double.MAX_VALUE < temp.doubleValue()) {
//                    return temp1;
//                } else {
//                    return temp;
//                }
//                return operation[1].equals("old") ? items.peek().multiply(items.poll()) : items.poll().multiply(BigInteger.valueOf(Integer.parseInt(operation[1])));
            }
        }
        return null;
    }

    public int getQueueLength() {
        return items.size();
    }

    public int getInspectedItems() {
        return inspectedItems;
    }
}