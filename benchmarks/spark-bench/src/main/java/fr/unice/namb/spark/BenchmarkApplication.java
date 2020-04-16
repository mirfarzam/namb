package fr.unice.namb.spark;

import fr.unice.namb.spark.Operators.NambBenchmark;
import fr.unice.namb.spark.Utils.Logger;
import fr.unice.namb.utils.common.AppBuilder;
import fr.unice.namb.utils.configuration.Config;
import fr.unice.namb.utils.configuration.schema.NambConfigSchema;
import fr.unice.namb.utils.configuration.schema.SparkConfigSchema;
import org.apache.spark.SparkContext;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class BenchmarkApplication {


    private static JavaStreamingContext buildBenchmarkEnvironment(NambConfigSchema conf, SparkConfigSchema sparkConf) throws Exception{

        NambBenchmark wfn = new NambBenchmark(conf, sparkConf);
        return wfn.appGenerator().getSparkContext();

    }

    public static void main(String[] args) throws Exception
    {

        String nambConfFilePath = args[0];
        String sparkConfFilePath = args[1];

        //Obtaining Configurations For Namb
        Config confParser = new Config(NambConfigSchema.class, nambConfFilePath);
        NambConfigSchema nambConf = (NambConfigSchema) confParser.getConfigSchema();

        //Obtaining Configuration for Spark
        Config sparkConfigParser = new Config(SparkConfigSchema.class, sparkConfFilePath);
        SparkConfigSchema sparkConf = (SparkConfigSchema) sparkConfigParser.getConfigSchema();

        if(nambConf != null && sparkConf != null) {

            Logger.start(sparkConf.getApplicationName().toLowerCase(), System.currentTimeMillis());
            Config.validateConf(nambConf);

            try {
                JavaStreamingContext jssc = buildBenchmarkEnvironment(nambConf,  sparkConf);

                jssc.start();
                jssc.awaitTerminationOrTimeout(sparkConf.getTimeout());


            } catch (Exception e){
                System.out.println(e.getMessage() + e.getStackTrace()[0].getClassName());
            }



        } else {
            throw new Exception("Something went wrong during configuration loading");
        }
    }
}