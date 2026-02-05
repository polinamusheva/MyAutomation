package com.example.my_automation.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import com.example.my_automation.config.ConsulValues;
import com.example.my_automation.dto.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.my_automation.constants.Constants.ElasticFields.OBJECT_KEY;

@Component
public class ScriptInsertion {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptInsertion.class);
    private String index;
    private final FileReader fileReader;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ElasticsearchClient client;

    @Autowired
    private ConsulValues consulValues;

    @PostConstruct
    public void init() {
        this.index = consulValues.getDiscoveryIndex();
    }

    public ScriptInsertion(FileReader fileReader, ElasticsearchClient client) throws IOException {
        this.fileReader = fileReader;
        this.client = client;
    }

    public void insert(ClassPathResource filename, Long searchId) {
        try {
            LOGGER.info("Reading items from file: {}", filename);
            InputStream resource = filename.getInputStream();

            Optional<Map<String, Object>[]> discoverySearchResultsOptional = convertFileToMap(resource);

            if (discoverySearchResultsOptional.isEmpty()) {
                LOGGER.warn("The discovery Search Result file is empty");
                return;
            }

            for (Map<String, Object> entity : discoverySearchResultsOptional.get()) {
                //edit permissions-> discovery_query_ids to contain only current searchId
                Permission permission = mapper.convertValue(entity.get("permissions"), new TypeReference<>() {
                });
                if (permission != null) {
                    if (permission.getDiscoveryQueryIds() != null) {
                        permission.setDiscoveryQueryIds(List.of(searchId.toString()));
                    }
                    Boolean isMonitored = (Boolean) entity.get("is_monitor");
                    if (isMonitored != null && isMonitored) {
                        permission.setMonitoredDiscoveryQueryIds(List.of(searchId.toString()));
                    } else {
                        permission.setMonitoredDiscoveryQueryIds(null);
                    }
                    entity.put("permissions", permission);
                }


                if (permission != null && permission.getMonitoredDiscoveryQueryIds() != null && !permission.getMonitoredDiscoveryQueryIds().isEmpty()) {
                    permission.setMonitoredDiscoveryQueryIds(List.of(searchId.toString()));
                    entity.put("permissions", permission);
                }

                if (entity.get("dataunits") != null) {
                    List<Map<String, Object>> webEntityDataUnits = mapper.convertValue(entity.get("dataunits"), new TypeReference<>() {
                    });

                    for (Map<String, Object> webEntityDataUnit : webEntityDataUnits) {
                        //edit dataUnit-> fullmatchSearchIds to contain only current searchId
                        List<String> fullmatchSearchIds = mapper.convertValue(webEntityDataUnit.get("fullmatchSearchIds"), new TypeReference<>() {
                        });
                        if (fullmatchSearchIds != null) {
                            fullmatchSearchIds = List.of(searchId.toString());
                        }
                        webEntityDataUnit.put("fullmatchSearchIds", fullmatchSearchIds);
                    }
                    entity.put("dataunits", webEntityDataUnits);
                }

                String id;
                if (entity.get(OBJECT_KEY) == null) {
                    id = searchId + "_recommendations";
                } else {
                    id = (String) entity.get(OBJECT_KEY);
                }

                client.index(index -> index
                        .index(this.index)
                        .id(id)
                        .document(entity));
            }


            LOGGER.info("Successfully inserted/updated all records from file: {}", filename);

        } catch (IOException e) {
            LOGGER.error(String.format("Could not store item to be used for tests in index '%s': %s!", index, e.getMessage()));
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Optional<Map<String, Object>[]> convertFileToMap(InputStream inputStream) {
        return fileReader.read(inputStream)
                .map(s -> {
                    try {
                        return mapper.readValue(s, new TypeReference<>() {
                        });
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                });
    }
}