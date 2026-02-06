package com.example.my_automation.builder;

import com.example.my_automation.dto.UserDataDTO;

import java.util.List;

public class UserDataBuilder {
    public static UserDataDTO createUserData(String username, String password) {
        UserDataDTO userData = new UserDataDTO();
        userData.setCurrentPassword(password);
        userData.setId(username);
        return userData;
    }

    public static UserDataDTO createUserData(String username, String password, List<String> roles, List<String> groups) {
        UserDataDTO userData = new UserDataDTO();
        userData.setCurrentPassword(password);
        userData.setEmail(username + "@example.com");
        userData.setGroups(groups);
        userData.setId(username);
        userData.setNewPassword(password);
        userData.setUsername(username);
        List<String> rolesToUpperCase = roles.stream().map(String::toUpperCase).toList();
        userData.setRoles(rolesToUpperCase);
        return userData;
    }

    public static UserDataDTO editUserData(String firstName, String lastName, List<String> groups, UserDataDTO userToEdit) {
        userToEdit.setFirstName(firstName);
        userToEdit.setLastName(lastName);
        userToEdit.setGroups(groups);
        return userToEdit;
    }
}
