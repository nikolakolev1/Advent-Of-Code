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

public class Main {
    public static void main(String[] args) {
        solve(1);
        solve(2);
    }

    private static void solve(int day) {
        String[] args = new String[0];

        System.out.println("*** Day " + day + " ***");

        switch (day) {
            case 1 -> Day1.main(args);
            case 2 -> Day2.main(args);
            case 3 -> Day3.main(args);
            case 4 -> Day4.main(args);
            case 5 -> Day5.main(args);
            case 6 -> Day6.main(args);
            case 7 -> Day7.main(args);
            case 8 -> Day8.main(args);
            case 9 -> Day9.main(args);
            case 10 -> Day10.main(args);
            case 11 -> Day11.main(args);
            case 12 -> Day12.main(args);
            case 13 -> Day13.main(args);
            case 14 -> Day14.main(args);
            case 15 -> Day15.main(args);
            case 16 -> Day16.main(args);
            case 17 -> Day17.main(args);
            case 18 -> Day18.main(args);
            case 19 -> Day19.main(args);
            case 20 -> Day20.main(args);
            case 21 -> Day21.main(args);
            case 22 -> Day22.main(args);
            case 23 -> Day23.main(args);
            case 24 -> Day24.main(args);
            case 25 -> Day25.main(args);
            default -> throw new IllegalStateException("Unexpected value: " + day);
        }

        System.out.println("--------------------\n");
    }
}