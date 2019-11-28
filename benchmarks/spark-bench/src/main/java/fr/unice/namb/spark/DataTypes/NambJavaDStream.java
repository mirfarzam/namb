package fr.unice.namb.spark.DataTypes;

import fr.unice.namb.spark.Operators.BusyWait.BusyWaitForEachRdd;
import fr.unice.namb.spark.Operators.BusyWait.PrintResultForAction;
import fr.unice.namb.utils.configuration.Config;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;

import java.util.Random;

public class NambJavaDStream<T> extends JavaDStream
{
    public NambJavaDStream(JavaDStream dstream) {
        super(dstream.dstream(), scala.reflect.ClassTag$.MODULE$.apply(dstream.dstream().getClass()));
    }

    public NambJavaDStream windowNamb(boolean enable, int WindowTime, int SlidingTime) {
        if(!enable) {
            return this;
        } else if (SlidingTime > 0) {
            return new NambJavaDStream<>(this.window(Durations.seconds(WindowTime), Durations.seconds(SlidingTime)));
        } else {
            return new NambJavaDStream<>(this.window(Durations.seconds(WindowTime)));
        }
    }

    public NambJavaDStream flatMapNamb(FlatMapFunction f) {
        return new NambJavaDStream(this.flatMap(f));
    }

    public NambJavaDStream unionNamb(NambJavaDStream a) {
        return new NambJavaDStream(this.union(a));
    }

    public NambJavaDStream mapNamb(Function f) {
        return new NambJavaDStream(this.map(f));
    }

    public NambJavaDStream filterNamb(Function f) {
        return new NambJavaDStream(this.filter(f));
    }

    public NambJavaDStream repartitionNamb(int a) { return new NambJavaDStream(this.repartition(a)); }

    public NambJavaDStream customAction() {
        this.print(0);
        return this;
    }
}
