package DocumentClasses;

import java.util.*;
import java.io.*;

/**
 * @author tstan
 */
public class TextVector implements Serializable {

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
}
