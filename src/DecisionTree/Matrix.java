package DecisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Matrix {

    private static int CLASS_INDEX = 4;

    private int[][] matrix;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Examines specified rows of the array and finds how often a value occurs
     *
     * @param attribute number b/t 0 to 4, 4 is the decision type
     * @param value the value to match
     * @param rows the rows to be examined
     * @return The number of rows in which the element at position `attribute` is equal to `value`
     */
    private int findFrequency(int attribute, int value, ArrayList<Integer> rows) {
        int total = 0;

        for (Integer i : rows) {
            if (matrix[i][attribute] == value) {
                total++;
            }
        }

        return total;
    }

    /**
     * returns the Information Gain Ratio,
     * where we only look at the data defined by the set of rows and we consider splitting on attribute.
     *
     * @param attribute
     * @param rows
     * @return
     */
    public double computeIGR(int attribute, ArrayList<Integer> rows) {

        if (allSame(attribute, rows)) {
            return 0.0;
        }

        double gain = findGain(attribute, rows);

        HashMap<Integer, ArrayList<Integer>> classMap = split(attribute, rows);

        double total = 0;

        for (Integer value : classMap.keySet()) {
            total += classMap.get(value).size();
        }

        double entropy = 0.0;

        for (Integer value : classMap.keySet()) {
            double size = classMap.get(value).size();
            double ratio = size / total;

            entropy += (-ratio * log2(ratio));
        }

        return gain / entropy;
    }

    /**
     * returns the most common category for the dataset that is the defined by the specified rows.
     *
     * @param rows
     * @return
     */
    public int findMostCommonValue(ArrayList<Integer> rows) {
        HashMap<Integer, ArrayList<Integer>> valueMap = split(CLASS_INDEX, rows);

        int largestKey = -1;
        int largestSize = -1;

        for (Integer key : valueMap.keySet()) {
            if (valueMap.get(key).size() > largestSize) {
                largestKey = key;
                largestSize = valueMap.get(key).size();
            }
        }

        return largestKey;
    }

    private boolean allSame(int attribute, ArrayList<Integer> rows) {
        HashMap<Integer, ArrayList<Integer>> classMap = split(attribute, rows);

        int classToMatch = -1;

        for (Integer key : classMap.keySet()) {
            if (classToMatch == -1) {
                classToMatch = findMostCommonValue(classMap.get(key));
            }
            else {
                if (classToMatch != findMostCommonValue(classMap.get(key))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Splits the dataset that is defined by rows on the attribute.
     * Each element of the HashMap that is returned contains the value
     * for the attribute and an ArrayList of rows that have this value.
     *
     * @param attribute
     * @param rows
     * @return Map of the possible values for `attribute` column and list of rows that have that value.
     */
    public HashMap<Integer, ArrayList<Integer>> split(int attribute, ArrayList<Integer> rows) {
        HashMap<Integer, ArrayList<Integer>> valueMap = new HashMap<>();

        for (Integer i : rows) {
            if (!valueMap.containsKey(matrix[i][attribute])) {
                valueMap.put(matrix[i][attribute], new ArrayList<>());
            }

            valueMap.get(matrix[i][attribute]).add(i);
        }

        return valueMap;
    }

    /**
     * Examines only the specified rows of the array.
     * It returns a HashSet of the different values for the specified attribute.
     *
     * @param attribute the attribute to find
     * @param rows the rows to examine
     * @return a hashset of possible values for the attribute
     */
    public HashSet<Integer> findDifferentValues(int attribute, ArrayList<Integer> rows) {
        return rows.stream()
                   .map(i -> matrix[i][attribute])
                   .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Examines only the specified rows of the array.
     * Returns an ArrayList of the rows where the value for the attribute is equal to value.
     *
     * @param attribute
     * @param value
     * @param rows
     * @return an ArrayList of rows that contain `value` for an `attribute`
     */
    private ArrayList<Integer> findRows(int attribute, int value, ArrayList<Integer> rows) {
        return rows.stream()
                   .filter(i -> matrix[i][attribute] == value)
                   .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * calculates log base 2 for `number`
     *
     * @param number
     * @return the log base 2 value of number
     */
    private double log2(double number) {
        return (Math.log(number) / Math.log(2));
    }

    private double calculateEntropy(ArrayList<Integer> subtotal, int total) {
        double entropy = 0.0;
        for (Integer i : subtotal) {
            double ratio = ((double) i / (double) total);

            entropy += (-ratio * log2(ratio));
        }

        return entropy;
    }

    // finds the entropy of the dataset that consists of the specified rows.
    // used to find entropy of decision within a certain amount of rows
    private double findEntropy(ArrayList<Integer> rows) {
        int class_index = CLASS_INDEX;

        HashMap<Integer, ArrayList<Integer>> classMap = split(class_index, rows);

        ArrayList<Integer> subtotals = new ArrayList<>();
        int total = 0;

        for (Integer value : classMap.keySet()) {
            int subtotal = classMap.get(value).size();

            subtotals.add(subtotal);
            total += subtotal;
        }

        return calculateEntropy(subtotals, total);
    }

    //finds the entropy of the dataset that consists of the specified rows after it is partitioned on the attribute.
    private double findEntropy(int attribute, ArrayList<Integer> rows) {

        HashMap<Integer, ArrayList<Integer>> classMap = split(attribute, rows);

        double total = 0;

        for (Integer value : classMap.keySet()) {
            total += classMap.get(value).size();
        }

        double entropy = 0.0;

        for (Integer value : classMap.keySet()) {
            double size = classMap.get(value).size();

            entropy +=  (size/total) * findEntropy(classMap.get(value));
        }

        return entropy;
    }

    // finds the information gain of partitioning on the attribute. Considers only the specified rows.
    private double findGain(int attribute, ArrayList<Integer> rows) {
        double dataEntropy = findEntropy(rows);
        double rowEntropy = findEntropy(attribute, rows);

        return dataEntropy - rowEntropy;
    }
}
