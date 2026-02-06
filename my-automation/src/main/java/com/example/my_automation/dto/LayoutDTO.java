package com.example.my_automation.dto;

import com.example.my_automation.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class LayoutDTO extends BaseDTO {

    public static final String TYPE = "case";
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("progress")
    private Long progress;
    @JsonProperty("status")
    private String status;
    @JsonProperty("uploadedAvatar")
    private String uploadedAvatar;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("dueDate")
    private LocalDateTime dueDate;

    @Override
    public String getEntityType() {
    	return TYPE;
    }
}
