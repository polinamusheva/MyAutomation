
package com.example.my_automation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"format", "queryString", "searchIDs", "docs", "offset", "highlight", "filters", "sort","filters"})
public class BaseRequestDto {

    @JsonProperty("format")
    protected String format;

    @JsonProperty("queryString")
    protected String queryString;

    @JsonProperty("actorIds")
    protected List<String> actorIds = new ArrayList<>();

    @JsonProperty("searchIDs")
    protected List<Long> searchIDs = new ArrayList<>();

    @JsonProperty("docs")
    protected Integer docs;

    @JsonProperty("offset")
    protected Integer offset;

    @JsonProperty("highlight")
    protected Boolean highlight;

    @JsonProperty("filters")
    protected List<Filter> filters = new ArrayList<>();

    @JsonProperty("sort")
    protected List<Sort> sort = new ArrayList<>();

    @JsonProperty("type")
    protected Type type;

    @JsonProperty("dates")
    protected List<String> dates;

    @JsonProperty("interval")
    protected String interval;

    public enum Type {
        ACTOR, ACTIVITY, COMMENT, LIKE, SHARE
    }

    
}