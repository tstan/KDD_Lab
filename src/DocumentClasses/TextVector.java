package DocumentClasses;

import java.util.*;
import java.io.*;

/**
 * @author tstan
 */
public abstract class TextVector implements Serializable {

    /* key is word and value is frequency*/
    public HashMap<String, Integer> rawVector = new HashMap<>();

    /**
     * returns entry set of rawVector
     *
     * @return
     */
    public Set<Map.Entry<String, Integer>> getRawVectorEntrySet() {
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
        return rawVector.get(word);
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
            return rawVector.values().stream().mapToInt(Integer::intValue).max().getAsInt();
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
    public abstract Set<Map.Entry<String,Double>> getNormalizedVectorEntrySet();

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
    public double getL2Norm() {
        double frequencySum = 0.0;

        for (String word : rawVector.keySet()) {
            double currentFrequency = getNormalizedFrequency(word);
            frequencySum += currentFrequency * currentFrequency;
        }

        return Math.sqrt(frequencySum);
    }

    /**
     * Calls method findDistance on distanceAlg variable to return closest docs.
     *
     * @param documents
     * @return the 20 closest documents' IDs in a list.
     */
    public ArrayList<Integer> findClosestDocuments(DocumentCollection documents) {
        //TODO: add DocumentDistance and findDistance

        return null;
    }
}
