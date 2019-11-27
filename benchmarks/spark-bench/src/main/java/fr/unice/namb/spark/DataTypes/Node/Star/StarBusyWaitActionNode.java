package fr.unice.namb.spark.DataTypes.Node.Star;

import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.NambBenchmark;

public class StarBusyWaitActionNode implements DAGNode {


    public StarBusyWaitActionNode() {}

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {
        NambJavaDStream action_first = input.customAction(
                NambBenchmark.app.getNextProcessing(),
                NambBenchmark._debugFrequency,
                depth,
                false);

        NambJavaDStream action_second = input.customAction(
                NambBenchmark.app.getNextProcessing(),
                NambBenchmark._debugFrequency,
                depth,
                false);

        return action_first.unionNamb(action_second);
    }
}
