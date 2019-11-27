package fr.unice.namb.spark.Operators;


import fr.unice.namb.spark.DataTypes.Node.Diamond.DiamondBusyWaitTransformationNode;
import fr.unice.namb.spark.DataTypes.Node.Star.MultiSourceNode;
import fr.unice.namb.spark.DataTypes.Node.General.BusyWaitActionNode;
import fr.unice.namb.spark.DataTypes.Node.General.BusyWaitTransformationNode;
import fr.unice.namb.spark.DataTypes.Node.DAGNode;
import fr.unice.namb.spark.DataTypes.NambJavaDStream;
import fr.unice.namb.spark.DataTypes.Node.General.SourceNode;
import fr.unice.namb.spark.DataTypes.Node.Star.StarBusyWaitActionNode;
import fr.unice.namb.spark.DataTypes.Node.Star.StarBusyWaitTransformationNode;
import fr.unice.namb.utils.common.AppBuilder;
import fr.unice.namb.utils.configuration.Config;
import fr.unice.namb.utils.configuration.schema.NambConfigSchema;
import fr.unice.namb.utils.configuration.schema.SparkConfigSchema;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.ArrayList;
import java.util.Collections;

public class NambBenchmark {

    public static JavaStreamingContext _jssc;

    // connection properties
    private Config.ConnectionShape connectionShape;
    private Config.TrafficRouting connectionRouting;

    // windowing properties
    public static boolean _windowingEnable;
    public static int _windowingDuration;
    public static int _windowingSlideDuration;

    public static int _dataSize;
    public static int _dataValues;
    public static Config.DataDistribution _dataValuesBalancing;
    public static Config.ArrivalDistribution _distribution;
    public static int _rate;
    private NambJavaDStream baseDStream;

    // Spark Config Schema
    public static double _debugFrequency;
    public static int _batchTime;
    public static String _master;
    public static String _applicationName;

    public static AppBuilder app;



    public NambBenchmark(NambConfigSchema nambConf, SparkConfigSchema sparkConf) throws Exception {
        // AppBuilder
        app = new AppBuilder(nambConf);
        // connection configuration
        this.connectionShape = nambConf.getWorkflow().getConnection().getShape();
        this.connectionRouting = nambConf.getWorkflow().getConnection().getRouting();
        // windowing configuration
        _windowingEnable = nambConf.getWorkflow().getWindowing().isEnabled();
        Config.WindowingType windowingType = nambConf.getWorkflow().getWindowing().getType();
        _windowingDuration = nambConf.getWorkflow().getWindowing().getDuration();
        if(windowingType.equals(Config.WindowingType.tumbling)) _windowingSlideDuration = 0;
        else _windowingSlideDuration = nambConf.getWorkflow().getWindowing().getInterval();
//        TODO : How we should consider the number of windowed tasks in my Spark Application
//        int windowedTasks = (depth > 3) ? 2 : 1;
        // DataStream configurations
        _dataSize = nambConf.getDatastream().getSynthetic().getData().getSize();
        _dataValues = nambConf.getDatastream().getSynthetic().getData().getValues();
        _dataValuesBalancing = nambConf.getDatastream().getSynthetic().getData().getDistribution();
        _distribution = nambConf.getDatastream().getSynthetic().getFlow().getDistribution();
        _rate = nambConf.getDatastream().getSynthetic().getFlow().getRate();
        _debugFrequency = sparkConf.getDebugFrequency();
        _batchTime = sparkConf.getBatchTime();
        _master = sparkConf.getMaster();
        _applicationName = sparkConf.getApplicationName();
        // Define Spark Configuration Here
        SparkConf conf = new SparkConf().setMaster(_master).setAppName(_applicationName);
        // Define Java Spark Streaming Context
        _jssc = new JavaStreamingContext(conf, Durations.seconds(_batchTime));
        baseDStream = null;
    }

    public JavaStreamingContext getSparkContext(){
        return _jssc;
    }


    public NambBenchmark appGenerator() throws Exception {
        ArrayList<DAGNode> a = computeTopologyShape(Config.ConnectionShape.linear,4);
        int i = 0;
        while(i < a.size()) {
            baseDStream = a.get(i).run(baseDStream, i+1);
            i++;
        }
        return this;
    }



    private ArrayList<DAGNode> computeTopologyShape(Config.ConnectionShape shape, int depth) throws Exception{
        ArrayList<DAGNode> dagSchema = new ArrayList<>();
        switch(shape){
            case linear:

//                 o o            o o                     o o
//                o   o -------> o   o ----------------> o   o
//                 o o            o o                     o o
//
//                Source    BusyWaitTransformation     BusyWaitAction

                dagSchema.add(new SourceNode());
                dagSchema.addAll(Collections.nCopies(depth-2, new BusyWaitTransformationNode()));
                dagSchema.add(new BusyWaitActionNode());
                return dagSchema;

            case star:

//                 o o                                          o o                                    o o                     o o
//                o   o                                        o   o   ---------------------------->  o   o  -------------->  o   o
//                 o o                                          o o                                    o o                     o o
//                       \              o o                 /
//                          -------->  o   o  ------------>
//                 o o   /              o o                 \   o o
//                o   o                                        o   o
//                 o o                                          o o
//
//
//             MultiSource     BusyWaitTransformation      StarBusyWaitTransformation         BusyWaitTransformation       BusyWaitAction
//                                                            ~StarBusyWaitAction~

                dagSchema.add(new MultiSourceNode(2));
                dagSchema.add(new BusyWaitTransformationNode());
                if(depth == 3) dagSchema.add(new StarBusyWaitActionNode());
                else dagSchema.add(new StarBusyWaitTransformationNode());
                if(depth == 4) dagSchema.add(new BusyWaitActionNode());
                else {
                    dagSchema.addAll(Collections.nCopies(depth-4, new BusyWaitTransformationNode()));
                    dagSchema.add(new BusyWaitActionNode());
                }
                return dagSchema;


            case diamond:
//                                      o o
//                                     o   o
//                                      o o
//                 o o              /         \                     o o                     o o
//                o   o ---------->             ---------------->  o   o  -------------->  o   o
//                 o o              \   o o   /                     o o                     o o
//                                     o   o
//                                      o o
//
//
//                Source     DiamondBusyWaitTransformation    BusyWaitTransformation      BusyWaitAction

                dagSchema.add(new SourceNode());
                dagSchema.add(new DiamondBusyWaitTransformationNode());
                if(depth == 3) dagSchema.add(new BusyWaitActionNode());
                else {
                    dagSchema.addAll(Collections.nCopies(depth-3, new BusyWaitTransformationNode()));
                    dagSchema.add(new BusyWaitActionNode());
                }
                return dagSchema;


            default:
                throw new Exception("This shape <" + shape.name() + "> has not been implemented yet");
        }

    }
}
