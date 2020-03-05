package fr.unice.namb.spark.Utils;

import java.util.HashMap;
import java.util.Map;

public class CounterState {

    private static Map< String,Integer> hm = new HashMap< String,Integer>();

    public static void setKey(String key) {
        hm.put(key, new Integer(0));
    }

    public static Integer getKey(String key){
        Integer currentCount = hm.get(key);
        currentCount ++;
        hm.put(key, new Integer(currentCount));
        return currentCount;
    }

}
