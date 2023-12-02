import Helpers.Helper;

public class Main {
    public static void main(String[] args) {
        try {
            Helper.solveAndPrint_Time(1);
            Helper.solveAndPrint_Time(2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}