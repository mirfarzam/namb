package fr.unice.namb.spark.DataTypes.Node.Star;

import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.BusyWait.BusyWaitFlatMap;
import fr.unice.namb.spark.Operators.NambBenchmark;

public class StarBusyWaitTransformationNode implements DAGNode {


    public StarBusyWaitTransformationNode() {

    }

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {

        NambJavaDStream first = input.flatMapNamb(
                new BusyWaitFlatMap(NambBenchmark.app.getNextProcessing(),
                        (NambBenchmark.app.getFilteringDagLevel() == depth) ? NambBenchmark.app.getFiltering() : 0,
                        0,
                        NambBenchmark._debugFrequency,
                        "BusyWaitTransformationNode  " + depth )).customAction();

        return input.flatMapNamb(
                new BusyWaitFlatMap(NambBenchmark.app.getNextProcessing(),
                (NambBenchmark.app.getFilteringDagLevel() == depth) ? NambBenchmark.app.getFiltering() : 0,
                0,
                NambBenchmark._debugFrequency,
                "BusyWaitTransformationNode  " + depth ));
    }
}
