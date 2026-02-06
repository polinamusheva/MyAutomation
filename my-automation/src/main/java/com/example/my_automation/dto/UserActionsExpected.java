package com.example.my_automation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActionsExpected {

    private boolean createProject;
    private boolean getProject;
    private boolean updateProject;
    private boolean deleteProject;
}
