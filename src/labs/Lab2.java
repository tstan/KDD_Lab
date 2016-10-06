package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.TextVector;
import com.sun.javaws.exceptions.InvalidArgumentException;

import javax.xml.soap.Text;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Compute the top 20 documents for each query using the
 * modified version of TF-IDF algorithm and cosine similarity distance
 *
 * @author tstan
 */
public class Lab2 {

    // change this to process more or less queries.
    public static final int TOTAL_TO_PROCESS = 3;

    public static DocumentCollection documents;
    public static DocumentCollection queries;

    public static void main(String args[]) throws IOException, IndexOutOfBoundsException {

        //deserialize docvector from Lab1 into DocumentCollection
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("docvector")))) {
            documents = (DocumentCollection) is.readObject();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        //initialize queries
        queries = new DocumentCollection();
        queries.documentCollection("queries.txt", "queries");

        //normalize
        queries.normalize(documents);
        documents.normalize(documents);

        //calculate cosine distance and print 20 closest.
        queries.printClosestDocsCosineDistance(TOTAL_TO_PROCESS, documents);
    }
}
