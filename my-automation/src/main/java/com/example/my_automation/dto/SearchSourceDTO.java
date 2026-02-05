package com.example.my_automation.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSourceDTO {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long searchId;
	private Long sourceId;
	private String source;
	private String collectionSources;
	 
	public SearchSourceDTO(Long id, Long searchId, Long sourceId, String source, String collectionSources) {
		this.id = id;
		this.searchId = searchId;
		this.sourceId = sourceId;
		this.source = source;
		this.collectionSources = collectionSources;
	}
}
