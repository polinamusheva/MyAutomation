package com.example.my_automation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchProjectsDTO {

    private String name;
    private String sort;
    private String relations;
    private Integer page;
    private Integer pageSize;
    private String timezone;
    private String dateFormat;
    private List<String> caseIds;
    private List<String> creators;
    private List<String> status;
    private List<String> favoriteCaseIds;

}
