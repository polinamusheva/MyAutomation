package com.example.my_automation.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.my_automation.dto.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.my_automation.config.ConsulValues;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;



@Component
public class JsonEditorForUniqueIds {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonEditorForUniqueIds.class);
    private static final String OBJECT_ID = "object id";
    private String index;
    private final FileReader fileReader;

    private final FileWriter fileWriter = new FileWriter("generated_json.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private final ElasticsearchClient client;

    @Autowired
    private ConsulValues consulValues;

    @PostConstruct
    public void init() {
        this.index = consulValues.getDiscoveryIndex();
    }

    public JsonEditorForUniqueIds(FileReader fileReader, ElasticsearchClient client) throws IOException {
        this.fileReader = fileReader;
        this.client = client;
    }

    public void insert(ClassPathResource filename, Long searchId) {
        try {
            LOGGER.info("Reading items from file: {}", filename);
            InputStream resource = filename.getInputStream();

            Optional<Map<String, Object>[]> searchResultsOptional = convertFileToMap(resource);

            if (searchResultsOptional.isEmpty()) {
                LOGGER.warn("The discovery Search Result file is empty");
                return;
            }

            List<Map<String, Object>> entities = new ArrayList<>();

            for (Map<String, Object> entity : searchResultsOptional.get()) {
                //edit permissions-> discovery_query_ids to contain only current searchId
                Permission permission = mapper.convertValue(entity.get("permissions"), new TypeReference<>() {
                });
                if (permission != null && permission.getDiscoveryQueryIds() != null) {
                    permission.setDiscoveryQueryIds(List.of(searchId.toString()));
                    entity.put("permissions", permission);
                }
                String objectKey = (String) entity.get(OBJECT_ID);
                if (objectKey != null && !objectKey.isEmpty()) {
                    String newObjectKey = "automation_" + objectKey;
                    entity.put(OBJECT_ID, newObjectKey);
                } else {
                    List<Map<String, Object>> recommendations = mapper.convertValue(entity.get("recommendations"), new TypeReference<>() {
                    });
                    for (Map<String, Object> recommendation : recommendations) {
                        Map<String, Object> actorB = mapper.convertValue(recommendation.get("actor_b"), new TypeReference<>() {
                        });
                        actorB.put("id", "automation_" + actorB.get("id"));
                        recommendation.put("actor_b", actorB);
                        Map<String, Object> actorA = mapper.convertValue(recommendation.get("actor_a"), new TypeReference<>() {
                        });
                        actorA.put("id", "automation_" + actorA.get("id"));
                        recommendation.put("actor_a", actorA);
                    }
                    entity.put("recommendations", recommendations);
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

                        //edit dataUnit-> object_key to start with "automation_":
                        Object dataUnitObjectKey = webEntityDataUnit.get(OBJECT_ID);
                        Class<?> aClass = dataUnitObjectKey.getClass();
                        if (aClass.getName().contains("String")) {
                            webEntityDataUnit.put(OBJECT_ID, "automation_" + dataUnitObjectKey);

                        } else {
                            List<String> newArray = new ArrayList<>();
                            List<String> listDataUnit = (List<String>) dataUnitObjectKey;
                            for (String str : listDataUnit) {
                                newArray.add("automation_" + str);
                            }
                            webEntityDataUnit.put(OBJECT_ID, newArray);
                        }

                        //edit  dataUnit-> relations-> entityId
                        List<Map<String, Object>> relations = mapper.convertValue(webEntityDataUnit.get("relations"), new TypeReference<>() {
                        });
                        if (relations != null && !relations.isEmpty()) {
                            for (Map<String, Object> relation : relations) {
                                Object entityId = relation.get("entity_id");
                                aClass = entityId.getClass();
                                if (aClass.getName().contains("String")) {
                                    relation.put("entity_id", "automation_" + entityId);
                                } else {
                                    List<String> newArray = new ArrayList<>();
                                    List<String> listEntityId = (List<String>) entityId;
                                    for (String str : listEntityId) {
                                        newArray.add("automation_" + str);
                                    }
                                    relation.put("entity_id", newArray);
                                }
                            }
                        }
                        webEntityDataUnit.put("relations", relations);
                    }
                    entity.put("dataunits", webEntityDataUnits);
                }

                String id;
                if (entity.get(OBJECT_ID) == null) {
                    id = searchId + "_recommendations";
                } else {
                    id = (String) entity.get(OBJECT_ID);
                }

                client.index(index -> index
                        .index(this.index)
                        .id(id)
                        .document(entity));

                entities.add(entity);
            }
            generateJsonFile(entities);

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

    private void generateJsonFile(List<Map<String, Object>> entities) throws IOException {
        try {
            mapper.writeValue(fileWriter, entities);
        } catch (IOException e) {
            LOGGER.error("Failed to write JSON to file: {}", e.getMessage());
            throw e;
        }
    }
}
