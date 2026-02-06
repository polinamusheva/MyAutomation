package com.example.my_automation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFactoryConfig {
    private String permission;
    private String restrict;
    private boolean isGroup;
    private UserActionsExpected expectedResultsFilename;
}
