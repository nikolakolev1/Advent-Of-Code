import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        mainMethod("part1"); // "part1" || or "part2"
    }

    public static void mainMethod(String part) {
        int answer = 0;

        try {
            File input = new File("input.txt");
            Scanner myReader = new Scanner(input);

            while (myReader.hasNextLine()) {
                int[] elves = new int[4]; // an array to store the ranges of both elves
                String[] elvesStr = myReader.nextLine().split(",|-"); // get the ranges into the String array
                for (int i = 0; i < elvesStr.length; i++) {
                    elves[i] = Integer.parseInt(elvesStr[i]); // convert them to ints in the main array
                }

                if (part.equals("part1")) answer = part1(elves) ? answer + 1 : answer; // answer if part1
                else if (part.equals("part2")) answer = part2(elves) ? answer + 1 : answer; // or part 2
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println(answer);
    }

    public static boolean part1(int[] elves) {
        return (elves[0] <= elves[2] && elves[1] >= elves[3]) || (elves[0] >= elves[2] && elves[1] <= elves[3]);
    } // return true if one of the ranges is fully inside the other

    public static boolean part2(int[] elves) {
        return ((elves[0] >= elves[2] && elves[0] <= elves[3]) || (elves[1] >= elves[2] && elves[1] <= elves[3])) ||
                ((elves[2] >= elves[0] && elves[2] <= elves[1]) || (elves[3] >= elves[0] && elves[3] <= elves[1]));
    } // return true if there is an overlap between the two ranges
}