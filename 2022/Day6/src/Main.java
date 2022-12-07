import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    // Sorry for how unreadable that is
    public static void main(String[] args) {
        mainMethod("part1"); // "part1" || whatever string
    }

    public static void mainMethod(String partStr) {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            int partInt = partStr.equals("part1") ? 4 : 14;
            Character[] currentChars = new Character[partInt];
            if (myScanner.hasNext()) {
                String test = myScanner.next();
                String[] characters = test.split("");
                for (int i = 0; i < partInt; i++) {
                    currentChars[i] = (characters[i].charAt(0));
                }

                // check for repetition
                for (int i = partInt; i < characters.length; i++) {
                    int counter = 0;
                    for (int j = 0; j < partInt; j++) {
                        char temp = currentChars[j];
                        for (int k = j + 1; k < partInt; k++) {
                            if (currentChars[k] != temp) {
                                counter++;
                            }
                        }
                    }

                    if (counter == sumOfSmallerInts(partInt)) {
                        System.out.println(i);
                        return;
                    }

                    for (int j = 1; j < partInt; j++) {
                        currentChars[j - 1] = currentChars[j];
                    }
                    currentChars[partInt - 1] = (characters[i].charAt(0));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int sumOfSmallerInts(int bigInt) {
        int sum = 0;
        for (int i = 1; i < bigInt; i++) {
            sum += i;
        }
        return sum;
    }
}