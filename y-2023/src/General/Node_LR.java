package General;

public class Node_LR {
    public Node_LR left;
    public Node_LR right;
    public String value;

    public Node_LR(String value) {
        this.value = value;
    }

    public Node_LR(String value, Node_LR left, Node_LR right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public void addLeft(Node_LR node) {
        this.left = node;
    }

    public void addRight(Node_LR node) {
        this.right = node;
    }
}