package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.TextVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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

        //initialize queries
        queries = new DocumentCollection();
        queries.documentCollection("queries.txt", "queries");

        //normalize
        queries.normalize();
        documents.normalize();

        //find cosine distance

        try {
            ArrayList<Integer> closest = queries.getDocumentById(1).findClosestDocuments(documents, new CosineDistance());
            System.out.println("documents for query " + 1 + ": " + closest.toString());
        }
        catch (Exception e) {
            System.out.println("broke at query id" + 1);
        }

    }
}
