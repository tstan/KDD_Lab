package DocumentClasses;

/**
 * Finds the distance using Okapi BM25 between a `query` and `document` based
 * on the collection `documents`
 *
 * Use k1=1.2, b=0.75, and k2=100.
 */
public class OkapiDistance implements DocumentDistance{

    public static final double K1 = 1.2, B = 0.75, K2 = 100;

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        double totalDistance = 0.0;

        for (String word : query.rawVector.keySet()) {
            double freqRatio = (documents.getSize() - documents.getDocumentFrequency(word) + 0.5)
                    / (documents.getDocumentFrequency(word) + 0.5);
            double rarityMultiplier = query.log(freqRatio, Math.E);

            double docLengthNum = ((K1 + 1) * (double) document.getRawFrequency(word));
            double docLengthDen = (K1 * (1 - B + (B * ((double) document.getTotalWordCount()
                    / (double) documents.getAverageDocumentLength())))) + document.getRawFrequency(word);
            double docLengthMultiplier = docLengthNum/docLengthDen;

            double queryRarityMultiplier = ((K2 + 1) * query.getRawFrequency(word))
                    / (K2 + query.getRawFrequency(word));

            totalDistance += (rarityMultiplier * docLengthMultiplier * queryRarityMultiplier);
        }

        return totalDistance;
    }
}
