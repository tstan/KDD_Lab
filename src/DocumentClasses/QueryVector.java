package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryVector extends TextVector {

    private HashMap<String, Double> normalizedVector = new HashMap<>();

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {

        return normalizedVector.entrySet();
    }

    /**
     * TF-IDF for queries modified goes here and populates normalizedVector
     * @param documents
     */
    @Override
    public void normalize(DocumentCollection documents) {
        for (String word : rawVector.keySet()) {
            double tf =  0.5 + (0.5 * ((double)getRawFrequency(word) / (double)getHighestRawFrequency()));

            double idf = 0.0;
            if (documents.getDocumentFrequency(word) > 0) {
                idf = log(((double) documents.getSize() / (double) documents.getDocumentFrequency(word)), BASE_TWO);
            }

            normalizedVector.put(word, tf * idf);
        }
    }

    @Override
    public double getNormalizedFrequency(String word) {

        return normalizedVector.get(word);
    }
}
