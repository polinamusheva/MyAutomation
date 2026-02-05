package com.example.my_automation.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSource {
	private Logger log = LoggerFactory.getLogger(DataSource.class);

	private String discoveryHost;
	private String discoveryUser;
	private String discoveryPass;
	private String discoveryDbName;

	@Autowired
	private ConsulValues consulValues;

	@PostConstruct
	private void init() {
		this.discoveryHost = consulValues.getDbHost();
		this.discoveryUser = consulValues.getDbUser();
		this.discoveryPass = consulValues.getDbPass();
		this.discoveryDbName = consulValues.getDbName();
	}

	@Bean(name = "datasource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public javax.sql.DataSource dataSource() {

		String discoveryDbUrl = "jdbc:postgresql://" + discoveryHost + ":5432/" + discoveryDbName;
		return DataSourceBuilder.create().url(discoveryDbUrl).username(discoveryUser).password(discoveryPass).build();
	}
}