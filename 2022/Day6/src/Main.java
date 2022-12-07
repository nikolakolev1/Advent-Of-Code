import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.*;
//import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        mainMethod("part1");
    }

    public static void mainMethod(String part) {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

//            ArrayList<Character> characters= new ArrayList<>();
            Character[] currentQueue = new Character[14];
            if (myScanner.hasNext()) {
                String test = myScanner.next();
//                System.out.println(test);
//                System.out.println("!!!!!!!!!");
                String[] characters = test.split("");
                for (int i = 0; i < 14; i++) {
                    currentQueue[i] = (characters[i].charAt(0));
                }


                // check for repetition
                for (int i = 14; i < characters.length; i++) {
                    int something = 0;
                    for (int j = 0; j < 14; j++) {
                        char temp = currentQueue[j];
                        for (int k = j + 1; k < 14; k++) {
                            if (currentQueue[k] != temp) {
                                something++;
                            }
                        }
                    }

                    if (something == 91) {
                        System.out.println(i);
                        return;
                    }

                    int[] copy = new int[13];
                    for (int j = 1; j < 14; j++) {
                        currentQueue[j-1] = currentQueue[j];
                    }
                    currentQueue[13] = (characters[i].charAt(0));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}