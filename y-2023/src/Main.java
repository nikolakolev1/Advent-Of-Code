import General.Helper;

public class Main {
    static String warning = "solveAndPrint_Time() is not the most accurate way to measure the time of a program, because some days do part of the computation during the loadData() method\n";
    static String warning2 = "Day 20, part 2 is slower, because it includes a reset and reload of the data\n";

    public static void main(String[] args) {
        solveAll_Time();
//        solveAll();
    }

    private static void solveAll_Time() {
        System.out.println("1) " + warning + "2) " + warning2);

        for (int i = 1; i <= 20; i++) {
            Helper.solveAndPrint_Time(i);
        }
    }

    private static void solveAll() {
        for (int i = 1; i <= 20; i++) {
            Helper.solveAndPrint(i);
        }
    }
}