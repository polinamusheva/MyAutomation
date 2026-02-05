package com.example.my_automation.service;

import com.example.my_automation.entity.Search;
import com.example.my_automation.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
@EnableJpaRepositories(basePackages = "com.example.my_automation.repository")
public class SearchService extends BaseService<Search, Long> {

    @Autowired
    private SearchRepository repository;

    @Override
    public JpaRepository<Search, Long> getRepository() { return repository; }

}
