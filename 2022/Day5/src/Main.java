import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String part = "part1"; // "part1" || or 'doesn't matter what'

        try {
            File input = new File("input.txt");
            Scanner myReader = new Scanner(input);

            // create the stacks table with 9 stacks
            String thisLine = myReader.nextLine() + " ";
            ArrayList<ArrayList<Character>> stacks = new ArrayList<>();
            for (int i = 0; i < thisLine.length() / 4; i++) {
                stacks.add(new ArrayList<>());
            }

            // add the stack table to a temporary list (in the form of Strings)
            ArrayList<String> tempList = new ArrayList<>();
            while (thisLine.charAt(1) != (char) 49) {
                tempList.add(thisLine);
                thisLine = myReader.nextLine() + " ";
            }

            // transfer the table to the stack list, converting the crates to chars
            for (String s : tempList) {
                int stackIndex = 0;
                for (int i = 0; i < s.length() - 3; i += 4) {
                    String crate = s.substring(i, i + 3);
                    if (!crate.equals("   ")) {
                        stacks.get(stackIndex).add(0, crate.charAt(1));
                    }
                    stackIndex++;
                }
            }

            myReader.nextLine(); // skip the empty line

            // follow the instructions
            while (myReader.hasNextLine()) {
                // set moveAmount, fromStack and toStack for readability
                String[] instructions = myReader.nextLine().split("move | from | to");
                int moveAmount = Integer.parseInt(instructions[1]);
                ArrayList<Character> fromStack = stacks.get((Integer.parseInt(instructions[2])) - 1);
                ArrayList<Character> toStack = stacks.get((Integer.parseInt(instructions[3].strip())) - 1);

                // move the crates: one by one (part1) || all at once (part2)
                for (int i = 0; i < moveAmount; i++) {
                    Character cratesToMove = part.equals("part1") ? fromStack.get(fromStack.size() - 1) : fromStack.get(fromStack.size() - (moveAmount - i));
                    if (part.equals("part1")) fromStack.remove(fromStack.size() - 1);
                    else fromStack.remove(fromStack.size() - (moveAmount - i));
                    toStack.add(cratesToMove);
                }
            }

            // print answer
            System.out.print(part + ": ");
            for (ArrayList<Character> stack : stacks) System.out.print(stack.get(stack.size() - 1));

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}