package com.example.my_automation.clients;

import com.example.my_automation.builder.RequestBuilder;
import com.example.my_automation.dto.BaseRequestDto;
import com.example.my_automation.dto.SearchDTO;
import com.example.my_automation.dto.SearchRequestDTO;
import com.example.my_automation.enums.Status;
import com.example.my_automation.utils.RequestSpecifications;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.example.my_automation.constants.Constants.Interior.*;
import static io.restassured.RestAssured.*;

@Component
public class ProjectsServiceClient extends MyAppApiClient {

    private static final Logger log = LoggerFactory.getLogger(ProjectsServiceClient.class);

    @Autowired
    private RequestSpecifications requestSpecifications;

    private ObjectMapper mapper;

    private static final String BASE_PATH = "/projects";
    private static final String SEARCH_PATH = BASE_PATH + "/search";
    private static final String SEARCH_ALL_PROJECTS_PATH = SEARCH_PATH + "/projects";
    private static final String GET_CATALOG_PATH = BASE_PATH + "/get-catalog";
    private static final String TOP_PROJECTS_PATH = SEARCH_PATH + "/top-projects";

    public Response search(SearchRequestDTO searchRequestDto, List<String> terms) {
        log.info("Search for: ");
        for (String term : terms) {
            log.info(term);
        }
        log.debug("Make POST request to: {}{}", baseURI, SEARCH_PATH);
        String authToken = getAuthToken();
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(searchRequestDto)
                        .when()
                        .post(SEARCH_PATH)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish POST request to: {}{}", baseURI, SEARCH_PATH);
        return response;
    }

    //Response object: DiscoveryCatalogSourceDto
    public Response getCatalog() {
        log.info("Get catalog from discovery.");
        log.debug("Make GET request to: {}{}", baseURI, GET_CATALOG_PATH);
        String authToken = getAuthToken();
        Response response = given().spec(requestSpecifications.getApiSpecsSearch(authToken))
                                   .when().get(GET_CATALOG_PATH).then().log().ifError().statusCode(200).extract().response();

        return response;
    }

    public Response getProjectsResultSearch(BaseRequestDto baseRequestDto, String projectsToSort) {
        String projectsPath = !Objects.equals(projectsToSort, PROJECTS_ALL)
                ? PROJECTS_RESULTS
                : SEARCH_ALL_PROJECTS_PATH;

        log.info("Get Projects Search result for searchIDs: {}", baseRequestDto.getSearchIDs());
        log.debug("Make POST request to: {}{}", baseURI,  projectsPath);
        String authToken = getAuthToken();
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(baseRequestDto)
                        .when()
                        .post(projectsPath)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish POST request to: {}:{}{}{}", baseURI, port, basePath, projectsPath);
        return response;
    }

    public Response getProjectypes(BaseRequestDto baseRequestDto) {
        log.info("Get All Projects for searchIDs: {}", baseRequestDto.getSearchIDs());
        log.debug("Make POST request to: {}:{}{}{}", baseURI, port, basePath, SEARCH_ALL_PROJECTS_PATH);
        String authToken = getAuthToken();
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(baseRequestDto)
                        .when()
                        .post(SEARCH_ALL_PROJECTS_PATH)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish POST request to: {}:{}{}{}", baseURI, port, basePath, SEARCH_ALL_PROJECTS_PATH);
        return response;
    }

    public Response getRecentSearch(BaseRequestDto dto) {
        log.info("Get Discovery Recent Search results");
        log.debug("Make POST request to: {}:{}{}{}", baseURI, port, basePath, SEARCH_PATH);
        String authToken = getAuthToken();
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(dto)
                        .when()
                        .post(SEARCH_PATH)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish POST request to: {}:{}{}{}", baseURI, port, basePath, SEARCH_PATH);
        return response;
    }


    public Callable<Boolean> isProjectsSearchFinished(Long discoverySearchId) {
        return new Callable<Boolean>() {
            public Boolean call() {
                log.info("Checking if Discovery Search with ID: {} is completed...", discoverySearchId);
                mapper = new ObjectMapper();
                SearchDTO foundSearch = null;
                Status status = null;
                try {
                    BaseRequestDto dto = RequestBuilder.createSearchProjectDTO(List.of(discoverySearchId), 1);
                    JsonNode recentSearches = getRecentSearch(dto).getBody().as(JsonNode.class);
                    if (recentSearches != null) {
                        JsonNode searchDtos = recentSearches.get("entities");
                        List<SearchDTO> searches = mapper.convertValue(searchDtos, new TypeReference<List<SearchDTO>>() {
                        });
                        
                        if (searches != null && !searches.isEmpty()) {
                            for (SearchDTO currentSearch : searches) {
                                if (discoverySearchId.equals(currentSearch.getId())) {
                                    foundSearch = currentSearch;
                                    break;
                                }
                            }
                        }
                    }
                    status = foundSearch.getStatus();
                } catch (NullPointerException e) {
                    log.warn("Still not have status for Discovery Recent Actor Search with ID: {}", discoverySearchId);
                    return false;
                }
                log.info("Discovery Search with ID: {} has status: {}", discoverySearchId, status.getSource());
                return STATUS_FINISHED.equals(status.getSource());
            }
        };
    }

    public Response deleteSearch(Long searchId) {
        log.info("Delete Discovery search with ID: {}", searchId);

        log.debug("Make DELETE request to: {}:{}{}{}", baseURI, port, basePath, SEARCH_PATH);
        String authToken = getAuthToken();
        Response response =
                given()
                        .spec(requestSpecifications.getApiSpecsSearch(authToken))
                        .body(Arrays.asList(searchId))
                        .when()
                        .delete(SEARCH_PATH)
                        .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();
        log.debug("Finish POST request to: {}:{}{}{}", baseURI, port, basePath, SEARCH_PATH);
        return response;
    }

    public boolean verifyLinkStatusCode(String link) {
        Response response =
                given()
                        .spec(requestSpecifications.getImageSpec())
                        .when()
                        .get(link);
        return response.getStatusCode() == 200;
    }
}
