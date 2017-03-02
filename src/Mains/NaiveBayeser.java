package Mains;

import DecisionTree.NaiveMatrix;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Naive Bayes Algorithm Implementation
 * @since 11/25/2016
 */
public class NaiveBayeser {

    public static int[] userInput() {
        Scanner input = new Scanner(System.in);
        int userChoices[] = new int[4];
        for (int i = 0; i < userChoices.length; i++) {
            System.out.println("Enter value for attribute " + (i+1) + ": ");
            userChoices[i] = input.nextInt();
        }

        return userChoices;
    }

    public static void main(String args[]) throws FileNotFoundException {
        //read in file
        NaiveMatrix naiveMatrix = new NaiveMatrix(DecisionTreer.process("data.txt"));
        int userChoices[] = userInput();

        // do calculation
        int resultClass = naiveMatrix.findCategory(userChoices);

        // print result
        System.out.println("Expected category: " + resultClass);
    }
}
