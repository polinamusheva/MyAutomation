package com.example.my_automation.builder;


import com.example.my_automation.dto.UserActionsExpected;
import com.example.my_automation.dto.UserFactoryConfig;

public class UserFactoryConfigBuilder {

    public static UserFactoryConfig createUserFactoryConfig(String permission, String restrict, boolean isGroup, UserActionsExpected expectedResultsFilename) {
        UserFactoryConfig config = new UserFactoryConfig();
        config.setPermission(permission);
        config.setRestrict(restrict);
        config.setGroup(isGroup);
        config.setExpectedResultsFilename(expectedResultsFilename);
        return config;
    }
}
