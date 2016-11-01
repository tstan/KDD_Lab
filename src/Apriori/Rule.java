package Apriori;

/**
 * Represents an association rule between itemsets
 */
public class Rule {
    private ItemSet left;
    private ItemSet right;
    private double supportWhole;

    public ItemSet getLeft() {
        return left;
    }

    public void setLeft(ItemSet left) {
        this.left = left;
    }

    public ItemSet getRight() {
        return right;
    }

    public void setRight(ItemSet right) {
        this.right = right;
    }

    public double getSupportWhole() {
        return supportWhole;
    }

    public void setSupportWhole(double supportWhole) {
        this.supportWhole = supportWhole;
    }

    public double getConfidence() {
        return supportWhole / left.getSupport();
    }

    public String toString() {
        return left.toString() + "->" + right.toString();
    }
}
