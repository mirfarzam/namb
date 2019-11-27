package fr.unice.namb.spark.DataTypes.Node.Diamond;

import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.BusyWait.BusyWaitFlatMap;
import fr.unice.namb.spark.Operators.NambBenchmark;

public class DiamondBusyWaitTransformationNode implements DAGNode {


    public DiamondBusyWaitTransformationNode() {

    }

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {

        NambJavaDStream tempDiamondBusyWaitTransformation_1 =   input.flatMapNamb(
                new BusyWaitFlatMap(NambBenchmark.app.getNextProcessing(),
                        (NambBenchmark.app.getFilteringDagLevel() == depth) ? NambBenchmark.app.getFiltering() : 0,
                        0,
                        NambBenchmark._debugFrequency,
                        "DiamondBusyWaitTransformationNode 1 -  " + depth ));

        NambJavaDStream tempDiamondBusyWaitTransformation_2 =   input.flatMapNamb(
                new BusyWaitFlatMap(NambBenchmark.app.getNextProcessing(),
                        (NambBenchmark.app.getFilteringDagLevel() == depth) ? NambBenchmark.app.getFiltering() : 0,
                        0,
                        NambBenchmark._debugFrequency,
                        "DiamondBusyWaitTransformationNode 2 -  " + depth ));

        return tempDiamondBusyWaitTransformation_1.unionNamb(tempDiamondBusyWaitTransformation_2);
    }
}
