import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Number> numbers;
    static int numbersCount;

    /**
     * idea:
     * 1) give every number left and right neighbours
     * 2) whenever a number has to move with their value
     * to the right (if the value is positive) and to the left (if the value is negative),
     * there is a for-loop that gets them between the appropriate numbers and reassigns
     * the old and new numbers' neighbours
     * 3) reassigning:
     * - old left's right becomes old right
     * - old right's left becomes old left
     * - current's left becomes new left
     * - current's right becomes new right
     * - new left's right becomes current
     * - new right's left becomes current
     * 4) then use modulo to find the 1000th, 2000th, and 3000th values
     * <p>
     * additionally: every number can store 10th, 100th and 1000th neighbour to the right for
     * a faster traverse through the numbers' circular graph (I'm not sure if 'graph'
     * is the right term here)
     */
    public static void main(String[] args) {
        part1();
        System.out.println();
        part2();
    }

    private static void part1() {
        loadData("input.txt");
        giveNeighbours();
        moveAll();
        printAnswer(1);
    }

    private static void part2() {
        for (Number n : numbers) {
            n.value *= 811589153;
        }
        giveNeighbours();
        for (int i = 0; i < 10; i++) {
            moveAll();
        }
        printAnswer(2);
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            numbers = new ArrayList<>();
            while (myScanner.hasNextLine()) {
                numbers.add(new Number(Integer.parseInt(myScanner.nextLine())));
            }

            numbersCount = numbers.size();
            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void giveNeighbours() {
        int lastNumIndex = numbersCount - 1, firstNumIndex = 0;

        for (int i = 0; i < numbersCount; i++) {
            Number currentNum = numbers.get(i);
            Number previousNum = new Number(0);
            Number nextNum = new Number(0);
//            Number[] prev10_100_100 = new Number[3];
//            Number[] next10_100_100 = new Number[3];

            if (i > 0) {
                previousNum = numbers.get(i - 1);
//                if (i > 9) prev10_100_100[0] = numbers.get(i - 10);
//                else prev10_100_100[0] = numbers.get(numbersCount + (i - 10));
//                if (i > 99) prev10_100_100[1] = numbers.get(i - 100);
//                else prev10_100_100[1] = numbers.get(numbersCount + (i - 100));
//                if (i > 999) prev10_100_100[2] = numbers.get(i - 1000);
//                else prev10_100_100[2] = numbers.get(numbersCount + (i - 1000));
            }
            if (i < lastNumIndex) {
                nextNum = numbers.get(i + 1);
//                if (i < lastNumIndex - 9) next10_100_100[0] = numbers.get(i + 10);
//                else next10_100_100[0] = numbers.get(i + (numbersCount - 10));
//                if (i < lastNumIndex - 99) next10_100_100[1] = numbers.get(i + 100);
//                else next10_100_100[1] = numbers.get(i + (numbersCount - 100));
//                if (i < lastNumIndex - 999) prev10_100_100[2] = numbers.get(i + 1000);
//                else next10_100_100[2] = numbers.get(i + (numbersCount - 1000));
            }

            if (i == firstNumIndex) {
                currentNum.left = numbers.get(lastNumIndex);
                currentNum.right = nextNum;

//                prev10_100_100 = new Number[]{numbers.get(numbersCount - 10), numbers.get(numbersCount - 100), numbers.get(numbersCount - 1000)};
            } else if (i == lastNumIndex) {
                currentNum.left = previousNum;
                currentNum.right = numbers.get(firstNumIndex);
            } else {
                currentNum.left = previousNum;
                currentNum.right = nextNum;
            }
        }
    }

    private static void move(Number currentNumber) {
        Number oldLeft = currentNumber.left;
        Number oldRight = currentNumber.right;

        Number newLeft = currentNumber, newRight = currentNumber;
        int repeat = (int) (currentNumber.value % (numbersCount - 1));
        if (repeat > 0) {
            for (int i = 0; i < repeat; i++) {
                newLeft = newLeft.right;
                if (newLeft.equals(currentNumber)) {
                    newLeft = newLeft.right;
                }
            }
            newRight = newLeft.right;
            if (newRight.equals(currentNumber)) {
                newRight = newRight.right;
            }
        } else if (repeat < 0) {
            for (int i = 0; i > repeat; i--) {
                newRight = newRight.left;
                if (newRight.equals(currentNumber)) {
                    newRight = newRight.left;
                }
            }
            newLeft = newRight.left;
            if (newLeft.equals(currentNumber)) {
                newLeft = newLeft.left;
            }
        } else {
            return;
        }

        currentNumber.left = newLeft;
        currentNumber.right = newRight;
        oldLeft.right = oldRight;
        oldRight.left = oldLeft;
        newLeft.right = currentNumber;
        newRight.left = currentNumber;
    }

    private static void moveAll() {
        for (Number number : numbers) {
            move(number);
        }
    }

    private static void printAnswer(int part) {
        Number zero = new Number(0);
        for (Number number : numbers) {
            if (number.value == 0) {
                zero = number;
                break;
            }
        }

        Number val1 = zero, val2 = zero, val3 = zero;
        int recursions = 1000 % numbersCount;
        for (int i = 0; i < recursions; i++) {
            val1 = val1.right;
        }
        recursions = 2000 % numbersCount;
        for (int i = 0; i < recursions; i++) {
            val2 = val2.right;
        }
        recursions = 3000 % numbersCount;
        for (int i = 0; i < recursions; i++) {
            val3 = val3.right;
        }

        long answer = val1.value + val2.value + val3.value;
        System.out.println("=== Part " + part + " ===\nAnswer: " + answer);
    }

    static class Number {
        public long value;
        public Number left;
        public Number right;
//        public Number[] l10_100_1000_r10_100_1000;

        public Number(long value) {
            this.value = value;
//            l10_100_1000_r10_100_1000 = new Number[6];
        }
    }
}