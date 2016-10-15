package labs;

import DocumentClasses.ClosenessMap;
import DocumentClasses.DocumentCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Compute the top 20 documents for each query using the
 * modified version of TF-IDF algorithm and cosine similarity distance
 *
 * @author tstan
 */
public class Lab2 {

    // change this to process more or less queries.
    public static final int TOTAL_TO_PROCESS = 21;

    public static DocumentCollection documents;
    public static DocumentCollection queries;

    public static void printElapsed(double prev) {
        System.out.println("Finished in " +
                Double.toString((System.currentTimeMillis() - prev) / 1000) + " sec !");
    }

    public static void main(String args[]) throws IOException, IndexOutOfBoundsException {

        Lab1.main(new String[]{});

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
        System.out.println("Normalizing documents and queries...");
        double currentTime = System.currentTimeMillis();
        queries.normalize(documents);
        documents.normalize(documents);
        Lab2.printElapsed(currentTime);

        //calculate cosine distance and print 20 closest, return hashmap to be used for part 3.
        System.out.println("Calculating cosine distances...");
        currentTime = System.currentTimeMillis();
        HashMap<Integer, ArrayList<Integer>> qToDocMap =
                queries.printClosestDocsCosineDistance(TOTAL_TO_PROCESS, documents);
        Lab2.printElapsed(currentTime);

        ClosenessMap closenessMap = new ClosenessMap(qToDocMap);

        /* serialize document collection to file(s) */
        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("documents_vector")))){
            os.writeObject(documents);
        }
        catch(Exception e){
            System.out.println(e);
        }
        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("queries_vector")))){
            os.writeObject(queries);
        }
        catch(Exception e){
            System.out.println(e);
        }
        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("cosine_dist_map")))){
            os.writeObject(closenessMap);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
