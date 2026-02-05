package com.example.my_automation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCollectedSourcesDTO {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long searchId;	
	private String name;

	public SearchCollectedSourcesDTO(Long id, Long searchId, String name) {
		this.id=id;
		this.searchId = searchId;
		this.name = name;
	}

}
