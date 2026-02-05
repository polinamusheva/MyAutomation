package com.example.my_automation.utils;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class JpaToJSONAdapter<T> implements AttributeConverter<List<T>, String> {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Logger LOGGER = LoggerFactory.getLogger(JpaToJSONAdapter.class);


    @Override
    public String convertToDatabaseColumn(List<T> values) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(values);
        } catch (final JsonProcessingException e) {
            LOGGER.error("JSON writing error", e);
        }
        return jsonString;
    }


	@Override
	public List<T> convertToEntityAttribute(String dbData) {
		if (StringUtils.isBlank(dbData)) {
			return new ArrayList<>();
		}
		try {
			return objectMapper.readValue(dbData, new TypeReference<List<T>>() {});
		} catch (JsonProcessingException e) {
			 LOGGER.error("JSON writing error", e);
		}
		return new ArrayList<>();
	}
}