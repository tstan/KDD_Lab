package labs;

import Apriori.ItemSet;
import Apriori.Rule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static labs.AprioriCalc.findFrequentItemSets;

/**
 * step 2 of apriori algorithm
 */
public class AprioriFrequentSet {

    private static final double MIN_CONFIDENCE = 0.99; // set / total sets

    //lists frequent itemsets. E.g., for key=1, store all 1-itemsets, for key=2, all 2-itemsets and so on.
    private static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet = new HashMap<>();

    //stores the result
    private static ArrayList<Rule> rules = new ArrayList<>();

    // generates all association rules possible
    private static ArrayList<Rule> split(ItemSet itemSet) {
        ArrayList<Rule> generatedRules = new ArrayList<>();

        Set<Set<Integer>> powerSet = itemSet.getPowerSet();

        powerSet.stream().filter(left -> left.size() > 0).forEachOrdered(left -> {
            powerSet.stream()
                    .filter(right -> right.size() > 0
                            && Collections.disjoint(left, right)
                            && left.size() + right.size() == itemSet.size())
                    .forEachOrdered(right -> {
                        ItemSet leftIS = new ItemSet();
                        leftIS.setItems(new ArrayList<>(left));
                        leftIS.setSupport(AprioriCalc.findSupport(left));

                        ItemSet rightIS = new ItemSet();
                        rightIS.setItems(new ArrayList<>(right));

                        Rule newRule = new Rule();
                        newRule.setLeft(leftIS);
                        newRule.setRight(rightIS);
                        newRule.setSupportWhole(itemSet.getSupport());

                        generatedRules.add(newRule);
                     });
             });

        return generatedRules;
    }

    // generates all the rules.
    private static void generateRules() throws FileNotFoundException {

        //get frequent item sets
        AprioriCalc.process("shopping_data.txt");
        findFrequentItemSets(5);

        // for each itemSet, generate rules
        for (ArrayList<ItemSet> isList : AprioriCalc.frequentItemSet.values()) {
            for (ItemSet is : isList) {
                ArrayList<Rule> candidateRules = split(is);

                // for each generated rule, if minimum confidence is met, add to final rules list
                rules.addAll(candidateRules.stream().filter(AprioriFrequentSet::isMinConfidenceMet).collect(Collectors.toList()));
            }
        }

        System.out.println(rules);
    }

    // checks if rule meets minimum confidence
    private static boolean isMinConfidenceMet(Rule r) {
        return r.getConfidence() >= MIN_CONFIDENCE;
    }

    public static void main(String args[]) throws FileNotFoundException {
        generateRules();
    }
}
