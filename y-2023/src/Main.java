import General.Helper;

public class Main {
    static String warning = "solveAndPrint_Time() is not the most accurate way to measure the time of a program, because some days do part of the computation during the loadData() method\n";

    public static void main(String[] args) {
        System.out.println(warning);

        try {
            Helper.solveAndPrint_Time(1);
            Helper.solveAndPrint_Time(2);
            Helper.solveAndPrint_Time(3);
            Helper.solveAndPrint_Time(4);
            Helper.solveAndPrint_Time(5);
            Helper.solveAndPrint_Time(6);
            Helper.solveAndPrint_Time(7);
            Helper.solveAndPrint_Time(8);
            Helper.solveAndPrint_Time(9);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}