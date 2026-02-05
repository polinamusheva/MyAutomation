package com.example.my_automation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableScheduling
@EntityScan("com.example.my_automation.domain")
@ComponentScan(basePackages = { "com.example.*" })
@EnableTransactionManagement
public class ApiConfiguration {

	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private AutomationSftpConfig sftpConfig;

	public static final String ADMINUSERNAME = "administrator";
	public static final String ADMINPASSWORD = "Adm1n1strator1!";

	//User with Administrator Rights
	public static final String USERNAME = "automationuser";
	public static final String PASSWORD = "Automationuser1!";

    @Autowired
    private ConsulValues consulValues;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@PostConstruct
	public void logValues() {
		consulValues.logValues();
	}
}
