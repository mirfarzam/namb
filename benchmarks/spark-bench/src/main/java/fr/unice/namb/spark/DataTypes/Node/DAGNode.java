package fr.unice.namb.spark.DataTypes.Node;

import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.Operators.BusyWait.BusyWaitFlatMap;
import fr.unice.namb.spark.Operators.NambBenchmark;
import fr.unice.namb.utils.configuration.Config;
import fr.unice.namb.utils.configuration.schema.NambConfigSchema;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.api.java.JavaDStream;

import java.util.Random;

public interface DAGNode {

    double _filtering = 0;
    double _frequency = 0;
    int _cycle = 0;
    NodeType _type = null;
    int _multi = 0;
    static int nodeNum = 0;

    public enum NodeType{
        MultiSource,
        Source,
        BusyWaitTransformation,
        DiamondBusyWaitTransformation,
        StarBusyWaitTransformation,
        BusyWaitAction,
        StarBusyWaitAction
    }

    public NambJavaDStream run(NambJavaDStream input, int depth) throws Exception;

}


//        switch (_type) {
//            case Source:
//                return input;
//            case MultiSource:
//                return input;
//            case BusyWaitAction:
//                return input.customAction(1, new Random(), 10);
//            case StarBusyWaitAction:
//                NambJavaDStream tempStarBusyWaitAction = input.customAction(1, new Random(), 10);
//                return input.customAction(1, new Random(), 10);
//            case BusyWaitTransformation:
//                return input.flatMapNamb(new BusyWaitFlatMap(100, 1.0, 1,1.0,"farzam"));
//            case StarBusyWaitTransformation:
//                NambJavaDStream tempStarBusyWaitTransformation = input.customAction(1, new Random(), 10);
//                return input.flatMapNamb(new BusyWaitFlatMap(100, 1.0, 1,1.0,"farzam"));
//            case DiamondBusyWaitTransformation:
//                NambJavaDStream tempDiamondBusyWaitTransformation_1 =  input.flatMapNamb(new BusyWaitFlatMap(100, 1.0, 1,1.0,"farzam"));
//                NambJavaDStream tempDiamondBusyWaitTransformation_2 =  input.flatMapNamb(new BusyWaitFlatMap(100, 1.0, 1,1.0,"farzam"));
//                return tempDiamondBusyWaitTransformation_1.unionNamb(tempDiamondBusyWaitTransformation_2);
//            default:
//                throw new Exception("This node type <" + this._type.toString() + "> has not been implemented yet");
//        }
