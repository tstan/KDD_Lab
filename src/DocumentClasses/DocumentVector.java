package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentVector extends TextVector {

    private HashMap<String, Double> normalizedVector = new HashMap<>();

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection documents) {
        for (String word : rawVector.keySet()) {
            double tf =  (double) getRawFrequency(word) / (double) getHighestRawFrequency();

            double idf = 0.0;

            if (documents.getDocumentFrequency(word) > 0) {
                idf = log(((double) documents.getSize() / (double) documents.getDocumentFrequency(word)), BASE_TWO);
            }

            normalizedVector.put(word, tf * idf);
        }
    }

    @Override
    public double getNormalizedFrequency(String word) {
        if (normalizedVector.containsKey(word)) {
            return normalizedVector.get(word);
        }
        else {
            return 0.0;
        }
    }
}
