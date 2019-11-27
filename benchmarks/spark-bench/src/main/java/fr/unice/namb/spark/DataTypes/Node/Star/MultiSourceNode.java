package fr.unice.namb.spark.DataTypes.Node.Star;

import fr.unice.namb.spark.Connectors.SyntheticConnector;
import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.NambBenchmark;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;

public class MultiSourceNode implements DAGNode {

    private int _numberOfSource;

    private JavaDStream<scala.Tuple4<String, String, Long, Long>> baseDStream;

    public MultiSourceNode(int numberOfSource) {}

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {
        for (int i = 0; i < _numberOfSource; i++) {
            JavaDStream<scala.Tuple4<String, String, Long, Long>> temp = NambBenchmark._jssc.receiverStream(new SyntheticConnector(StorageLevel.MEMORY_ONLY(), NambBenchmark._dataSize, NambBenchmark._dataValues, NambBenchmark._dataValuesBalancing, NambBenchmark._distribution, NambBenchmark._rate, NambBenchmark._debugFrequency, "farzam"));
            baseDStream = baseDStream.union(temp);
        }
        NambJavaDStream NambInput = new NambJavaDStream<>(baseDStream);
        return NambInput.windowNamb(NambBenchmark._windowingEnable, NambBenchmark._windowingDuration, NambBenchmark._windowingSlideDuration);
    }
}
