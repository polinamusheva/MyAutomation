package com.example.my_automation.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.my_automation.utils.JpaToJSONAdapter;
import jakarta.persistence.Converter;


import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

@Converter
public class StringListConverter extends JpaToJSONAdapter<List<String>> {

	@SuppressWarnings("unchecked")
	@Override
	public List convertToEntityAttribute(String dbData) {
		if (StringUtils.isBlank(dbData)) {
			return new ArrayList<>();
		}
		try {
			return objectMapper.readValue(dbData, new TypeReference<List<String>>() {
			});
		} catch (JsonProcessingException e) {
			LOGGER.error("JSON writing error", e);
		}
		return new ArrayList<>();
	}

}
