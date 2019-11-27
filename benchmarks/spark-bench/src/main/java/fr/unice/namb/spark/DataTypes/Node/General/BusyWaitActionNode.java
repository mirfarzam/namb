package fr.unice.namb.spark.DataTypes.Node.General;

import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.BusyWait.BusyWaitFlatMap;
import fr.unice.namb.spark.Operators.NambBenchmark;
import fr.unice.namb.utils.configuration.Config;

import java.util.Random;

public class BusyWaitActionNode implements DAGNode {


    public BusyWaitActionNode() {}

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {
        return input.customAction(
                NambBenchmark.app.getNextProcessing(),
                NambBenchmark._debugFrequency,
                depth,
                true);
    }
}
