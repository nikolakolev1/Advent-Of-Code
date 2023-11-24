import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<ArrayList<Tree>> forest = new ArrayList<>();

    public static void main(String[] args) {
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

        System.out.println("=== Part 1 ===\nAnswer: " + answerP1);
        System.out.println();
        System.out.println("=== Part 2 ===\nAnswer: " + answerP2);
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
                Tree leftNeigh = null, rightNeigh = null, topNeigh = null, bottomNeigh = null;

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

    private boolean checkVisLeft() {
        int maxHeightToL = 0;

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

    private boolean checkVisRight() {
        int maxHeightToR = 0;

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

    private boolean checkVisTop() {
        int maxHeightToT = 0;

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

    private boolean checkVisBottom() {
        int maxHeightToB = 0;

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

    private int scenicLeft() {
        int scenicToL = 0;

        Tree leftN = leftNeighbour;
        while (leftN != null) {
            scenicToL++;
            if (leftN.height >= height) {
                break;
            }
            leftN = leftN.leftNeighbour;
        }

        return scenicToL;
    }

    private int scenicRight() {
        int scenicToR = 0;

        Tree rightN = rightNeighbour;
        while (rightN != null) {
            scenicToR++;
            if (rightN.height >= height) {
                break;
            }
            rightN = rightN.rightNeighbour;
        }

        return scenicToR;
    }

    private int scenicTop() {
        int scenicToT = 0;

        Tree topN = topNeighbour;
        while (topN != null) {
            scenicToT++;
            if (topN.height >= height) {
                break;
            }
            topN = topN.topNeighbour;
        }

        return scenicToT;
    }

    private int scenicBottom() {
        int scenicToB = 0;

        Tree bottomN = bottomNeighbour;
        while (bottomN != null) {
            scenicToB++;
            if (bottomN.height >= height) {
                break;
            }
            bottomN = bottomN.bottomNeighbour;
        }

        return scenicToB;
    }

    public int scenicScore() {
        int scenicToL = scenicLeft();
        int scenicToR = scenicRight();
        int scenicToT = scenicTop();
        int scenicToB = scenicBottom();
        return scenicToL * scenicToR * scenicToT * scenicToB;
    }

    public boolean isVisible() {
        checkVisibilities();
        return (visFrLeft || visFrRight) || (visFrBottom || visFrTop);
    }
}