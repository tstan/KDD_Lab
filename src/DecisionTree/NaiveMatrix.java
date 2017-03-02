package DecisionTree;

import labs.Lab7;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Used for calculating naive bayes decisions
 */
public class NaiveMatrix extends Matrix {
    public static final double LAMBDA = 0.0066666667; //set to 1/n, 1/150 in this case

    public NaiveMatrix(int[][] matrix) {
        super(matrix);
    }

    public ArrayList<Integer> findAllRows() {
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 0; i < this.getMatrix().length; i++) {
            indices.add(i);
        }

        return indices;
    }

    public int getCategoryAttribute() {
        return this.getMatrix()[0].length - 1;
    }

    // takes as input the values for a single row,
    // e.g., 5,3,1,2 and the category,
    // e.g. 2.
    // Returns the probability that the row belongs to the category using the Naïve Bayesian model.
    public double findProb(int[] row, int category) {
        double probability = 0.0;
        int attributeCount[] = new int[row.length];
        int categoryCount = 0;

        ArrayList<Integer> indices = findAllRows();

        // go through all rows
        for (Integer index : indices) {
            int currentRow[] = this.getMatrix()[index];
            if (currentRow[getCategoryAttribute()] == category) {
                categoryCount++;
                // for each attribute in row, add to count if equivalent in examined row
                for (int i = 0; i < attributeCount.length; i++) {
                    if (row[i] == currentRow[i]) {
                        attributeCount[i]++;
                    }
                }
            }
        }

        //initial Pr(C = category)
        probability = (double) categoryCount / (double) indices.size();

        for (int i = 0; i < attributeCount.length; i++) {
            double intermediate = (attributeCount[0] + LAMBDA) /
                    (categoryCount + (LAMBDA * findDifferentValues(i, indices).size()));

            probability *= intermediate;
        }

        return probability;
    }

    public int findCategory(int[] row) {
        int result = -1;
        double probability = -1;

        HashSet<Integer> categories = findDifferentValues(getCategoryAttribute(), findAllRows());

        for (Integer category : categories) {
            double currentProb = findProb(row, category);
            if (currentProb > probability) {
                probability = currentProb;
                result = category;
            }
        }

        return result;
    }

    public static void main(String args[]) throws FileNotFoundException {
        NaiveMatrix matrix = new NaiveMatrix(Lab7.process("data.txt"));

        ArrayList<Integer> indices = matrix.findAllRows();
        int categoryAttr = matrix.getCategoryAttribute();

        System.out.println("Tests finished.");
    }
}
