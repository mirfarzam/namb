package fr.unice.namb.spark.DataTypes;

import org.apache.spark.streaming.api.java.JavaDStream;

public class NambJavaPairDStream<K, V> extends JavaDStream
{
    public NambJavaPairDStream(JavaDStream dstream) {
        super(dstream.dstream(), scala.reflect.ClassTag$.MODULE$.apply(dstream.dstream().getClass()));
    }

//    public NambJavaDStream(JavaDStream p){
//        super(p);
//    }

//    public NambJavaPairDStream windowNamb(int WindowTime) {
//        JavaDStream<T> result = this.window(Durations.seconds(WindowTime));
//        return new NambJavaPairDStream(result);
//    }

//    public NambJavaPairRDD(JavaPairRDD jprdd) {
//        this();
//
//    }
}
