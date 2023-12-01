package Helpers;

public class Helper {
    public static String filename(int day) {
        return "data/day" + day + "/input.txt";
    }

    public static void printAnswer(int part, int answer) {
        System.out.println("=== Part " + part + " ===\nAnswer: " + answer + "\n");
    }

    public static void printAnswer(int part, int answer, long time) {
        System.out.println("=== Part " + part + " ===\nAnswer: " + answer + " (" + time + "ms)\n");
    }
}