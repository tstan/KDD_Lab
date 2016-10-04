package labs;

import DocumentClasses.DocumentCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Compute the top 20 documents for each query using the
 * modified version of TF-IDF algorithm and cosine similarity distance
 *
 * @author tstan
 */
public class Lab2 {

    public static DocumentCollection documents;

    public static DocumentCollection queries;

    public static void main(String args[]) throws IOException {

        //deserialize docvector from Lab1 into DocumentCollection
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("docvector")))) {
            documents = (DocumentCollection) is.readObject();
        }
        catch (Exception e) {
            System.out.println(e);
        }


    }
}
