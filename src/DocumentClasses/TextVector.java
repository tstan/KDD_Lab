package DocumentClasses;

import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author tstan
 */
public abstract class TextVector implements Serializable {

    /* key is word and value is frequency*/
    public HashMap<String, Integer> rawVector = new HashMap<>();
    public static final int BASE_TWO = 2;

    /**
     * returns entry set of rawVector
     *
     * @return
     */
    public Set<Entry<String, Integer>> getRawVectorEntrySet() {
        return rawVector.entrySet();
    }

    /**
     * adds a word to the rawVector
     *
     * @param word
     */
    public void add(String word) {
        if (rawVector.containsKey(word)) {
            rawVector.replace(word, rawVector.get(word) + 1);
        } else {
            rawVector.put(word, 1);
        }
    }

    /**
     * checks if rawVector contains searchWord
     *
     * @param searchWord
     * @return
     */
    public boolean contains(String searchWord) {
        return rawVector.containsKey(searchWord);
    }

    /**
     * returns frequency # of a word
     *
     * @param word
     * @return
     */
    public int getRawFrequency(String word) {
        if (rawVector.containsKey(word)) {
            return rawVector.get(word);
        }
        return 0;
    }

    /**
     * returns total number of words stored
     *
     * @return
     */
    public int getTotalWordCount() {
        return rawVector.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * returns # of distinct words that are stored
     *
     * @return
     */
    public int getDistinctWordCount() {
        return rawVector.size();
    }

    /**
     * returns the highest frequency number
     *
     * @return
     */
    public int getHighestRawFrequency() {

        try {
            int test = rawVector.values().stream().mapToInt(Integer::intValue).max().getAsInt();
            return test;
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    /**
     * returns word with highest frequency
     *
     * @return
     */
    public String getMostFrequentWord() {
        return getRawVectorEntrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    /**
     * Returns the normalized frequency for each word in the text vector
     *
     * @return
     */
    public abstract Set<Entry<String,Double>> getNormalizedVectorEntrySet();

    /**
     * Normalizes the frequency of each word using TF-IDF formula
     *
     * @param documents
     */
    public abstract void normalize(DocumentCollection documents);

    /**
     * Returns the normalized frequency of the word
     *
     * @param word
     * @return
     */
    public abstract double getNormalizedFrequency(String word);

    /**
     * Calls this.getNormalizedFrequency to get normalized frequencies.
     *
     * @return the square root of the sum of the squares of the frequencies.
     */
    double getL2Norm() {
        double frequencySum = 0.0;

        for (String word : rawVector.keySet()) {
            double currentFrequency = getNormalizedFrequency(word);
            frequencySum += (currentFrequency * currentFrequency);
        }

        return Math.sqrt(frequencySum);
    }

    /**
     * Calls method findDistance on distanceAlg variable to return closest docs.
     *
     * @param documents
     * @return the 20 closest documents' IDs in a list.
     */
    ArrayList<Integer> findClosestDocuments(DocumentCollection documents, DocumentDistance distanceAlg) {
        HashMap<Integer, Double> distanceMap = new HashMap<>();
        for (Integer i : documents.documents.keySet()) {

            // There are documents that contain no text.
            // If a document is empty, the distance from it to any query should be equal to 0
            if (documents.getDocumentById(i).getTotalWordCount() > 0) {
                distanceMap.put(i, distanceAlg.findDistance(this, documents.getDocumentById(i), documents));
            }
            else {
                distanceMap.put(i, 0.0);
            }
        }

        List<Map.Entry<Integer, Double>> list = distanceMap.entrySet().stream().collect(Collectors.toList());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        list.stream().limit(20).collect(Collectors.toList());
        ArrayList<Integer> ids = list.stream()
                .map(Entry::getKey)
                .limit(20)
                .collect(Collectors.toCollection(ArrayList::new));

        return ids;
    }

    double log(double x, double base)
    {
        return (Math.log(x) / Math.log(base));
    }
}
