import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Integer> valuesAtTimestamps = new ArrayList<>();
    static int counter = 1;
    static int x = 1;
    static StringBuilder part2Str = new StringBuilder();
    static int counterSubtract = 0;

    public static void main(String[] args) {
        System.out.println("=== Part 2 ===");

        loadData();

        System.out.println();

        int answerP1 = 0;
        for (Integer i : valuesAtTimestamps) {
            answerP1 += i;
        }
        System.out.println("=== Part 1 ===\nAnswer: " + answerP1);
    }

    private static void loadData() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();
                mainMethod(thisLine);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mainMethod(String thisLine) {
        mapBuilderEvaluatePixel();

        counter++;
        checkIfToRecord();

        if (thisLine.startsWith("a")) {
            mapBuilderEvaluatePixel();

            thisLine = thisLine.substring(5);
            x += Integer.parseInt(thisLine);

            counter++;
            checkIfToRecord();
        }
    }

    private static void mapBuilderEvaluatePixel() {
        int pixelIndex = counter - 1 - counterSubtract;

        if (pixelIndex >= x - 1 && pixelIndex <= x + 1) part2Str.append("#");
        else part2Str.append(".");

        checkIfToPrint();
    }

    private static void checkIfToRecord() {
        if (counter == 20 || counter == 60 || counter == 100 || counter == 140 || counter == 180 || counter == 220) {
            valuesAtTimestamps.add(counter * x);
        }
    }

    private static void checkIfToPrint() {
        if (counter == 40 || counter == 80 || counter == 120 || counter == 160 || counter == 200 || counter == 240) {
            System.out.println(part2Str);
            part2Str = new StringBuilder();
            counterSubtract += 40;
        }
    }
}