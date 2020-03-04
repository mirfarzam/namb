package fr.unice.namb.spark.DataTypes.Node.General;

import fr.unice.namb.spark.Connectors.SyntheticConnector;
import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.NambBenchmark;
import fr.unice.namb.utils.common.Task;
import fr.unice.namb.utils.configuration.Config;
import fr.unice.namb.utils.configuration.schema.NambConfigSchema;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import javax.xml.transform.Source;

public class SourceNode implements DAGNode {

    private StorageLevel _storageLevel;
    private NambConfigSchema.DataStream.Synthetic _syntheticConf;
    private double _debugFrequency;

    private int dataSize;
    private int dataValues;
    private Config.DataDistribution dataValuesBalancing;
    private Config.ArrivalDistribution distribution;
    private int rate;
    private String nodeName;

    private boolean pipelined = false;

    public SourceNode() {}

    public SourceNode(Task task) {
        pipelined = true;
        dataSize = task.getDataSize();
        dataValues = task.getDataValues();
        dataValuesBalancing = task.getDataDistribution();
        distribution = task.getFlowDistribution();
        rate = task.getFlowRate();
        nodeName = task.getName();
    }

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {

        JavaDStream SourceNodeInput = NambBenchmark._jssc.receiverStream(
                new SyntheticConnector(StorageLevel.MEMORY_ONLY(),
                        NambBenchmark._dataSize,
                        NambBenchmark._dataValues,
                        NambBenchmark._dataValuesBalancing,
                        NambBenchmark._distribution,
                        NambBenchmark._rate,
                        NambBenchmark._debugFrequency,
                        (pipelined) ? nodeName :  "source : " + depth
                ));
        NambJavaDStream NambInput = new NambJavaDStream<>(SourceNodeInput);
        return NambInput.windowNamb(NambBenchmark._windowingEnable, NambBenchmark._windowingDuration, NambBenchmark._windowingSlideDuration);
    }

    public NambJavaDStream runPipe() throws Exception {

        JavaDStream SourceNodeInput = NambBenchmark._jssc.receiverStream(
                new SyntheticConnector(StorageLevel.MEMORY_ONLY(),
                        dataSize,
                        dataValues,
                        dataValuesBalancing,
                        distribution,
                        rate,
                        NambBenchmark._debugFrequency,
                        nodeName
                ));
        NambJavaDStream NambInput = new NambJavaDStream<>(SourceNodeInput);
        return NambInput.windowNamb(NambBenchmark._windowingEnable, NambBenchmark._windowingDuration, NambBenchmark._windowingSlideDuration);
    }
}
