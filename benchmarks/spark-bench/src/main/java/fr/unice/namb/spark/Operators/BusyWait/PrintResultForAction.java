package fr.unice.namb.spark.Operators.BusyWait;

import fr.unice.namb.spark.Operators.NambBenchmark;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple4;

public class PrintResultForAction implements VoidFunction {

    private String _me;
    private int _cycle;

    public PrintResultForAction(int cycle, String name) {
        _me = name;
        _cycle = cycle;
    }

    @Override
    public void call(Object o) throws Exception {
        for(int i = 0; i < _cycle; i++) {}
        final int[] i = {0};
        ((JavaRDD) o).foreach(ele -> {
            i[0]++;
            System.out.println("[DEBUG] [" + _me + " : "  + NambBenchmark._jssc.sparkContext().env().executorId() + "] : " + ((Tuple4)ele)._2() + "," + ((Tuple4)ele)._3() + "," + System.currentTimeMillis() + "," + ((Tuple4)ele)._1() );
        });
    }
}
