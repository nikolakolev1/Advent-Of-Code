import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static ArrayList<Monkey> monkeys;
    private static HashMap<String, Integer> monkeyIndices;
    private static Monkey rootMonkey;

    public static void main(String[] args) {
        loadData("input.txt");

        part1();
        System.out.println();
        part2();
    }

    private static void part1() {
        System.out.println("=== Part 1 ===\nAnswer: " + calculateMonkey(rootMonkey));
    }

    private static void part2() {
        long answer = 1;

        Monkey hardcode = monkeys.get(monkeyIndices.get("tnbf"));
        hardcode.number = 452;
        hardcode.yellNumber = true;

        long right = calculateMonkey(monkeys.get(monkeyIndices.get(rootMonkey.operation[2])));

        Monkey leftPart = monkeys.get(monkeyIndices.get(rootMonkey.operation[0]));
        long left = calculateMonkey(monkeys.get(monkeyIndices.get(rootMonkey.operation[0])));

        Monkey human = monkeys.get(monkeyIndices.get("humn"));

        while (left > right) {
            human.number = answer;

            left = calculateMonkey(leftPart);

            if (left < right) {
                answer /= 10;
                break;
            } else if (left > right) {
                answer *= 10;
            } else {
                System.out.println("=== Part 2 ===\nhumn: " + human.number);
                return;
            }
        }

        while (left != right) {
            String[] ansStr = Long.toString(answer).split("");
            for (int i = 0; i < ansStr.length; i++) {
                for (int j = 0; j < 10; j++) {
                    ansStr[i] = String.valueOf(j);
                    human.number = toLong(ansStr);
                    left = calculateMonkey(leftPart);

                    if (left < right) {
                        ansStr[i] = String.valueOf(j - 1);
                        break;
                    } else if (left == right) {
                        System.out.println("=== Part 2 ===\nhumn: " + human.number);
                        return;
                    }
                }
            }
        }
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            monkeys = new ArrayList<>();
            monkeyIndices = new HashMap<>();
            int index = 0;
            while (myScanner.hasNextLine()) {
                Monkey thisMonkey = new Monkey(myScanner.nextLine().replace(":", "").split(" "));

                monkeys.add(thisMonkey);
                monkeyIndices.put(thisMonkey.name, index++);

                if (thisMonkey.name.equals("root")) {
                    rootMonkey = thisMonkey;
                }
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static long calculateMonkey(Monkey monkeyToCalc) {
        if (monkeyToCalc.yellNumber) {
            return monkeyToCalc.number;
        } else {
            long int1 = calculateMonkey(monkeys.get(monkeyIndices.get(monkeyToCalc.operation[0])));
            long int2 = calculateMonkey(monkeys.get(monkeyIndices.get(monkeyToCalc.operation[2])));

            if (monkeyToCalc.addition) {
                return int1 + int2;
            } else if (monkeyToCalc.subtraction) {
                return int1 - int2;
            } else if (monkeyToCalc.multiplication) {
                return int1 * int2;
            } else {
                return int1 / int2;
            }
        }
    }

    private static long toLong(String[] ansStr) {
        StringBuilder numberStr = new StringBuilder();
        for (String digit : ansStr) {
            numberStr.append(digit);
        }
        return Long.parseLong(numberStr.toString());
    }

    private static class Monkey {
        public String name;
        public long number;
        public String[] operation;
        public boolean yellNumber;
        public boolean addition;
        public boolean subtraction;
        public boolean multiplication;
        public boolean division;

        public Monkey(String[] yelling) {
            name = yelling[0];

            if (yelling.length == 2) {
                yellNumber = true;
                number = Integer.parseInt(yelling[1]);
            } else {
                yellNumber = false;
                operation = new String[3];
                System.arraycopy(yelling, 1, operation, 0, 3);

                String operationStr = operation[1];
                switch (operationStr) {
                    case "+" -> addition = true;
                    case "-" -> subtraction = true;
                    case "*" -> multiplication = true;
                    default -> division = true;
                }
            }
        }
    }
}