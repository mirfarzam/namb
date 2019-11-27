package fr.unice.namb.spark.Operators.BusyWait;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple4;

public class PrintResultForAction implements VoidFunction {

    private int _depth;

    public PrintResultForAction(int depth) {
        _depth = depth;
    }

    @Override
    public void call(Object o) throws Exception {
        final int[] i = {0};
        ((JavaRDD) o).foreach(ele -> {
            i[0]++;
            System.out.println("[DEBUG] [ Print Rsult of Action-" + _depth + "] : " + ((Tuple4)ele)._2() + "," + ((Tuple4)ele)._3() + "," + System.currentTimeMillis() + "," + ((Tuple4)ele)._1() );
        });
    }
}
