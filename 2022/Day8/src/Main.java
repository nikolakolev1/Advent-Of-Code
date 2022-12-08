import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<ArrayList<Tree>> forest = new ArrayList<>();

    public static void main(String[] args) {
        mainMethod();
    }

    public static void mainMethod() {
        loadData();
        provideNeighbours();

        int answerP1 = 0, answerP2 = 0;
        for (ArrayList<Tree> trees : forest) {
            for (int j = 0; j < forest.get(0).size(); j++) {
                if (trees.get(j).isVisible()) {
                    answerP1++;
                }
                int scenicScore = trees.get(j).scenicScore();
                if (scenicScore > answerP2) {
                    answerP2 = scenicScore;
                }
            }
        }
        System.out.println("Part 1: " + answerP1);
        System.out.println("Part 2: " + answerP2);
    }

    public static void loadData() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            int row = -1;
            while (myScanner.hasNextLine()) {
                String thisRow = myScanner.nextLine();
                row++;
                forest.add(new ArrayList<>());

                for (int i = 0; i < thisRow.length(); i++) {
                    forest.get(row).add(new Tree(Integer.parseInt(thisRow.substring(i, i + 1))));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void provideNeighbours() {
        int rowSize = forest.get(0).size();

        for (int i = 0; i < forest.size(); i++) {
            for (int j = 0; j < rowSize; j++) {
                Tree leftNeigh = null;
                Tree rightNeigh = null;
                Tree topNeigh = null;
                Tree bottomNeigh = null;
                if (j > 0) {
                    leftNeigh = forest.get(i).get(j - 1);
                }
                if (j < rowSize - 1) {
                    rightNeigh = forest.get(i).get(j + 1);
                }
                if (i > 0) {
                    topNeigh = forest.get(i - 1).get(j);
                }
                if (i < forest.size() - 1) {
                    bottomNeigh = forest.get(i + 1).get(j);
                }
                forest.get(i).get(j).assignNeighbours(leftNeigh, rightNeigh, topNeigh, bottomNeigh);
            }
        }
    }
}