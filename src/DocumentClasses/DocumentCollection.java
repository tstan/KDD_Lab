package DocumentClasses;

import java.util.*;
import java.io.*;

/**
 * @author tstan
 */
public class DocumentCollection implements Serializable {

    public static final String DOCUMENT = "document";

    public static String noiseWordArray[] = {"a", "about", "above", "all", "along", "also", "although", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "e.g.", "either", "etc", "etc.", "even", "ever", "enough", "for", "from", "further", "get", "gets", "got", "had", "have", "hardly", "has", "hasn't", "having", "he", "hence", "her", "here", "hereby", "herein", "hereof", "hereon", "hereto", "herewith", "him", "his", "how", "however", "i", "i.e.", "if", "in", "into", "it", "it's", "its", "me", "more", "most", "mr", "my", "near", "nor", "now", "no", "not", "or", "on", "of", "onto", "other", "our", "out", "over", "really", "said", "same", "she", "should", "shouldn't", "since", "so", "some", "such", "than", "that", "the", "their", "them", "then", "there", "thereby", "therefore", "therefrom", "therein", "thereof", "thereon", "thereto", "therewith", "these", "they", "this", "those", "through", "thus", "to", "too", "under", "until", "unto", "upon", "us", "very", "was", "wasn't", "we", "were", "what", "when", "where", "whereby", "wherein", "whether", "which", "while", "who", "whom", "whose", "why", "with", "without", "would", "you", "your", "yours", "yes"};

    public static List<String> noiseWordList = Arrays.asList(noiseWordArray);

    /* key is value for I (1-1400), value is textvector*/
    public HashMap<Integer, TextVector> documents = new HashMap<>();

    /**
     * returns the TextVector for the document with the specified ID
     *
     * @param id
     * @return
     */
    public TextVector getDocumentById(int id) {
        return documents.get(id);
    }

    /**
     * returns the average length of a document. Uses method getTotalWordCount()
     * to calculate number of non noise words in a document.
     *
     * @return
     */
    public int getAverageDocumentLength() {
        int sum = 0;

        for (TextVector tv : getDocuments()) {
            sum += tv.getTotalWordCount();
        }

        return sum / getDocuments().size();
    }

    /**
     * returns number of documents
     *
     * @return
     */
    public int getSize() {
        return documents.size();
    }

    /**
     * returns Collection of TextVectors
     *
     * @return
     */
    public Collection<TextVector> getDocuments() {

        return documents.values();
    }

    /**
     * returns the entry set
     *
     * @return
     */
    public Set<Map.Entry<Integer, TextVector>> getEntrySet() {
        return documents.entrySet();
    }

    /**
     * returns number of documents that contain searchWord
     *
     * @param searchWord
     * @return
     */
    public int getDocumentFrequency(String searchWord) {
        Collection<TextVector> docCollection = getDocuments();

        int count = 0;
        for (TextVector tv : docCollection) {
            if (tv.contains(searchWord)) {
                count++;
            }
        }

        return count;
    }

    /**
     * reads the file named fileName and uses the file to populate the Map
     *
     * @param fileName
     */
    public void documentCollection(String fileName, String type) throws IOException {
        Scanner scanner = new Scanner(new File(fileName));

        int currentDocId = -1;
        boolean insideDoc = false;
        while (scanner.hasNext()) {
            String word = scanner.next();

            if (word.equals(".I")) {
                insideDoc = false;
                // set Id
                currentDocId = Integer.valueOf(scanner.next());

                if (type.equals(DOCUMENT)) {
                    documents.put(currentDocId, new DocumentVector());
                }
                else {
                    documents.put(currentDocId, new QueryVector());
                }

                // find start of body
                while (!insideDoc) {
                    if (scanner.next().equals(".W")) {
                        insideDoc = true;
                    }
                }
            } else if (insideDoc) {
                // split words by non-alphabetic characters
                String words[] = word.split("\\P{Alpha}+");

                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].toLowerCase();

                    if (words[i].length() < 2 || isNoiseWord(words[i])) {
                        continue;
                    } else {
                        documents.get(currentDocId).add(words[i]);
                    }
                }
            }
        }
    }

    public void printResults() {
        String topWord = "";
        int topWordFreq = -1;
        int sum = 0;
        int total = 0;
        for (TextVector tv : getDocuments()) {
            if (tv.getHighestRawFrequency() >= topWordFreq) {
                topWord = tv.getMostFrequentWord();
                topWordFreq = tv.getHighestRawFrequency();
            }
            total += tv.getTotalWordCount();
            sum += tv.getDistinctWordCount();
        }

        System.out.println("Word = " + topWord);
        System.out.println("Frequency = " + topWordFreq);
        System.out.println("Sum of Distinct Number of Words = " + sum);
        System.out.println("Total word count = " + total);

    }

    /**
     * calls normalize on each document in queries or documents
     */
    public void normalize(DocumentCollection allDocs) {
        for (Integer i : documents.keySet()) {
            documents.get(i).normalize(allDocs);
        }
    }

    /**
     * print method for results in Lab 2 for TF-IDF cosine distance
     * @param totalToProcess total queries to process
     * @param allDocs full document collection
     */
    public void printClosestDocsCosineDistance(int totalToProcess, DocumentCollection allDocs) {
        if (totalToProcess < 0 || totalToProcess > this.getSize()) {
            throw new IndexOutOfBoundsException(
                    "The amount of queries asked for must be between 0 and " + this.getSize());
        }

        Iterator<Map.Entry<Integer, TextVector>> queryIterator = this.getEntrySet().iterator();
        int num = 0;
        while (queryIterator.hasNext() && num++ < totalToProcess) {
            int key = queryIterator.next().getKey();
            ArrayList<Integer> closest = this.getDocumentById(key)
                    .findClosestDocuments(allDocs, new CosineDistance());
            System.out.println("documents for query " + key + ": " + closest.toString());
        }
    }

    private boolean isNoiseWord(String word) {
        return noiseWordList.contains(word);
    }

    private boolean isLetter(char c) {
        return Character.isLetter(c);
    }
}
