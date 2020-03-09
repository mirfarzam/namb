package fr.unice.namb.spark.Utils;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static RestHighLevelClient client;

    private static String indexName;

    public static void start(String appName, Long startingTime) throws IOException {

        indexName = appName.concat(startingTime.toString());

        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "trA3/E/Njd2wjB4T43Lgzw=="));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost("elasticsearch.datapirates.ir", 443, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        client = new RestHighLevelClient(builder);

    }

    public static void debug(String log) throws IOException {

        Long id = System.currentTimeMillis();

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("timestamp", new Date());
        jsonMap.put("type", "debug");
        jsonMap.put("message", log);

        IndexRequest request = new IndexRequest(indexName).id(id.toString()).source(jsonMap);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

    }
}
