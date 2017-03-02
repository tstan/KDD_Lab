package Mains;


import DecisionTree.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author tstan
 * @since 11/03/16
 *
 * decision tree implementation
 */
public class DecisionTreer {

    private static double THRESHOLD = 0.01;

    private static Matrix matrix;

    /**
     * creates a 2-d array from input file
     *
     * @param filename
     * @return
     */
    public static int[][] process(String filename) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> matrixArrayList = new ArrayList<>();

        Scanner input = new Scanner(new File(filename));

        while (input.hasNext()) {
            ArrayList<Integer> row = new ArrayList<>();
            String[] rowValues = input.nextLine().split(",");
            row.add(Double.valueOf(rowValues[0]).intValue());
            row.add(Double.valueOf(rowValues[1]).intValue());
            row.add(Double.valueOf(rowValues[2]).intValue());
            row.add(Double.valueOf(rowValues[3]).intValue());
            row.add(Integer.valueOf(rowValues[4]));

            matrixArrayList.add(row);
        }

        int matrix[][] = new int[matrixArrayList.size()][matrixArrayList.get(0).size()];

        for (int i = 0; i < matrixArrayList.size(); i++) {
            for (int j = 0; j < matrixArrayList.get(i).size(); j++) {
                matrix[i][j] = matrixArrayList.get(i).get(j);
            }
        }

        return matrix;
    }

    /**
     * recursive method that prints the decision tree.
     * It takes as input the data,
     * the set of attributes that have not been used so far in this branch of the tree,
     * the set of rows to examine,
     * the current level (initially 0, use to determine how many tabs to print),
     * and the information gain ratio from last iteration
     * (I set it initially equal to 100, used to create terminating condition).
     *
     * @param attributes
     * @param rows
     * @param level
     */
    public static void printDecisionTree(ArrayList<Integer> attributes,
                                         ArrayList<Integer> rows,
                                         int level) {
        int attributeToSplit = -1;
        double highestIGR = -1.0;

        for (Integer attribute : attributes) {
            double currentIGR = matrix.computeIGR(attribute, rows);
            if (currentIGR > highestIGR) {
                attributeToSplit = attribute;
                highestIGR = currentIGR;
            }
        }

        if (attributeToSplit >= 0 && Math.abs(highestIGR) >= THRESHOLD) {
            attributes.remove(attributes.indexOf(attributeToSplit));
            HashMap<Integer, ArrayList<Integer>> valueMap = matrix.split(attributeToSplit, rows);
            for (Integer key : valueMap.keySet()) {
                for (int i = 0; i < level; i++) {
                    System.out.print("\t");
                }
                System.out.print("When attribute " + (attributeToSplit + 1) + " has value " + key + "\n");
                printDecisionTree(attributes, valueMap.get(key), level + 1);
            }
        }
        else {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.print("value = " + matrix.findMostCommonValue(rows) + "\n");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        matrix = new Matrix(process("data_test.txt"));

        // initial attributes
        ArrayList<Integer> attributes = new ArrayList<>();
        for (int i = 0; i < matrix.getMatrix()[0].length - 1; i++) {
            attributes.add(i);
        }

        // use all rows initially
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < matrix.getMatrix().length; i++) {
            rows.add(i);
        }

        printDecisionTree(attributes, rows, 0);
    }
}
