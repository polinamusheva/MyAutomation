package com.example.my_automation;


import com.example.my_automation.builder.UserDataBuilder;
import com.example.my_automation.clients.LayoutServiceClient;
import com.example.my_automation.clients.UserManagementClient;
import com.example.my_automation.config.ApiConfiguration;
import com.example.my_automation.constants.Constants;
import com.example.my_automation.dto.GroupDTO;
import com.example.my_automation.dto.LayoutDTO;
import com.example.my_automation.dto.SearchProjectsDTO;
import com.example.my_automation.builder.SearchProjectsBuilder;
import com.example.my_automation.dto.UserDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;

import java.io.IOException;
import java.util.*;

import static com.example.my_automation.constants.Constants.Sorting.DEFAULT_SORTING;

@SpringBootTest
@ContextConfiguration(classes = ApiConfiguration.class)
public abstract class BaseTests extends AbstractTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(BaseTests.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private LayoutServiceClient layoutServiceClient;

    @Autowired
    UserManagementClient userManagementClient;

    protected Object[][] prepareTestDataToSearchProjects(ClassPathResource searchParamsJson) throws IOException {
        SearchProjectsDTO[] inputCases = mapper.readValue(searchParamsJson.getInputStream(), SearchProjectsDTO[].class);
        return Arrays.stream(inputCases).map(s -> new Object[]{s.getName(), s.getSort(), s.getCreators(), s.getStatus()}).toArray(Object[][]::new);
    }

    protected Object[][] prepareTestDataForSorting() {
        return new Object[][]{
                {"creationDate desc"},
                {"creationDate asc"},
                {"name desc"},
                {"name asc"},
                {"lastModifiedDate desc"},
                {"lastModifiedDate asc"},
                {"createdBy desc"},
                {"createdBy asc"},
        };
    }

    protected String createOrReuseCase(String projectName, ITestContext context) {
        context.setAttribute(Constants.Architecture.ARCH_PROJECT_NAME, projectName);
        log.info("Creating case with name {} if not exist", projectName);
        Response caseResponse = layoutServiceClient.getAllProjects(projectName);
        LayoutDTO[] cases = caseResponse.getBody().as(LayoutDTO[].class);
        String caseId;
        if (caseResponse.statusCode() == 200 && cases.length > 0) {
            log.info("Case with name {} exist. Try to get caseId ...", projectName);
            LayoutDTO projectFirst = cases[0];
            caseId = projectFirst.getId();
        } else {
            log.info("Case with name {} NOT exist. Creating case ...", projectName);
            caseId = layoutServiceClient.createProject(projectName);
        }
        context.setAttribute(Constants.Parameters.PROJECT_ID, caseId);
        log.info("Case with name {} is available. CaseId: {}...", projectName, caseId);
        Reporter.log("Case ID: " + caseId);
        return caseId;
    }

    protected LayoutDTO[] searchCases() {
        SearchProjectsDTO casesFilterRequest = SearchProjectsBuilder.createProjectsSearchRequest();
        Response response = layoutServiceClient.searchProjects(casesFilterRequest);
        return response.getBody().as(LayoutDTO[].class);
    }

    protected LayoutDTO[] searchProjects(String name, String sort, List<String> creators, List<String> status) {
        SearchProjectsDTO casesFilterRequest = SearchProjectsBuilder.createProjectsSearchRequest(name, sort, creators, status);
        Response response = layoutServiceClient.searchProjects(casesFilterRequest);
        return response.getBody().as(LayoutDTO[].class);
    }

    protected LayoutDTO[] searchProjects(List<String> creators) {
        String name = "";
        String sort = DEFAULT_SORTING;
        List<String> status = new ArrayList<>();

        SearchProjectsDTO casesFilterRequest = SearchProjectsBuilder.createProjectsSearchRequest(name, sort, creators, status);
        Response response = layoutServiceClient.searchProjects(casesFilterRequest);
        return response.getBody().as(LayoutDTO[].class);
    }


    protected LayoutDTO updateCase(LayoutDTO caseDTO, boolean expectedSuccess) {
        Response res = layoutServiceClient.updateProject(caseDTO);
        if (expectedSuccess) {
            Assert.assertEquals(res.getStatusCode(), Constants.StatusCodes.SUCCESS_CODE);
            return res.getBody().as(LayoutDTO.class);
        } else {
            Assert.assertEquals(res.getStatusCode(), Constants.StatusCodes.FORBIDDEN_CODE);
            return null;
        }
    }

    protected LayoutDTO getProjectById(String caseId, boolean expectedSuccess) {
        Response res = layoutServiceClient.getProjectByid(caseId);
        if (expectedSuccess) {
            Assert.assertEquals(res.getStatusCode(), Constants.StatusCodes.SUCCESS_CODE);
            return res.getBody().as(LayoutDTO.class);
        } else {
            Assert.assertEquals(res.getStatusCode(), Constants.StatusCodes.FORBIDDEN_CODE);
            return null;
        }
    }

    protected void deleteProjectById(String id, String username, String password, boolean expectedSuccess) {
        String encodedPass = encodeString(password);
        String encodedUsername = encodeString(username);
        userManagementClient.verifyUser(encodedUsername, encodedPass);
        Response res = layoutServiceClient.deleteProject(id);
        if (expectedSuccess) {
            Assert.assertEquals(res.getStatusCode(), Constants.StatusCodes.SUCCESS_CODE);
        } else {
            Assert.assertEquals(res.getStatusCode(), Constants.StatusCodes.FORBIDDEN_CODE);
        }
    }


    protected UserDataDTO createUser(String username, String password, List<String> roles, List<String> groups, boolean expectedSuccess) {
        String encoded = encodeString(password);
        UserDataDTO userData = UserDataBuilder.createUserData(username, encoded, roles, groups);
        Response response = userManagementClient.createUser(userData);
        if (expectedSuccess) {
            Assert.assertEquals(response.getStatusCode(), Constants.StatusCodes.SUCCESS_CODE);
        } else {
            Assert.assertEquals(response.getStatusCode(), Constants.StatusCodes.CONFLICT_CODE);
        }
        return userData;
    }

    protected void editUser(String firstName, String lastName, List<String> groups, UserDataDTO userToEdit) {
        UserDataDTO userData = UserDataBuilder.editUserData(firstName, lastName, groups, userToEdit);
        userManagementClient.editUser(userData);
    }

    protected void deleteUser(String username) {
        userManagementClient.deleteUser(username);
    }

    protected void createOrReuseUser(String username, String password, List<String> roles, List<String> groups, boolean expectedSuccess) {
        log.info("Get all users and check if {} exist", username);
        List<UserDataDTO> users = userManagementClient.getAllUsers().as(new TypeRef<>() {
        });

        UserDataDTO foundUser = users
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (foundUser == null) {
            log.info("User {} does NOT exist. Creating user ...", username);
            createUser(username, password, roles, groups, expectedSuccess);
        } else {
            editUser(foundUser.getFirstName(), foundUser.getLastName(), groups, foundUser);
            log.info("User {} exists. User ID: {}", username, foundUser.getId());
        }
    }

    public void createOrReuseGroup(String groupName, String username) throws JSONException {
        log.info("Create new group with name: {}", groupName);
        List<GroupDTO> allGroups = userManagementClient.getAllGroups().as(new TypeRef<>() {
        });
        if (allGroups.stream().anyMatch(g -> g.getGroupName().equals(groupName))) {
            log.info("Group with name: {} already exists", groupName);
        } else {
            userManagementClient.createGroup(groupName, username);
            log.info("Group with name: {} created successfully", groupName);
        }
    }

    protected String encodeString(String input) {
        return Base64.getEncoder().encodeToString((input).getBytes());
    }
}