package Helpers;

public class Helper {
    public static String filename(int day) {
        return "data/day" + day + "/input.txt";
    }

    public static void printAnswer(int part, int asnwer) {
        System.out.println("=== Part " + part + " ===\nAnswer: " + asnwer + "\n");
    }
}