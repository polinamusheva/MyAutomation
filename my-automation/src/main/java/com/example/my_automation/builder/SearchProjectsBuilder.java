package com.example.my_automation.builder;

import com.example.my_automation.dto.SearchProjectsDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.example.my_automation.constants.Constants.MEMBERS;
import static com.example.my_automation.constants.Constants.TIMEZONE_GMT;

public class SearchProjectsBuilder {
	public static SearchProjectsDTO createProjectsSearchRequest(String name, String sort, List<String> creators, List<String> status) {
		SearchProjectsDTO searchProjectsDto = new SearchProjectsDTO();
		searchProjectsDto.setName(name);
		searchProjectsDto.setSort(sort);
		searchProjectsDto.setCreators(creators);
		searchProjectsDto.setPage(1);
		searchProjectsDto.setPageSize(100);
		searchProjectsDto.setStatus(status);
		searchProjectsDto.setRelations(MEMBERS);
		searchProjectsDto.setTimezone(TIMEZONE_GMT
				+ ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString());
		searchProjectsDto.setFavoriteCaseIds(new ArrayList<>());
		return searchProjectsDto;
	}
}
