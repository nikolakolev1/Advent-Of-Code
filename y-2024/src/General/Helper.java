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
        return "data/day" + day + "/input_test.txt";
    }

    public static void solveAndPrint(int dayInt) {
        String[] answers = solve(dayInt);
        System.out.println("*** Day " + dayInt + " ***");
        printAnswer(1, answers[0]);
        printAnswer(2, answers[1]);
        System.out.println();
    }

    public static void solveAndPrint_Time(int dayInt) {
        String[] answers = solve_Time(dayInt);
        System.out.println("*** Day " + dayInt + " ***");
        System.out.println("Parse time: " + answers[4] + "ms");
        printAnswer(1, answers[P1], answers[P1_TIME]);
        printAnswer(2, answers[P2], answers[P2_TIME]);
        System.out.println();
    }

    public static void printAnswer(int part, String answer) {
        System.out.println("Part " + part + " : " + answer);
    }

    public static void printAnswer(int part, String answer, String time) {
        System.out.println("Part " + part + ": " + answer + " (" + time + "ms)");
    }

    private static Day getDay(int day) {
        return switch (day) {
            case 1 -> new Day1();
//            case 2 -> new Day2();
//            case 3 -> new Day3();
//            case 4 -> new Day4();
//            case 5 -> new Day5();
//            case 6 -> new Day6();
//            case 7 -> new Day7();
//            case 8 -> new Day8();
//            case 9 -> new Day9();
            default -> throw new IllegalStateException("Invalid day: " + day);
        };
    }

    private static String[] solve(int dayInt) {
        Day day = getDay(dayInt);

        day.loadData(filename(dayInt));

        return new String[]{day.part1(), day.part2()};
    }

    private static String[] solve_Time(int dayInt) {
        Day day_p1 = getDay(dayInt);
        Day day_p2 = getDay(dayInt);

        long parse_start = System.nanoTime();
        day_p1.loadData(filename(dayInt));
        long parse_end = System.nanoTime();
        String parse_time_ms = String.valueOf((parse_end - parse_start) / 1000000);

        long p1_start = System.nanoTime();
        String p1_answer = day_p1.part1();
        long p1_end = System.nanoTime();
        String p1_time_ms = String.valueOf((p1_end - p1_start) / 1000000);

        day_p2.loadData(filename(dayInt));

        long p2_start = System.nanoTime();
        String p2_answer = day_p2.part2();
        long p2_end = System.nanoTime();
        String p2_time_ms = String.valueOf((p2_end - p2_start) / 1000000);

        return new String[]{p1_answer, p2_answer, p1_time_ms, p2_time_ms, parse_time_ms};
    }
}