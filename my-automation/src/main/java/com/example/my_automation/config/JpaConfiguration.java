package com.example.my_automation.config;


import com.example.my_automation.entity.Search;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories()
public class JpaConfiguration {

    @Bean(name = "discoveryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean discoveryEntityManagerFactory(
      @Qualifier("datasource") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
        return builder
          .dataSource(dataSource)
          .packages(Search.class)
          .build();
    }
    @Bean
    public PlatformTransactionManager discoveryTransactionManager(
            final @Qualifier("discoveryEntityManagerFactory") LocalContainerEntityManagerFactoryBean discoveryEntityManagerFactory) {
        return new JpaTransactionManager(discoveryEntityManagerFactory.getObject());
    }
}