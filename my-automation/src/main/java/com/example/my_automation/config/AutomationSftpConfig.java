package com.example.my_automation.config;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


@Configuration
@RefreshScope
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AutomationSftpConfig {
    private String serverHost;
    private int serverPort;
    private String username;
    private String password;
    private int timeout;
    private int  maxConnections;

    @Autowired
    ConsulValues consulValues;

    @PostConstruct
    private void init() {
        this.serverHost = consulValues.getSftpHost();
        this.serverPort = consulValues.getSftpPort();
        this.username = consulValues.getSftpUsername();
        this.password = consulValues.getSftpPassword();
        this.timeout = consulValues.getSftpTimeout();
        this.maxConnections = consulValues.getSftpMaxConnections();
    }
}
