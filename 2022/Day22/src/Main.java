import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<ArrayList<String>> map;
    private static ArrayList<ArrayList<String>> instructions;

    public static void main(String[] args) {
        loadData("input2.txt");
        System.out.println("Hello world!");
    }

    private static void loadData(String file) {
        try {
            File input = new File(file);
            Scanner myScanner = new Scanner(input);

            boolean readMap = true;
            map = new ArrayList<>();
            instructions = new ArrayList<>();

            while (myScanner.hasNextLine()) {
                String thisline = myScanner.nextLine();

                if (thisline.isEmpty()) {
                    readMap = false;
                    thisline = myScanner.nextLine();
                }

                if (readMap) {
                    ArrayList<String> row = new ArrayList<>();

                    String[] thisLineArr = thisline.split("");
                    for (int i = 0; i < thisLineArr.length; i++) {
                        if (thisLineArr[i].equals(" ")) {
                            row.add("#");
                        } else {
                            row.add(thisLineArr[i]);
                        }
                    }
                } else {
                    String[] thisLineArr = thisline.split("");

                }
            }

            myScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}