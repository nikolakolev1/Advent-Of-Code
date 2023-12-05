package General;

import Days.Day1.Day1;
import Days.Day2.Day2;
import Days.Day3.Day3;
import Days.Day4.Day4;
import Days.Day5.Day5;
import Days.Day6.Day6;
import Days.Day7.Day7;
import Days.Day8.Day8;
import Days.Day9.Day9;

public class Helper {
    private static final int P1 = 0, P2 = 1, P1_TIME = 2, P2_TIME = 3;

    public static String filename(int day) {
        return "data/day" + day + "/input.txt";
    }

    public static String filename_test(int day) {
        return "data/day" + day + "/test_input.txt";
    }

    public static void solveAndPrint(int dayInt) {
        long[] answers = solve(dayInt);
        System.out.println("*** Day " + dayInt + " ***");
        printAnswer(1, answers[0]);
        printAnswer(2, answers[1]);
        System.out.println();
    }

    public static void solveAndPrint_Time(int dayInt) {
        long[] answers = solve_Time(dayInt);
        System.out.println("*** Day " + dayInt + " ***");
        printAnswer(1, (int) answers[P1], answers[P1_TIME]);
        printAnswer(2, (int) answers[P2], answers[P2_TIME]);
        System.out.println();
    }

    public static void printAnswer(int part, long answer) {
        System.out.println("Part " + part + " : " + answer);
    }

    public static void printAnswer(int part, int answer, long time) {
        System.out.println("Part " + part + ": " + answer + " (" + time + "ms)");
    }

    private static Day getDay(int day) {
        return switch (day) {
            case 1 -> new Day1();
            case 2 -> new Day2();
            case 3 -> new Day3();
            case 4 -> new Day4();
            case 5 -> new Day5();
            case 6 -> new Day6();
            case 7 -> new Day7();
            case 8 -> new Day8();
            case 9 -> new Day9();
//            case 10 -> new Days.Day10();
//            case 11 -> new Days.Day11();
//            case 12 -> new Days.Day12();
//            case 13 -> new Days.Day13();
//            case 14 -> new Days.Day14();
//            case 15 -> new Days.Day15();
//            case 16 -> new Days.Day16();
//            case 17 -> new Days.Day17();
//            case 18 -> new Days.Day18();
//            case 19 -> new Days.Day19();
//            case 20 -> new Days.Day20();
//            case 21 -> new Days.Day21();
//            case 22 -> new Days.Day22();
//            case 23 -> new Days.Day23();
//            case 24 -> new Days.Day24();
//            case 25 -> new Days.Day25();
            default -> throw new IllegalStateException("Invalid day: " + day);
        };
    }

    private static long[] solve(int dayInt) {
        Day day = getDay(dayInt);

        day.loadData(filename(dayInt));

        long p1_answer = day.part1();
        long p2_answer = day.part2();

        return new long[]{p1_answer, p2_answer};
    }

    private static long[] solve_Time(int dayInt) {
        Day day = getDay(dayInt);

        day.loadData(filename(dayInt));

        long p1_start = System.nanoTime();
        long p1_answer = day.part1();
        long p1_end = System.nanoTime();
        long p1_time_ms = (p1_end - p1_start) / 1000000;

        long p2_start = System.nanoTime();
        long p2_answer = day.part2();
        long p2_end = System.nanoTime();
        long p2_time_ms = (p2_end - p2_start) / 1000000;

        return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
    }
}