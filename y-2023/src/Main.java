import General.Helper;

public class Main {
    public static void main(String[] args) {
        try {
            Helper.solveAndPrint_Time(1);
            Helper.solveAndPrint_Time(2);
            Helper.solveAndPrint_Time(3);
            Helper.solveAndPrint_Time(4);
            Helper.solveAndPrint_Time(5);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}