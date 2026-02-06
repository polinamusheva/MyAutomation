package com.example.my_automation.clients;


import com.example.my_automation.builder.ProjectBuilder;
import com.example.my_automation.constants.Constants;
import com.example.my_automation.dto.LayoutDTO;
import com.example.my_automation.dto.SearchProjectsDTO;
import com.example.my_automation.utils.RequestSpecifications;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@Component
public class LayoutServiceClient extends MyAppApiClient {

    private static final Logger log = LoggerFactory.getLogger(LayoutServiceClient.class);

    @Autowired
    private RequestSpecifications requestSpecifications;

    private static final String PROJECT_PATH = "/projects";
    private static final String RELATED_PROJECT_PATH = "/projects/{related_projectId}";
    private static final String ID_PATH = "id";
    private static final String CASE_ID_PATH = PROJECT_PATH + "/{caseId}";
    private static final String SEARCH_PATH = "/search";
    private static final String SEARCH_CASES_PATH = SEARCH_PATH + "/repository/search_cases";


    public String createCase(String caseName) {
        LayoutDTO caseDTO = ProjectBuilder.createProjectForTest(caseName);
        Response res = createCase(caseDTO);
        Assert.assertEquals(res.getStatusCode(), 200);
        return res.getBody().path(ID_PATH);
    }

    public LayoutDTO createProject(String name, String description, String priority, LocalDateTime dueDate, String avatar) {
        LayoutDTO caseDTO = ProjectBuilder.createProjectForTest(name, description, priority, dueDate, avatar);
        Response res = createProject(caseDTO);
        Assert.assertEquals(res.getStatusCode(), 200);
        return res.getBody().as(LayoutDTO.class);
    }

    public Response searchProjects(SearchProjectsDTO casesSearchRequest) {
        log.info("Get all investigations from first page");
        log.debug("Make POST request to: {}", baseURI + SEARCH_CASES_PATH);
        String authToken = getAuthToken();
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(casesSearchRequest)
                        .when().log().all()
                        .post(SEARCH_CASES_PATH)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish POST request to: {}", baseURI + SEARCH_CASES_PATH);
        return response;
    }

    public Response getProjectByid(String id) {
        log.info("Get case by CaseId {}", id);
        log.debug("Make GET request to: {}{}", baseURI, CASE_ID_PATH);
        String url = CASE_ID_PATH.replace("{caseId}", id);
        String authToken = getAuthToken();
        Response response =
                given()
                        .queryParam(Constants.Parameters.RELATIONS, "members")
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .when()
                        .get(url)
                        .then()
                        .log().ifError()
                        .extract().response();
        log.debug("Finish GET request to: {}", baseURI + url);
        return response;
    }

    public Response getAllProjects(String caseName) {
        log.info("Search in all cases for caseName: {}", caseName);
        log.debug("Make GET request to: {}{}", baseURI, PROJECT_PATH);
        String authToken = getAuthToken();
        Response response =
                given()
                        .queryParam(Constants.Parameters.PAGE_PARAM, "1")
                        .queryParam(Constants.Parameters.NAME, caseName)
                        .queryParam(Constants.Parameters.PAGE_SIZE, "1")
                        .queryParam(Constants.Parameters.SORT, "creationDate desc")
                        .queryParam(Constants.Parameters.RELATIONS, "members")
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .when()
                        .get(PROJECT_PATH)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish GET request to: {}", baseURI + PROJECT_PATH);
        return response;
    }

    public Response updateProject(LayoutDTO caseDTO) {
        log.info("Update case with caseId {}", caseDTO.getId());
        log.debug("Make GET request to: {}", baseURI + CASE_ID_PATH);
        String url = CASE_ID_PATH.replace("{caseId}", caseDTO.getId());
        String authToken = getAuthToken();
        LayoutDTO requestBody = ProjectBuilder.createProjectForUpdate(caseDTO.getName(),
                caseDTO.getDescription(), caseDTO.getPriority(), caseDTO.getDueDate(), caseDTO.getStatus(), caseDTO.getUploadedAvatar());
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(requestBody)
                        .when()
                        .log().all()
                        .put(url)
                        .then()
                        .extract().response();
        log.debug("Finish GET request to: {}", baseURI + CASE_ID_PATH);
        return response;
    }


    public Response createProject(LayoutDTO caseDTO) {
        String authToken = getAuthToken();
        log.info("Creating case with name \"{}\" ...", caseDTO.getName());
        log.debug("Make POST request to: {}", baseURI + PROJECT_PATH);
        Response response = given()
                .spec(requestSpecifications.getApiSpecsSearch(authToken))
                .body(caseDTO)
                .when().post(PROJECT_PATH)
                .then().log().ifError().extract().response();
        log.info(response.prettyPrint());
        log.debug("Finish POST request to: {}", baseURI + PROJECT_PATH);
        log.info("Creation of case \"{}\" finished.", caseDTO.getName());
        return response;
    }

    public Response deleteProject(String id) {
        String authToken = getAuthToken();
        String url = CASE_ID_PATH.replace("{caseId}", id);
        log.info("Deleting case with caseId {}: ", id);
        log.debug("Make POST request to: {}", baseURI + url);
        Response response = given().spec(requestSpecifications.getApiSpecsSearch(authToken))
                .when().delete(url).then().log().ifError().extract().response();
        log.debug("Finish DELETE request to: {}", baseURI + url);
        log.info("Deletion of case with caseID: {} finished.", id);
        return response;
    }
}