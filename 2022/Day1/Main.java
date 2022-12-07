import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Main {
    public static void main(String[] args) {
        mainMethod("part2");
    }

    public static void mainMethod(String part) {
        int[] firstThree = new int[3]; // an array to store the top 3 biggest results
        int temp = 0, temp2, answer = 0; // temp and temp2 are used to juggle the ints in the array when adding a new int

        try {
            File caloriesFile = new File("input.txt");
            Scanner myReader = new Scanner(caloriesFile);

            while (myReader.hasNextLine()) {
                String str = myReader.nextLine();

                if (isNumeric(myReader.nextLine())) {
                    temp += Integer.parseInt(str); // if != blank, add the number to the total calc of the elf
                } else {
                    for (int i = 0; i < firstThree.length; i++) { // else -> check whether it should be among the top 3 elves
                        if (temp > firstThree[i]) {
                            temp2 = firstThree[i];
                            firstThree[i] = temp;
                            temp = temp2;
                        }
                    }
                    temp = 0;
                }
            }

            if (part.equals("part1")) {
                answer = firstThree[0];
            } else if (part.equals("part2")) {
                for (int i : firstThree) {
                    answer += i;
                }
            }

            System.out.println(answer);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}