import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<ArrayList<Tree>> forest = new ArrayList<>();

    public static void main(String[] args) {
        mainMethod();
    }

    private static void mainMethod() {
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

    private static void loadData() {
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

    private static void provideNeighbours() {
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

class Tree {
    private final int height;
    private boolean visFrLeft, visFrRight, visFrTop, visFrBottom;
    public Tree leftNeighbour, rightNeighbour, topNeighbour, bottomNeighbour;

    public Tree(int height) {
        this.height = height;
    }

    public void assignNeighbours(Tree leftN, Tree rightN, Tree topN, Tree bottomN) {
        leftNeighbour = leftN;
        rightNeighbour = rightN;
        topNeighbour = topN;
        bottomNeighbour = bottomN;
    }

    private int maxHeightToL = 0;

    private boolean checkVisLeft() {
        Tree leftN = leftNeighbour;
        while (leftN != null) {
            if (leftN.height >= maxHeightToL) {
                maxHeightToL = leftN.height;
                if (maxHeightToL >= height) {
                    return false;
                }
            }
            leftN = leftN.leftNeighbour;
        }
        return true;
    }

    private int maxHeightToR;

    private boolean checkVisRight() {
        Tree rightN = rightNeighbour;
        while (rightN != null) {
            if (rightN.height >= maxHeightToR) {
                maxHeightToR = rightN.height;
                if (maxHeightToR >= height) {
                    return false;
                }
            }
            rightN = rightN.rightNeighbour;
        }
        return true;
    }

    private int maxHeightToT;

    private boolean checkVisTop() {
        Tree topN = topNeighbour;
        while (topN != null) {
            if (topN.height >= maxHeightToT) {
                maxHeightToT = topN.height;
                if (maxHeightToT >= height) {
                    return false;
                }
            }
            topN = topN.topNeighbour;
        }
        return true;
    }

    private int maxHeightToB;

    private boolean checkVisBottom() {
        Tree bottomN = bottomNeighbour;
        while (bottomN != null) {
            if (bottomN.height >= maxHeightToB) {
                maxHeightToB = bottomN.height;
                if (maxHeightToB >= height) {
                    return false;
                }
            }
            bottomN = bottomN.bottomNeighbour;
        }
        return true;
    }

    public void checkVisibilities() {
        visFrLeft = false;
        visFrRight = false;
        visFrTop = false;
        visFrBottom = false;
        if (checkVisLeft()) {
            visFrLeft = true;
        } else if (checkVisRight()) {
            visFrRight = true;
        } else if (checkVisTop()) {
            visFrTop = true;
        } else if (checkVisBottom()) {
            visFrBottom = true;
        }
    }

    private int scenicToL = 0;

    private void scenicLeft() {
        Tree leftN = leftNeighbour;
        while (leftN != null) {
            scenicToL++;
            if (leftN.height >= height) {
                return;
            }
            leftN = leftN.leftNeighbour;
        }
    }

    private int scenicToR = 0;

    private void scenicRight() {
        Tree rightN = rightNeighbour;
        while (rightN != null) {
            scenicToR++;
            if (rightN.height >= height) {
                return;
            }
            rightN = rightN.rightNeighbour;
        }
    }

    private int scenicToT = 0;

    private void scenicTop() {
        Tree topN = topNeighbour;
        while (topN != null) {
            scenicToT++;
            if (topN.height >= height) {
                return;
            }
            topN = topN.topNeighbour;
        }
    }

    private int scenicToB = 0;

    private void scenicBottom() {
        Tree bottomN = bottomNeighbour;
        while (bottomN != null) {
            scenicToB++;
            if (bottomN.height >= height) {
                return;
            }
            bottomN = bottomN.bottomNeighbour;
        }
    }

    public int scenicScore() {
        scenicLeft();
        scenicRight();
        scenicTop();
        scenicBottom();
        return scenicToL * scenicToR * scenicToT * scenicToB;
    }

    public boolean isVisible() {
        checkVisibilities();
        return (visFrLeft || visFrRight) || (visFrBottom || visFrTop);
    }
}