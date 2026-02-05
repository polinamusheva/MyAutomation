package com.example.my_automation.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.json.DelegatingDeserializer;
import co.elastic.clients.json.ObjectDeserializer;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.my_automation.constants.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ElasticConfiguration {

    private HttpHost[] clusterHosts;

    private RestClient restClient;

    @Autowired
    private ConsulValues consulValues;

    @Bean
    public ElasticsearchClient client() {
        this.clusterHosts = consulValues.getElasticSearchHosts().stream().map(this::createHost).toList().toArray(new HttpHost[consulValues.getElasticSearchHosts().size()]);
        RestClientBuilder builder = RestClient.builder(clusterHosts);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectDeserializer<?> unwrapped = (ObjectDeserializer<?>) DelegatingDeserializer.unwrap(Analyzer._DESERIALIZER);
        unwrapped.setTypeProperty("type", "custom");
        ElasticsearchTransport transport = new RestClientTransport(builder.build(), new JacksonJsonpMapper(mapper));
        return new ElasticsearchClient(transport);
    }

    @PostConstruct
    public void init() {
        this.clusterHosts = consulValues.getElasticSearchHosts().stream()
                .map(this::createHost)
                .toList()
                .toArray(new HttpHost[consulValues.getElasticSearchHosts().size()]);
        final RestClientBuilder restClientBuilder = RestClient.builder(clusterHosts);

        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectionRequestTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT)
                    .setConnectTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT)
                    .setSocketTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT);
            return requestConfigBuilder;
        }).setHttpClientConfigCallback(clientBuilder -> {
            clientBuilder.setKeepAliveStrategy((response, context) -> Duration.ofDays(1).toMillis());
            clientBuilder.setConnectionReuseStrategy((response, context) -> true);
            return clientBuilder;
        });
        restClient = restClientBuilder.build();
    }

    private HttpHost createHost(String clusterHost) {
        String[] cluster = clusterHost.split(":");
        return new HttpHost(cluster[0], Integer.parseInt(cluster[1]));
    }

}