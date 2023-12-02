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

    public static void solveAndPrint(int day) {
        int[] answers = solve(day);
        System.out.println("*** Day " + day + " ***");
        printAnswer(1, answers[0]);
        printAnswer(2, answers[1]);
        System.out.println();
    }

    public static void solveAndPrint_Time(int day) {
        long[] answers = solve_Time(day);
        System.out.println("*** Day " + day + " ***");
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

    public static int[] solve(int day) throws IllegalStateException {
        switch (day) {
            case 1 -> {
                Day1.loadData(filename(day));
                return new int[]{Day1.part1(), Day1.part2()};
            }
            case 2 -> {
                Day2.loadData(filename(day));
                return new int[]{Day2.part1(), Day2.part2()};
            }
            case 3 -> {
                Day3.loadData(filename(day));
                return new int[]{Day3.part1(), Day3.part2()};
            }
            case 4 -> {
                Day4.loadData(filename(day));
                return new int[]{Day4.part1(), Day4.part2()};
            }
            case 5 -> {
                Day5.loadData(filename(day));
                return new int[]{Day5.part1(), Day5.part2()};
            }
            case 6 -> {
                Day6.loadData(filename(day));
                return new int[]{Day6.part1(), Day6.part2()};
            }
            case 7 -> {
                Day7.loadData(filename(day));
                return new int[]{Day7.part1(), Day7.part2()};
            }
            case 8 -> {
                Day8.loadData(filename(day));
                return new int[]{Day8.part1(), Day8.part2()};
            }
            case 9 -> {
                Day9.loadData(filename(day));
                return new int[]{Day9.part1(), Day9.part2()};
            }
            case 10 -> {
                Day10.loadData(filename(day));
                return new int[]{Day10.part1(), Day10.part2()};
            }
            case 11 -> {
                Day11.loadData(filename(day));
                return new int[]{Day11.part1(), Day11.part2()};
            }
            case 12 -> {
                Day12.loadData(filename(day));
                return new int[]{Day12.part1(), Day12.part2()};
            }
            case 13 -> {
                Day13.loadData(filename(day));
                return new int[]{Day13.part1(), Day13.part2()};
            }
            case 14 -> {
                Day14.loadData(filename(day));
                return new int[]{Day14.part1(), Day14.part2()};
            }
            case 15 -> {
                Day15.loadData(filename(day));
                return new int[]{Day15.part1(), Day15.part2()};
            }
            case 16 -> {
                Day16.loadData(filename(day));
                return new int[]{Day16.part1(), Day16.part2()};
            }
            case 17 -> {
                Day17.loadData(filename(day));
                return new int[]{Day17.part1(), Day17.part2()};
            }
            case 18 -> {
                Day18.loadData(filename(day));
                return new int[]{Day18.part1(), Day18.part2()};
            }
            case 19 -> {
                Day19.loadData(filename(day));
                return new int[]{Day19.part1(), Day19.part2()};
            }
            case 20 -> {
                Day20.loadData(filename(day));
                return new int[]{Day20.part1(), Day20.part2()};
            }
            case 21 -> {
                Day21.loadData(filename(day));
                return new int[]{Day21.part1(), Day21.part2()};
            }
            case 22 -> {
                Day22.loadData(filename(day));
                return new int[]{Day22.part1(), Day22.part2()};
            }
            case 23 -> {
                Day23.loadData(filename(day));
                return new int[]{Day23.part1(), Day23.part2()};
            }
            case 24 -> {
                Day24.loadData(filename(day));
                return new int[]{Day24.part1(), Day24.part2()};
            }
            case 25 -> {
                Day25.loadData(filename(day));
                return new int[]{Day25.part1(), Day25.part2()};
            }
            default -> throw new IllegalStateException("Invalid day: " + day);
        }
    }

    public static long[] solve_Time(int day) throws IllegalStateException {
        switch (day) {
            case 1 -> {
                Day1.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day1.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day1.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 2 -> {
                Day2.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day2.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day2.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 3 -> {
                Day3.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day3.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day3.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 4 -> {
                Day4.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day4.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day4.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 5 -> {
                Day5.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day5.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day5.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 6 -> {
                Day6.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day6.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day6.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 7 -> {
                Day7.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day7.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day7.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 8 -> {
                Day8.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day8.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day8.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 9 -> {
                Day9.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day9.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day9.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 10 -> {
                Day10.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day10.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day10.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 11 -> {
                Day11.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day11.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day11.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 12 -> {
                Day12.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day12.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day12.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 13 -> {
                Day13.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day13.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day13.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 14 -> {
                Day14.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day14.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day14.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 15 -> {
                Day15.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day15.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day15.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 16 -> {
                Day16.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day16.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day16.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 17 -> {
                Day17.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day17.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day17.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 18 -> {
                Day18.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day18.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day18.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 19 -> {
                Day19.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day19.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day19.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 20 -> {
                Day20.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day20.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day20.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 21 -> {
                Day21.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day21.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day21.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 22 -> {
                Day22.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day22.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day22.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 23 -> {
                Day23.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day23.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day23.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 24 -> {
                Day24.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day24.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day24.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            case 25 -> {
                Day25.loadData(filename(day));

                long p1_start = System.nanoTime();
                int p1_answer = Day25.part1();
                long p1_end = System.nanoTime();
                long p1_time_ms = (p1_end - p1_start) / 1000000;

                long p2_start = System.nanoTime();
                int p2_answer = Day25.part2();
                long p2_end = System.nanoTime();
                long p2_time_ms = (p2_end - p2_start) / 1000000;

                return new long[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms};
            }
            default -> throw new IllegalStateException("Invalid day: " + day);
        }
    }
}