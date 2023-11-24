import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        mainMethod("q2"); // "q1" || "q2" for parts 1 and 2
    }

    public static void mainMethod(String part) {
        int answer = 0;
        try {
            File input = new File("input.txt");
            Scanner myReader = new Scanner(input);

            int[] currentRoundArr = new int[2]; // store my rival's and my move for the current round
            int points; // my points for the current round
            while (myReader.hasNextLine()) {
                String currentRoundStr = myReader.nextLine();

                // rival elf's move
                int charComparison = Character.compare(currentRoundStr.charAt(0), 'B'); // use it to identify rock, paper or scissors
                if (charComparison < 0) currentRoundArr[0] = 1; // if rock
                else if (charComparison == 0) currentRoundArr[0] = 2; // if paper
                else currentRoundArr[0] = 3; // if scissors

                // your move
                charComparison = Character.compare(currentRoundStr.charAt(2), 'Y'); // use it to identify rock, paper or scissors
                currentRoundArr[1] = part.equals("q1") ? YourMove_Part1(charComparison) : YourMove_Part2(currentRoundArr, charComparison);

                // draw, win or loss
                points = currentRoundArr[1]; // if loss
                if (currentRoundArr[0] == currentRoundArr[1]) {
                    points = 3 + currentRoundArr[1]; // if draw
                } else if ((currentRoundArr[0] != 3 && (currentRoundArr[1] - currentRoundArr[0]) == 1)
                        || (currentRoundArr[0] == 3 && currentRoundArr[1] == 1)) {
                    points = 6 + currentRoundArr[1]; // if win
                }

                answer += points; // add the points from this round to the final answer
            }
            System.out.println("=== Part " + part.charAt(1) + " ===\nAnswer: " + answer);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int YourMove_Part1(int charComparison) {
        int currentRoundArr = 3; // give scissors (and then check whether to change)

        if (charComparison < 0) currentRoundArr = 1; // if "X", give rock
        else if (charComparison == 0) currentRoundArr = 2; // else if "Y", give paper

        return currentRoundArr;
    }

    public static int YourMove_Part2(int[] currentRoundArr, int charComparison) {
        int currentRoundNum = currentRoundArr[0]; // if "Y", make draw

        if (charComparison < 0) { // if "X", lose
            currentRoundNum = currentRoundArr[0] - 1;
            if (currentRoundNum == 0) currentRoundNum = 3;
        } else if (charComparison > 0) { // else if "Z", win
            currentRoundNum = currentRoundArr[0] + 1;
            if (currentRoundNum == 4) currentRoundNum = 1;
        }

        return currentRoundNum;
    }
}