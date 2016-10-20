package labs;

import PageRankClasses.PageRankUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * PageRank for graph
 */
public class Lab4 {

    public static void main(String args[]) throws IOException {
        PageRankUtil pru = new PageRankUtil();
        pru.initializeGraph("graph.csv");
        ArrayList<Integer> top20Nodes = pru.getTop20UsingPageRank();
        System.out.println(top20Nodes.toString());
    }
}
