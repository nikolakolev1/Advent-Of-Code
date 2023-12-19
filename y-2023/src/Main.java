import General.Helper;

public class Main {
    static String warning = "solveAndPrint_Time() is not the most accurate way to measure the time of a program, because some days do part of the computation during the loadData() method\n";

    public static void main(String[] args) {
        solveAll_Time();
//        solveAll();
    }

    private static void solveAll_Time() {
        System.out.println(warning);

        for (int i = 1; i <= 19; i++) {
            Helper.solveAndPrint_Time(i);
        }
    }

    private static void solveAll() {
        for (int i = 1; i <= 19; i++) {
            Helper.solveAndPrint(i);
        }
    }
}