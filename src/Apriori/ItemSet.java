package Apriori;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSet {
    private ArrayList<Integer> items = new ArrayList<>();
    private double support = 0.0;

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public void add(String item) {

        items.add(Integer.valueOf(item));
    }

    public void add(int item) {

        items.add(item);
    }

    public void addAll(ArrayList<Integer> list) {
        items.addAll(list);
    }

    public int size() {
        return items.size();
    }

    public int getTail() {
        return items.get(size() - 1);
    }

    public ArrayList<Integer> getWithoutTail() {
        ArrayList<Integer> temp = (ArrayList<Integer>) items.clone();
        temp.remove(temp.size() - 1);

        return temp;
    }

    public boolean contains(List<Integer> list) {
        return items.containsAll(list);
    }

    // helper to get subsets
    private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unsuccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }

    // gets all subsets of size k of a list of integers
    public static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<>(), res);
        return res;
    }

    public boolean containsSubset(Set<Integer> subset) {
        return items.containsAll(subset);
    }

    public String toString() {
        return items.toString();
    }
}
