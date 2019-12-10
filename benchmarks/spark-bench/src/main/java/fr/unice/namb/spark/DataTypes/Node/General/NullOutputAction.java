package fr.unice.namb.spark.DataTypes.Node.General;

import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.Operators.BusyWait.BusyWaitFlatMap;
import fr.unice.namb.spark.Operators.NambBenchmark;


public class NullOutputAction implements DAGNode {

    public NullOutputAction() {}

    @Override
    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception {
        return input.customAction();
    }
}
