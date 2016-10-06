package DocumentClasses;

public class CosineDistance implements DocumentDistance {

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {

        //document has nothing in common possible with the query
        if (document.getTotalWordCount() == 0) {
            return 0.0;
        }

        double dotProduct = 0.0;

        for (String word : query.rawVector.keySet()) {
            dotProduct += query.getNormalizedFrequency(word) * document.getNormalizedFrequency(word);
        }

        double size = document.getL2Norm() * query.getL2Norm();

        if (size > 0) {
            return dotProduct/size;
        }

        else {
            return 0.0;
        }
    }
}
