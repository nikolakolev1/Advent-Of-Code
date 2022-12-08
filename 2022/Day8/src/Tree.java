public class Tree {
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

    public boolean checkVisLeft() {
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

    public boolean checkVisRight() {
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

    public boolean checkVisTop() {
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

    public boolean checkVisBottom() {
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

    public void scenicLeft() {
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

    public void scenicRight() {
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

    public void scenicTop() {
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

    public void scenicBottom() {
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