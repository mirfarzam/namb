package fr.unice.namb.spark.Operators.BusyWait;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;

public class BusyWaitForEachRdd implements VoidFunction {

    private int _cycle;

    public BusyWaitForEachRdd(int cycle) {
        _cycle =  cycle;
    }

    @Override
    public void call(Object o) throws Exception {
        for(int i = 0; i < _cycle; i++) {}
        ((JavaRDD) o).take(1);
    }
}
