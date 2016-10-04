package DocumentClasses;

public interface DocumentDistance {

    /**
     * return the distance between the query and the document
     * @param query
     * @param document
     * @param documents
     * @return
     */
    double findDistance(TextVector query, TextVector document, DocumentCollection documents);

}
