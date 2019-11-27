package fr.unice.namb.spark.Operators.BusyWait;

import fr.unice.namb.spark.Operators.NambBenchmark;
import fr.unice.namb.utils.configuration.Config;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple4;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class BusyWaitFlatMap implements FlatMapFunction<Tuple4<String, String, Long, Long>, Tuple4<String, String, Long, Long>> {

    private long _cycles;
    private double _filtering;
    private int _dataSize;
    private int _rate;
    private Random _rand;
    private long _count;
    private String _me;
    private int WF_FILTERING_PRECISION = 100;
//    private SparkContext _sc;

    public BusyWaitFlatMap(long cycles, double filtering, int dataSize, double frequency, String operator_name) {
        _cycles = cycles;
        _filtering = filtering;
        _dataSize = dataSize;
        _me = operator_name;
        _count = 0;
        if(frequency > 0) _rate = (int)(1 / frequency);
        else _rate = 0;
        this.open();
    }

    public BusyWaitFlatMap(SparkContext sc, int cycles, double filtering, double frequency, String operator_name){
        this(cycles, filtering, 0, frequency, operator_name);
    }

    private void open() {
        if (this._filtering > 0){
            this._rand = new Random();
        }
    }

    @Override
    public Iterator<Tuple4<String, String, Long, Long>> call(Tuple4<String, String, Long, Long> in) throws Exception {
        Long ts = 0L;

        String nextValue = in._1();
        String tuple_id = in._2();

        if(this._dataSize > 0 && this._dataSize < nextValue.length()){
            nextValue = nextValue.substring(0, this._dataSize);
        }

        _count ++;
        // simulate processing load
//        for(int i = 0; i < 10000; i++){}
        for(long i = 0; i < _cycles; i++){}


        if (this._rate > 0 && this._count % this._rate == 0){
            if (ts == 0) ts = System.currentTimeMillis();
            System.out.println("[DEBUG] [" + _me + " :  " + NambBenchmark._jssc.sparkContext().env().executorId() + "] : " + tuple_id + "," + _count + "," + ts + "," + nextValue );
        }

        if(this._filtering > 0) {
            if (this._rand.nextInt(Config.WF_FILTERING_PRECISION) <= this._filtering * Config.WF_FILTERING_PRECISION) {
                ts = System.currentTimeMillis();
                return Arrays.asList(new Tuple4<>(nextValue, tuple_id, this._count, ts)).iterator();
            }
        }
        else {
            ts = System.currentTimeMillis();
            return Arrays.asList(new Tuple4<>(nextValue, tuple_id, this._count, ts)).iterator();
        }
        return null;
    }
}
