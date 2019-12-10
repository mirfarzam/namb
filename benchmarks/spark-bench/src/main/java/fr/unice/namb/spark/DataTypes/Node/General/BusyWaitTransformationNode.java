package fr.unice.namb.spark.DataTypes.Node.General;

import fr.unice.namb.spark.Connectors.SyntheticConnector;
import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.BusyWait.BusyWaitFlatMap;
import fr.unice.namb.spark.Operators.NambBenchmark;
import fr.unice.namb.utils.common.Task;
import fr.unice.namb.utils.configuration.Config;
import fr.unice.namb.utils.configuration.schema.NambConfigSchema;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;

public class BusyWaitTransformationNode implements DAGNode {

    private int dataSize;
    private int cycle;
    private double filtering;
    private String nodeName;

    private boolean pipelined = false;


    public BusyWaitTransformationNode() {

    }

    public BusyWaitTransformationNode(Task task) {
        pipelined = true;
        dataSize = task.getDataSize();
        cycle = task.getProcessing();
        nodeName = task.getName();
        filtering = task.getFiltering();
    }

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {
        return input.flatMapNamb(
                new BusyWaitFlatMap(
                        NambBenchmark.app.getNextProcessing(),
                        (NambBenchmark.app.getFilteringDagLevel() == depth) ? NambBenchmark.app.getFiltering() : 0,
                        0,
                        NambBenchmark._debugFrequency,
                        "BusyWaitTransformationNode  " + depth )
        );
    }

    public NambJavaDStream runPipe(NambJavaDStream input) throws Exception {
        return input.flatMapNamb(
                new BusyWaitFlatMap(
                        cycle,
                        filtering,
                        dataSize,
                        NambBenchmark._debugFrequency,
                        nodeName)
        );
    }
}
