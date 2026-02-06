package com.example.my_automation.clients;


import com.example.my_automation.builder.UserDataBuilder;
import com.example.my_automation.config.ConsulValues;
import com.example.my_automation.constants.Constants;
import com.example.my_automation.dto.UserDataDTO;
import com.example.my_automation.utils.RequestSpecifications;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import jakarta.annotation.PostConstruct;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import static io.restassured.RestAssured.given;

@Component
public class UserManagementClient {
    private static final Logger log = LoggerFactory.getLogger(UserManagementClient.class);
    private final String UNBLOCK_USER_PATH = "/users/{username}/passwordmetadata/unblock_user";
    private final String UPDATE_PASSWORD_PATH = "/users/{username}";
    private final String USERS_PATH = "/users";
    private final String VERIFY_USER_PATH = USERS_PATH + "/verify-user/";
    private final String DELETE_USER_PATH = USERS_PATH + "/delete-user/";
    private final String GET_GROUPS_PATH = "/groups";
    private final String CREATE_GROUP_PATH = GET_GROUPS_PATH +"/create-group";
    private final String EDIT_GROUP_PATH = GET_GROUPS_PATH +"/edit-group";
    private final String DELETE_GROUP_PATH = GET_GROUPS_PATH +"/delete-group";


    private String proxyBasePath;
    private String loginIP;
    private int loginPort;

    @Autowired
    private ConsulValues consulValues;

    @PostConstruct
    private void init() {
        this.proxyBasePath=consulValues.getProxyBasePath();
        this.loginIP = consulValues.getLoginIP();
        this.loginPort = consulValues.getLoginPort();
    }

    @Autowired
    private RequestSpecifications requestSpecifications;

    @Autowired
    private MyAppApiClient apiClient;

    public void unblockUser(String username, String authToken) {
        RequestSpecification spec = requestSpecifications.getBasePath(loginIP, loginPort, proxyBasePath);
        spec.header(Constants.Headers.AUTHORIZATION_TOKEN, authToken);
        String url = UNBLOCK_USER_PATH.replace("{username}", username);
        log.info("Send request to {}", url);
        given().spec(spec).contentType(ContentType.JSON).accept(ContentType.JSON).body("{}")
                .when().post(url).then().log().ifError().statusCode(200).extract().response();
        log.info("Unblock user {} is successful", username);
    }

    // TODO: Adapt the endpoint
    public void changePassword(String username, String usernameEncoded, String currentPassword, String newPassword, String authToken) throws JSONException {
        RequestSpecification spec = requestSpecifications.getBasePath(loginIP, loginPort, proxyBasePath);
        JSONObject request = new JSONObject();
        request.put("id", usernameEncoded);
        request.put("username", usernameEncoded);
        request.put("currentPassword", currentPassword);
        request.put("newPassword", newPassword);
        spec.header(Constants.Headers.AUTHORIZATION_TOKEN, authToken);
        String url = UPDATE_PASSWORD_PATH.replace("{username}", username);
        log.info("Request password change for {}", username);
        log.debug("Send PUT request to {} for user {} with current password {} and new password {}",
                url, username, currentPassword, newPassword);
        given().spec(spec).contentType(ContentType.JSON).accept(ContentType.JSON).body(request.toString())
                .when().put(url).then().log().ifError().statusCode(200).extract().response();
    }

    public ResponseBody getAllUsers() {
        String authToken = apiClient.getAuthToken();
        log.info("Send request to {} to get all users", USERS_PATH);

        ResponseBody responseBody = given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .when()
                .get(USERS_PATH)
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .extract()
                .response();
        return responseBody;
    }

    public Response createUser(UserDataDTO userData) {
        String authToken = apiClient.getAuthToken();
        log.info("Send request to {} path to create user", USERS_PATH);
        Response response = given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .body(userData).filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                .when()
                .post(USERS_PATH)
                .then()
                .log()
                .ifError()
                .extract().response();
        return response;
    }

    //TODO: Add test for "/ users/ verify-user/" path with demo user and read only user
    public void verifyUser(String username, String password) {
        String authToken = apiClient.getAuthToken();
        log.info("Send request to {}", VERIFY_USER_PATH);
        UserDataDTO userData = UserDataBuilder.createUserData(username, password);
        given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .body(userData)
                .when()
                .post(VERIFY_USER_PATH)
                .then()
                .log()
                .ifError()
                .statusCode(200);
    }

    public Response editUser(UserDataDTO userData) {
        String authToken = apiClient.getAuthToken();
        log.info("Send PUT request to {} to update user {}", USERS_PATH, userData.getUsername());

        Response response = given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .body(userData)
                .when()
                .put(USERS_PATH)
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .extract()
                .response();
        return response;
    }

    public void deleteUser(String username) {
        String authToken = apiClient.getAuthToken();
        log.info("Send DELETE request to {} to delete user", USERS_PATH+ "/" + username);
        String path = DELETE_USER_PATH + username;

        given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .when().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                .delete(path)
                .then()
                .log()
                .ifError()
                .statusCode(200);
    }

    public Response getAllGroups() {
        String authToken = apiClient.getAuthToken();
        log.info("Send request to {} to get all groups", GET_GROUPS_PATH);

        Response response = given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .when()
                .get(GET_GROUPS_PATH)
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .extract()
                .response();
        return response;
    }

    public Response createGroup(String groupName, String userName) throws JSONException {
        String authToken = apiClient.getAuthToken();
        log.info("Send request to {} to get all groups", CREATE_GROUP_PATH);
        JSONObject request = new JSONObject();
        request.put("groupName", groupName);
        request.put("userName", userName);

        Response response = given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .body(request.toString())
                .when().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                .post(CREATE_GROUP_PATH)
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .extract()
                .response();
        return response;
    }

    public void editGroup(String newGroupName, String oldGroupName, String role) throws JSONException {
        String authToken = apiClient.getAuthToken();
        log.info("Send request to {} to get all groups", EDIT_GROUP_PATH);
        JSONObject request = new JSONObject();
        request.put("newGroupName", newGroupName);
        request.put("oldGroupName", oldGroupName);
        request.put("role", role);

        given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .body(request.toString())
                .when().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                .put(EDIT_GROUP_PATH)
                .then()
                .log()
                .ifError()
                .statusCode(200);
    }

    public void deleteGroup(String group) {
        String authToken = apiClient.getAuthToken();
        String path = DELETE_GROUP_PATH + "/" + group;
        log.info("Send DELETE request to {} to delete group", path);

        given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .when().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                .delete(path)
                .then()
                .log()
                .ifError()
                .statusCode(200);
    }
}
