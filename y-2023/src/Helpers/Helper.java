package Helpers;

import Day1.Day1;
import Day10.Day10;
import Day11.Day11;
import Day12.Day12;
import Day13.Day13;
import Day14.Day14;
import Day15.Day15;
import Day16.Day16;
import Day17.Day17;
import Day18.Day18;
import Day19.Day19;
import Day2.Day2;
import Day20.Day20;
import Day21.Day21;
import Day22.Day22;
import Day23.Day23;
import Day24.Day24;
import Day25.Day25;
import Day3.Day3;
import Day4.Day4;
import Day5.Day5;
import Day6.Day6;
import Day7.Day7;
import Day8.Day8;
import Day9.Day9;

public class Helper {
    private static final int P1 = 0, P2 = 1, P1_TIME = 2, P2_TIME = 3;

    public static String filename(int day) {
        return "data/day" + day + "/input.txt";
    }

    public static String filename_test(int day) {
        return "data/day" + day + "/test_input.txt";
    }

    public static void solveAndPrint(int dayInt) {
        int[] answers = solve(dayInt);
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

    public static void printAnswer(int part, int answer) {
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
//            case 5 -> new Day5();
//            case 6 -> new Day6();
//            case 7 -> new Day7();
//            case 8 -> new Day8();
//            case 9 -> new Day9();
//            case 10 -> new Day10();
//            case 11 -> new Day11();
//            case 12 -> new Day12();
//            case 13 -> new Day13();
//            case 14 -> new Day14();
//            case 15 -> new Day15();
//            case 16 -> new Day16();
//            case 17 -> new Day17();
//            case 18 -> new Day18();
//            case 19 -> new Day19();
//            case 20 -> new Day20();
//            case 21 -> new Day21();
//            case 22 -> new Day22();
//            case 23 -> new Day23();
//            case 24 -> new Day24();
//            case 25 -> new Day25();
            default -> throw new IllegalStateException("Invalid day: " + day);
        };
    }

    public static int[] solve(int dayInt) {
        Day day = getDay(dayInt);

        day.loadData(filename(dayInt));

        int p1_answer = day.part1();
        int p2_answer = day.part2();

        return new int[]{p1_answer, p2_answer};
    }

    public static long[] solve_Time(int dayInt) {
        Day day = getDay(dayInt);

        day.loadData(filename(dayInt));

        long p1_start = System.nanoTime();
        int p1_answer = day.part1();
        long p1_end = System.nanoTime();
        long p1_time_ms = (p1_end - p1_start) / 1000000;

        long p2_start = System.nanoTime();
        int p2_answer = day.part2();
        long p2_end = System.nanoTime();
        long p2_time_ms = (p2_end - p2_start) / 1000000;

        return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
    }
}