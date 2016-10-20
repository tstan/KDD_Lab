package PageRankClasses;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PageRankUtil {

    private HashSet<Integer> nodes = new HashSet<>();
    private HashMap<Integer, ArrayList<Integer>> adjacencyList = new HashMap<>(); // keeps track of nodes that point inwards
    private HashMap<Integer, Integer> outgoingLinks = new HashMap<>(); // keep track of NUMBER of outgoing links

    private HashMap<Integer, Double> pageRankOld = new HashMap<>();
    private HashMap<Integer, Double> pageRankNew = new HashMap<>();

    // finds the L1 Norm between old and new.
    private double findDistance(HashMap<Integer, Double> pageRankOld, HashMap<Integer, Double> pageRankNew) {
        double diff = 0.0;

        for (Integer i : nodes) {
            diff += Math.abs(pageRankNew.get(i) - pageRankOld.get(i));
        }

        //System.out.println("current difference is: " + Double.toString(diff));
        return Math.abs(diff);
    }

    private void addNode(Integer node) {
        nodes.add(node);
        outgoingLinks.put(node, 0);
        adjacencyList.put(node, new ArrayList<>());
    }

    /**
     * Digests `filename` and initializes the graph structures.
     *
     * @param filename a csv that contains left node at index 0 and outgoing node at index 2
     * @throws IOException
     */
    public void initializeGraph(String filename) throws IOException {
        Scanner scanner = new Scanner(new File(filename));
        String input[];
        while (scanner.hasNext()) {
            input = scanner.next().split(",");
            int nodeLft = Integer.valueOf(input[0]);
            int nodeRht = Integer.valueOf(input[2]);

            if (!nodes.contains(nodeLft)) {
                addNode(nodeLft);
            }
            if (!nodes.contains(nodeRht)) {
                addNode(nodeRht);
            }

            // don't allow duplicates
            if (!adjacencyList.get(nodeRht).contains(nodeLft)) {
                outgoingLinks.replace(nodeLft, outgoingLinks.get(nodeLft) + 1);
                adjacencyList.get(nodeRht).add(nodeLft);
            }
        }
    }

    /**
     * get 0th iteration of PageRank weights, all equal weights that add up to 1
     */
    private void setupPageRank() {
        double initialWeight = 1.0 / (double) nodes.size();
        for (Integer node : nodes) {
            pageRankNew.put(node, initialWeight);
        }
    }

    /**
     * gets the nextPageRank iteration and sets it to pageRankNew
     */
    private void nextPageRank() {

        if (pageRankNew.isEmpty()) {
            setupPageRank();
        }

        // move new to old
        pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone();

        // calculate page rank for every node for this iteration
        for (Integer node : nodes) {
            double currentVal = pageRankOld.get(node);
            double adjacentWeightSum = 0.0;

            // for each node pointing inward, adjust weight based on their weight
            for (Integer inLink : adjacencyList.get(node)) {
                // penalize by amount that the inlink points at
                adjacentWeightSum += (pageRankOld.get(inLink) / (double) outgoingLinks.get(inLink));
            }

            double PROB_D = 0.9;
            double PROB_1_MINUS_D = 0.1;
            double newVal = (PROB_1_MINUS_D * currentVal) + (PROB_D * adjacentWeightSum);

            pageRankNew.replace(node, newVal);
        }
    }

    public ArrayList<Integer> getTop20UsingPageRank() {
        boolean reachedEpsilon = false;

        while (!reachedEpsilon) {
            nextPageRank();

            double NORM_DIFFERENCE = 0.001;
            if (findDistance(pageRankOld, pageRankNew) <= NORM_DIFFERENCE) {
                reachedEpsilon = true;
            }
        }

        // move map to a list
        List<Map.Entry<Integer, Double>> list = pageRankNew.entrySet().stream().collect(Collectors.toList());
        // sort list by value
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        // limit to 20 and only put keys into list
        return list.stream()
                .map(Map.Entry::getKey)
                .limit(20)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
