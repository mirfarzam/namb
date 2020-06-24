package fr.unice.namb.spark.Utils;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.net.InetAddress;

public class Logger {

    private static RestHighLevelClient client;

    private static String indexName;

    private static File logFile;
    private static FileWriter logFileWriter;
    private static BufferedWriter logFileBufferedWrited;

    public static void start(String appName, Long startingTime) throws IOException {
        indexName = appName;
        logFile = new File("Test.txt");
        if(!logFile.exists()){
            logFile.createNewFile();
        }else{
            System.out.println("File already exists");
        }
        logFileWriter = new FileWriter(logFile, true);
        logFileBufferedWrited = new BufferedWriter(logFileWriter);
    }

    public static void debug(String log) throws IOException {

//        final CredentialsProvider credentialsProvider =
//                new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY,
//                new UsernamePasswordCredentials("elastic", "trA3/E/Njd2wjB4T43Lgzw=="));
//
//        RestClientBuilder builder = RestClient.builder(
//                new HttpHost("elasticsearch.datapirates.ir", 443, "https"))
//                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
//                    @Override
//                    public HttpAsyncClientBuilder customizeHttpClient(
//                            HttpAsyncClientBuilder httpClientBuilder) {
//                        return httpClientBuilder
//                                .setDefaultCredentialsProvider(credentialsProvider);
//                    }
//                });
//
//        client = new RestHighLevelClient(builder);
//
//        Long time = System.currentTimeMillis();
//        String id = time.toString().concat("@").concat(InetAddress.getLocalHost().getHostName());
//
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("timestamp", new Date());
//        jsonMap.put("type", "debug");
//        jsonMap.put("message", log);
//
//        IndexRequest request = new IndexRequest("jafar").id(id).source(jsonMap);
//
//        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        logFileBufferedWrited.write(log+"\n");
//        System.out.println(log);

    }
}
