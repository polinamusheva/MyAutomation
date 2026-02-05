package com.example.my_automation.dto;

import com.example.my_automation.enums.Status;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class SearchDTO {
    private static final long serialVersionUID = 1L;
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String term;
    private String purpose;
    private Status status;
    @JsonProperty("count")
    private Integer count = 0;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date createdAt;
    private Set<String> sources;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SearchDTO> children;
    private Set<String> collectedSources = new HashSet();
    @JsonIgnore
    private Set<Long> sourceIds;
    private String imageUrl;

    public SearchDTO() {
    }

    public SearchDTO(Long id, String term) {
        this.id = id;
        this.term = term;
    }

    public SearchDTO clone() {
        SearchDTO search = new SearchDTO();
        search.createdAt = this.createdAt;
        search.name = this.name;
        search.purpose = this.purpose;
        search.sources = this.sources;
        search.status = this.status;
        search.term = this.term;
        search.imageUrl = this.imageUrl;
        return search;
    }
}
