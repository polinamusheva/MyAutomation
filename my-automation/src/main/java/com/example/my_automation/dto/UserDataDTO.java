package com.example.my_automation.dto;

import com.fasterxml.jackson.annotation.*;


import javax.annotation.Generated;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "company",
        "email",
        "username",
        "firstName",
        "lastName",
        "userProfileUid",
        "workspaceId",
        "avatar",
        "uploadedAvatar",
        "groups",
        "currentPassword",
        "newPassword",
        "passwordMetadataDTO",
        "passwordUpdateDate",
        "numOfIncorrectPassLogin",
        "entityType",
        "loggedIn",
        "blocked",
        "principalId",
        "AuthorizationToken",
        "passExpired",
        "passDaysRemain",
        "type",
        "data",
})
@Generated("jsonschema2pojo")
public class UserDataDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("company")
    private String company;
    @JsonProperty("email")
    private String email;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("userProfileUid")
    private String userProfileUid;
    @JsonProperty("workspaceId")
    private String workspaceId;
    @JsonProperty("avatar")
    private Object avatar;
    @JsonProperty("uploadedAvatar")
    private Object uploadedAvatar;
    @JsonProperty("groups")
    private List<String> groups;
    @JsonProperty("roles")
    private List<String> roles;
    @JsonProperty("currentPassword")
    private Object currentPassword;
    @JsonProperty("newPassword")
    private Object newPassword;
    @JsonProperty("passwordUpdateDate")
    private Long passwordUpdateDate;
    @JsonProperty("numOfIncorrectPassLogin")
    private Integer numOfIncorrectPassLogin;
    @JsonProperty("entityType")
    private String entityType;
    @JsonProperty("loggedIn")
    private Boolean loggedIn;
    @JsonProperty("blocked")
    private Boolean blocked;
    @JsonProperty("principalId")
    private String principalId;
    @JsonProperty("AuthorizationToken")
    private String authorizationToken;
    @JsonProperty("passExpired")
    private Boolean passExpired;
    @JsonProperty("passDaysRemain")
    private Integer passDaysRemain;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("company")
    public String getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(String company) {
        this.company = company;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("userProfileUid")
    public String getUserProfileUid() {
        return userProfileUid;
    }

    @JsonProperty("userProfileUid")
    public void setUserProfileUid(String userProfileUid) {
        this.userProfileUid = userProfileUid;
    }

    @JsonProperty("workspaceId")
    public String getWorkspaceId() {
        return workspaceId;
    }

    @JsonProperty("workspaceId")
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    @JsonProperty("avatar")
    public Object getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("uploadedAvatar")
    public Object getUploadedAvatar() {
        return uploadedAvatar;
    }

    @JsonProperty("uploadedAvatar")
    public void setUploadedAvatar(Object uploadedAvatar) {
        this.uploadedAvatar = uploadedAvatar;
    }

    @JsonProperty("groups")
    public List<String> getGroups() {
        return groups;
    }

    @JsonProperty("groups")
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @JsonProperty("currentPassword")
    public Object getCurrentPassword() {
        return currentPassword;
    }

    @JsonProperty("currentPassword")
    public void setCurrentPassword(Object currentPassword) {
        this.currentPassword = currentPassword;
    }

    @JsonProperty("newPassword")
    public Object getNewPassword() {
        return newPassword;
    }

    @JsonProperty("newPassword")
    public void setNewPassword(Object newPassword) {
        this.newPassword = newPassword;
    }

    @JsonProperty("passwordUpdateDate")
    public Long getPasswordUpdateDate() {
        return passwordUpdateDate;
    }

    @JsonProperty("passwordUpdateDate")
    public void setPasswordUpdateDate(Long passwordUpdateDate) {
        this.passwordUpdateDate = passwordUpdateDate;
    }

    @JsonProperty("numOfIncorrectPassLogin")
    public Integer getNumOfIncorrectPassLogin() {
        return numOfIncorrectPassLogin;
    }

    @JsonProperty("numOfIncorrectPassLogin")
    public void setNumOfIncorrectPassLogin(Integer numOfIncorrectPassLogin) {
        this.numOfIncorrectPassLogin = numOfIncorrectPassLogin;
    }

    @JsonProperty("entityType")
    public String getEntityType() {
        return entityType;
    }

    @JsonProperty("entityType")
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @JsonProperty("loggedIn")
    public Boolean getLoggedIn() {
        return loggedIn;
    }

    @JsonProperty("loggedIn")
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @JsonProperty("blocked")
    public Boolean getBlocked() {
        return blocked;
    }

    @JsonProperty("blocked")
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @JsonProperty("principalId")
    public String getPrincipalId() {
        return principalId;
    }

    @JsonProperty("principalId")
    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    @JsonProperty("AuthorizationToken")
    public String getAuthorizationToken() {
        return authorizationToken;
    }

    @JsonProperty("AuthorizationToken")
    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    @JsonProperty("passExpired")
    public Boolean getPassExpired() {
        return passExpired;
    }

    @JsonProperty("passExpired")
    public void setPassExpired(Boolean passExpired) {
        this.passExpired = passExpired;
    }

    @JsonProperty("passDaysRemain")
    public Integer getPassDaysRemain() {
        return passDaysRemain;
    }

    @JsonProperty("passDaysRemain")
    public void setPassDaysRemain(Integer passDaysRemain) {
        this.passDaysRemain = passDaysRemain;
    }
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("passDaysRemain")
    public void setType(String type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
