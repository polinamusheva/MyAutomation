package com.example.my_automation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private @Null() String name;

    private @NotNull() @Size(
            min = 1,
            max = 1,
            message = "OpenAPI supports search by one term"
    ) List<String> searchData;
    private String purpose;

    public SearchRequestDTO() {
    }
}
