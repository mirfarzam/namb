package fr.unice.namb.spark.Connectors;

import fr.unice.namb.spark.DataTypes.Node.CounterState;
import fr.unice.namb.utils.common.DataGenerator;
import fr.unice.namb.utils.common.DataStream;
import fr.unice.namb.utils.configuration.Config;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import org.w3c.dom.css.Counter;
import scala.Tuple4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyntheticConnector extends Receiver<Tuple4<String, String, Long, Long>> {

    private int dataSize;
    private int dataValues;
    private Config.DataDistribution dataValuesBalancing;
    private int flowRate;
    private long sleepTime;
    private Config.ArrivalDistribution distribution;
    private DataGenerator dataGenerator;
    private DataStream dataStream;
    private double frequency;

    private Long count;
    private int rate;
    private String me;

    public SyntheticConnector(
            StorageLevel storageLevel,
            int dataSize,
            int dataValues,
            Config.DataDistribution dataValuesBalancing,
            Config.ArrivalDistribution flowDistribution,
            int flowRate,
            double frequency,
            String sourceName) {
        // constructor
        super(storageLevel);
        this.dataSize = dataSize;
        this.dataValues = dataValues;
        this.dataValuesBalancing = dataValuesBalancing;
        this.distribution = flowDistribution;
        this.flowRate = flowRate;
        this.me = sourceName;
        this.frequency = frequency;
        if(frequency > 0) this.rate = (int)(1 / frequency);
        else this.rate = 0;
        CounterState.setKey(this.me);
    }

    @Override
    public void onStart() {
        // Start the thread that receives data over a connection
        this.dataGenerator = new DataGenerator(this.dataSize, this.dataValues, this.dataValuesBalancing);
        this.dataStream = new DataStream();
        if (this.flowRate != 0)
            this.sleepTime = dataStream.convertToInterval(this.flowRate);
        this.count = 0L;
//        this.me = this.me + "_" + getRuntimeContext().getIndexOfThisSubtask();
        new Thread(this::receive).start();
    }

    @Override
    public void onStop() {

    }

    private void receive() {
        try {
            while(!isStopped()) {
                try {
                    String nextValue = new String(dataGenerator.getNextValue());
                    if (this.flowRate != 0) {
                        Thread.sleep(
                                this.dataStream.getInterMessageTime(this.distribution, (int) this.sleepTime)
                        );
                    }
                    this.count = new Long(CounterState.getKey(this.me));
                    String tuple_id = UUID.randomUUID().toString();
                    Long ts = System.currentTimeMillis();
                    store(new Tuple4<String, String, Long, Long>(nextValue, tuple_id, this.count, ts));

                    ts = System.currentTimeMillis();
                    if (this.rate > 0 && this.count % this.rate == 0){
                        System.out.println("[DEBUG] [" + this.me + "] : " + tuple_id + "," + this.count + "," + ts + "," + nextValue);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch(Throwable t) {
            // restart if there is any other error
            restart("Error receiving data", t);
        }
    }

}
