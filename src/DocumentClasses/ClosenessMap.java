package DocumentClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class ClosenessMap implements Serializable {
    public HashMap<Integer, ArrayList<Integer>> closenessMap;

    public ClosenessMap(HashMap<Integer, ArrayList<Integer>> map) {
        closenessMap = map;
    }

    public HashMap<Integer, ArrayList<Integer>> getMap() {
        return closenessMap;
    }
}
