package com.example.my_automation.config;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class ConsulValues {
    private Logger log = LoggerFactory.getLogger(ConsulValues.class);

    //API values
    @Value("${api.url}")
    private String apiUrl;

    //SFTP values
    @Value("${sftp.host}")
    private String sftpHost;
    @Value("${sftp.port}")
    private int sftpPort;
    @Value("${sftp.user}")
    private String sftpUsername;
    @Value("${sftp.password}")
    private String sftpPassword;
    @Value("${sftp.socketTimeout}")
    private int sftpTimeout;
    @Value("${sftp.maxConnections}")
    private int sftpMaxConnections;

    //DiscoveryDb values
    @Value("${db.db-host}")
    private String dbHost;
    @Value("${db.db-user}")
    private String dbUser;
    @Value("${db.db-password}")
    private String dbPass;
    @Value("${db.db-name}")
    private String dbName;


    //ElasticSearch values
    @Value("#{'${elasticSearch.hosts}'.split(',')}")
    protected List<String> elasticSearchHosts;
    @Value("${elasticSearch.index}")
    private String discoveryIndex;
    @Value("${elasticSearch.investigationIndex}")
    private String investigationIndex;

    //Login values
    @Value("https://${loginIP}")
    protected String loginIP;
    @Value("${loginPort}")
    protected int loginPort;
    @Value("${loginBasePath}")
    private String loginBasePath;
    @Value("${proxyBasePath}")
    private String proxyBasePath;


    public void logValues(){
        log.info("--- API Values ---");
        log.info("API URL: " + apiUrl);

        log.info("--- DB Values ---");
        log.info("Discovery DB Host: " + dbHost);
        log.info("Discovery DB User: " + dbUser);
        log.info("Discovery DB Name: " + dbName);
        log.info("--- ElasticSearch Values ---");
        log.info("ElasticSearch Hosts: " + String.join(", ", elasticSearchHosts));
        log.info("ElasticSearch Index: " + discoveryIndex);
        log.info("ElasticSearch Investigation Index: " + investigationIndex);
        log.info("--- Login Values ---");
        log.info("Login IP: " + loginIP);
        log.info("Login Port: " + loginPort);
        log.info("Login Base Path: " + loginBasePath);
        log.info("Proxy Base Path: " + proxyBasePath);
    }

}
