package com.example.my_automation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO {

    public enum ENTITY_TYPE {
        LAYOUT,
        DESIGN,
        PROJECT
    }
    @JsonProperty("id")
    private String id;
    @JsonProperty("entityType")
    private String entityType;
    @JsonProperty("name")
    @NotEmpty
    private String name;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("creationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creationDate;
    @JsonProperty("lastModifiedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime lastModifiedDate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("lastUpdateBy")
    private String lastUpdateBy;
}	


