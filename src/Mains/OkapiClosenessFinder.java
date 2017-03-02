package Mains;

import DocumentClasses.ClosenessMap;
import DocumentClasses.DocumentCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Compare the TF-IDF algorithm to the Okapi BM25 algorithm.
 * First, compute the 20 most relevant documents for each query using the Okapi BM25 algorithm.
 * Use k1=1.2, b=0.75, and k2=100. Save the result in a hash map.
 * The key will be the query number and the value will be an array list of the IDs of the documents
 * that are relevant in ranked order.
 */
public class OkapiClosenessFinder {
    public static final int MAP_LIMIT = 19;

    public static DocumentCollection documents;
    public static DocumentCollection queries;
    public static ClosenessMap cosineClosenessMap;
    public static ClosenessMap okapiClosenessMap;

    public static HashMap<Integer, ArrayList<Integer>> cosineResults;
    public static HashMap<Integer, ArrayList<Integer>> okapiResults;

    // assumes that only relevance degrees 1, 2, 3 are relevant
    private static HashMap<Integer, ArrayList<Integer>> digestHumanJudgmentDoc() throws FileNotFoundException {
        HashMap<Integer, ArrayList<Integer>> humanJudgement = new HashMap<>();
        Scanner scanner = new Scanner(new File("human_judgement.txt"));

        while (scanner.hasNext()) {
            int query = scanner.nextInt();
            int doc = scanner.nextInt();
            int relevance = scanner.nextInt();

            if (relevance > 0 && relevance < 4) {
                if (!humanJudgement.containsKey(query)) {
                    humanJudgement.put(query, new ArrayList<>());
                }
                humanJudgement.get(query).add(doc);
            }
        }

        return humanJudgement;
    }

    /**
     * Computes mean average precision using first 20 queries
     * as specs defined.
     *
     * MAP = average of all precision / relevant documents
     * @param humanJudgment
     * @param distanceResults
     * @return
     */
    public static double computeMAP (HashMap<Integer, ArrayList<Integer>> humanJudgment,
                                     HashMap<Integer, ArrayList<Integer>> distanceResults) {
        // counter to limit at 20
        int count = 0;

        double sumMap = 0.0;

        // go through 20 queries, since multiple queries, take average over ALL 20 at end
        while (count < 20) {
            count++;
            // total of relevant documents
            int totalRelevant = 0;

            // integers to track precision
            int totalReturned = 0;
            int relevantReturned = 0;
            double sumPrecision = 0.0; // relevantReturned/totalReturned
            // get list of relevant documents and returned documents
            ArrayList<Integer> relevantDocs;
            ArrayList<Integer> returnedDocs = distanceResults.get(count);

            if (humanJudgment.containsKey(count)) {
                relevantDocs =  humanJudgment.get(count);
            }
            else {
                totalReturned += returnedDocs.size();
                continue;
            }

            // add total number of relevant documents to sum
            totalRelevant += relevantDocs.size();

            // get precisions
            for (Integer docNum : returnedDocs) {
                totalReturned++;
                if (relevantDocs.contains(docNum)) {
                    relevantReturned++;
                    sumPrecision += ((double)relevantReturned / (double)totalReturned);
                }
            }

            sumMap += sumPrecision/totalRelevant;
        }

        return sumMap / 20.0;
    }

    public static void main(String args[]) throws IOException {

        //re-run lab 2 and 1
        CosineClosenessFinder.main(new String[]{});

        //deserialize objects from CosineClosenessFinder into accessible objects
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("documents_vector")))) {
            documents = (DocumentCollection) is.readObject();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("queries_vector")))) {
            queries = (DocumentCollection) is.readObject();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("cosine_dist_map")))) {
            cosineClosenessMap = (ClosenessMap) is.readObject();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        // commented out to save okapi distances for caching
/*
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("okapi_dist_map")))) {
            okapiClosenessMap = (ClosenessMap) is.readObject();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        okapiResults = okapiClosenessMap.getMap();
*/

        cosineResults = cosineClosenessMap.getMap();

        //calculate top 20 using okapi
        System.out.println("getting okapi distances... might take awhile...");
        double currentTime = System.currentTimeMillis();
        okapiResults = queries.getClosestOkapiDistance(documents);
        CosineClosenessFinder.printElapsed(currentTime);

        //store okapi results into an object and serialize for storage
        ClosenessMap cMap = new ClosenessMap(okapiResults);

        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("okapi_dist_map")))){
            os.writeObject(cMap);
        }
        catch(Exception e){
            System.out.println(e);
        }

        //Compute and print the MAP scores for cosine and okapi.
        computeMapHelper();
    }

    public static void computeMapHelper() throws FileNotFoundException {
        HashMap<Integer, ArrayList<Integer>> humanJudgement = OkapiClosenessFinder.digestHumanJudgmentDoc();

        System.out.println("Cosine MAP = " +
                computeMAP(humanJudgement, cosineResults));
        System.out.println("Okapi MAP = " +
                computeMAP(humanJudgement, okapiResults));
    }
}
