package labs;

import Apriori.ItemSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static Apriori.ItemSet.getSubsets;

/**
 * Implementation of apriori algorithm to find frequent itemsets
 * e.g. for recommendations when purchasing or classes you should take
 */
public class AprioriCalc {
    private static final double MIN_SUPPORT = 0.01; // set / total sets

    private static ArrayList<ItemSet> transactions = new ArrayList<>(); //lists of all itemsets

    private static HashSet<Integer> items = new HashSet<>(); //set of all items

    //lists frequent itemsets. E.g., for key=1, store all 1-itemsets, for key=2, all 2-itemsets and so on.
    static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet = new HashMap<>();


    static double findSupport(Collection<Integer> list) {
        for (ItemSet itemSet : frequentItemSet.get(list.size())) {
            if (itemSet.getItems().containsAll(list)) {
                return itemSet.getSupport();
            }
        }
        return 0.0;
    }

    //processes the input file
    static void process(String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));

        while (input.hasNext()) {
            String itemsString[] = input.nextLine().split(", ");
            ItemSet itemSet = new ItemSet();

            for (int i = 1; i < itemsString.length; i++) {
                itemSet.add(itemsString[i]);
                items.add(Integer.valueOf(itemsString[i]));
            }

            transactions.add(itemSet);
        }
    }

    //finds all k-itemsets, Returns false if no itemsets were found (precondition k>=2)
    static boolean findFrequentItemSets(int k) {
        findFrequentSingleItemSets();

        for (int i = 2; i <= k; i++) {
            frequentItemSet.put(i, new ArrayList<>());

            ArrayList<ItemSet> candidates = generateCandidates(frequentItemSet.get(i - 1));

            if (candidates.isEmpty()) {
                return false;
            }

            // check each itemset has at least minimum support value
            for (ItemSet items : candidates) {
                double supp = isFrequent(items);
                if (supp > MIN_SUPPORT) {
                    items.setSupport(supp);
                    frequentItemSet.get(i).add(items);
                }
            }

            if (frequentItemSet.get(i).isEmpty()) {
                return false;
            }
        }

        return frequentItemSet.size() >= 2;
    }

    //tells if the ItemSet is frequent, i.e., meets the minimum support
    private static double isFrequent(ItemSet itemSet) {
        int count = 0;
        for (ItemSet transaction : transactions) {
            if (transaction.contains(itemSet.getItems())) {
                count++;
            }
        }

        return (double) count / (double) transactions.size();
    }

    // generates superset candidates for previous set (k-1)
    private static ArrayList<ItemSet> generateCandidates(ArrayList<ItemSet> previous) {
        ArrayList<ItemSet> candidates = new ArrayList<>();

        //join step
        for (ItemSet prevIS : previous) {
            previous.stream()
                    .filter(prevIS2 -> prevIS.getWithoutTail().containsAll(prevIS2.getWithoutTail())
                            && prevIS.getTail() < prevIS2.getTail())
                    .forEach(prevIS2 -> {
                        ItemSet newSet = new ItemSet();
                        newSet.addAll(prevIS.getItems());
                        newSet.add(prevIS2.getTail());

                        candidates.add(newSet);
                    });
        }

        ArrayList<ItemSet> result = new ArrayList<>();

        //prune step
        for (ItemSet cand : candidates) {
            List<Set<Integer>> subsets = getSubsets(cand.getItems(), previous.get(0).size());
            boolean frequent = true;
            for (Set<Integer> subset : subsets) {
                // check if this subset was in previous result
                if(!hasSubset(subset, previous)) {
                    frequent = false;
                }
            }
            if (frequent) {
                result.add(cand);
            }
        }
        return result;
    }

    // helper method to check if a list of ItemSets contains a subset
    private static boolean hasSubset(Set<Integer> subset, ArrayList<ItemSet> previous) {
        for (ItemSet items : previous) {
            if (items.containsSubset(subset)) {
                return true;
            }
        }
        return false;
    }

    // finds all 1-itemsets
    private static void findFrequentSingleItemSets() {
        ArrayList<ItemSet> oneItemSets = new ArrayList<>();

        HashMap<Integer, Integer> itemToCount = new HashMap<>();

        for (ItemSet itemSet : transactions) {
            for (int i : itemSet.getItems()) {
                if (!itemToCount.containsKey(i)) {
                    itemToCount.put(i, 0);
                }

                itemToCount.replace(i, itemToCount.get(i) + 1);
            }
        }

        for (Integer i : itemToCount.keySet()) {
            double supp = (double) itemToCount.get(i) / (double) transactions.size();

            if (supp >= MIN_SUPPORT) {
                ItemSet newItemSet = new ItemSet();
                newItemSet.add(i);
                newItemSet.setSupport(supp);
                oneItemSets.add(newItemSet);
            }
        }

        frequentItemSet.put(1, oneItemSets);
    }

    public static void main(String args[]) throws FileNotFoundException {
        AprioriCalc.process("shopping_data.txt");
        findFrequentItemSets(5);

        System.out.println(frequentItemSet.toString());
    }

}
