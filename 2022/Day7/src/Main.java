import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.MAX_VALUE;

public class Main {
    private static boolean isDirectory, isFile, moveIn, moveOut;
    private static final Directory mainDir = new Directory("/", null);
    private static Directory currentDir = mainDir;
    private static ArrayList<Directory> fileTree = new ArrayList<>();
    private static int answerPart1 = 0;
    private static int answerPart2 = MAX_VALUE;

    public static void main(String[] args) {
        mainMethod();
    }

    public static void mainMethod() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);
            myScanner.nextLine(); // get into "/"

            while (myScanner.hasNext()) {
                resetBooleans();

                String thisLine = myScanner.nextLine();
                determineAction(thisLine); // move in, move out, isFile or isDirectory
                if (moveIn) {
                    String getInDirName = thisLine.substring(5);
                    for (Directory childDir : currentDir.getDirectories()) {
                        if (childDir.getDirName().equals(getInDirName)) {
                            currentDir = childDir;
                            break;
                        }
                    }
                } else if (moveOut) {
                    currentDir = currentDir.getParentDir();
                } else if (isFile) {
                    Integer fileSize = Integer.valueOf(thisLine.replaceAll("[^0-9]", ""));
                    String fileName = thisLine.substring(fileSize.toString().length() + 1);
                    currentDir.putFileInDir(fileName, fileSize);
                } else if (isDirectory) {
                    String childDirectoryName = thisLine.substring(4);
                    currentDir.putDirInDir(childDirectoryName);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File with that name was not found");
            fnfe.printStackTrace();
        }

        calcAnswer(mainDir);
        System.out.println("Part1: " + answerPart1);
        System.out.println("Part2: " + answerPart2);
    }

    // Set one of the booleans to true
    private static void determineAction(String thisLine) {
        if (thisLine.startsWith("$")) { // list, move out or move in
            String endOfLine = thisLine.substring(2);
            if (endOfLine.equals("ls")) {
            } else if (endOfLine.equals("cd ..")) moveOut = true;
            else moveIn = true;
        } else { // dir or file
            if (thisLine.substring(0, 3).equals("dir")) isDirectory = true;
            else isFile = true;
        }
    }

    // Calculate the answers for part 1 and 2
    private static void calcAnswer(Directory dirToAdd) {
        fileTree.add(dirToAdd);
        int dirToAddSize = dirToAdd.getSize();

        // part 1
        if (dirToAddSize <= 100000) answerPart1 += dirToAddSize;

        // part 2
        if (dirToAddSize >= 8381165 && dirToAddSize < answerPart2) answerPart2 = dirToAddSize;

        // continue traversing
        if (dirToAdd.getDirectories().size() != 0) {
            for (Directory dirToAddChildDir : dirToAdd.getDirectories()) {
                calcAnswer(dirToAddChildDir);
            }
        }
    }

    // Reset the boolean vars on every turn
    public static void resetBooleans() {
        isDirectory = false;
        isFile = false;
        moveIn = false;
        moveOut = false;
    }
}