import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        mainMethod(2);
    }

    public static void mainMethod(int part) {
        int answer = 0;
        HashSet<Character> compareHS = new HashSet<>();

        try {
            File input = new File("input.txt");
            Scanner myReader = new Scanner(input);

            while (myReader.hasNextLine()) {
                if (part == 1) answer = part1(myReader, compareHS, answer); // calc for part1
                else answer = part2(myReader, compareHS, answer); // or for part2 of the task
                compareHS.clear(); // clear the set on every turn
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("=== Part " + part + " ===\nAnswer: " + answer);
    }

    public static int part1(Scanner myReader, HashSet<Character> compareHS, int answer) {
        String currentLine = myReader.nextLine(); // get current line

        for (int i = 0; i < currentLine.length() / 2; i++) {
            compareHS.add(currentLine.charAt(i)); // add every unique char of the first half of the line to the set
        }

        for (int i = currentLine.length() / 2; i < currentLine.length(); i++) {
            if (compareHS.contains(currentLine.charAt(i))) { // find the duplicate in the two backpack compartments
                char temp = currentLine.charAt(i);
                answer += Character.isUpperCase(temp) ? (int) temp - 38 : (int) temp - 96; // calculate the result (if the char is capital or not)
                break;
            }
        }

        return answer;
    }

    public static int part2(Scanner myReader, HashSet<Character> compareHS, int answer) {
        String elf1 = myReader.nextLine(), elf2 = myReader.nextLine(), elf3 = myReader.nextLine(); // get next three lines

        for (int i = 0; i < elf1.length(); i++) {
            compareHS.add(elf1.charAt(i)); // add every unique char of the first line (elf) to the set
        }

        outerLoop:
        for (int i = 0; i < elf2.length(); i++) {
            if (compareHS.contains(elf2.charAt(i))) { // find the duplicates between the first and second lines
                char temp = elf2.charAt(i);
                for (int j = 0; j < elf3.length(); j++) { // see if that char is also present in line three
                    if (temp == elf3.charAt(j)) { // if present in all elves backpacks:
                        answer += Character.isUpperCase(temp) ? (int) temp - 38 : (int) temp - 96; // calculate the result (if the char is capital or not)
                        break outerLoop;
                    }
                }
            }
        }

        return answer;
    }
}