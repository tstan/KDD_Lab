package Mains;

import PageRankClasses.PageRankUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * PageRank for graph similar to Google page rank.
 */
public class PageRanker {

    public static void main(String args[]) throws IOException {
        PageRankUtil pru = new PageRankUtil();
        pru.initializeGraph("graph.csv");
        ArrayList<Integer> top20Nodes = pru.getTop20UsingPageRank();
        System.out.println(top20Nodes.toString());
    }
}
