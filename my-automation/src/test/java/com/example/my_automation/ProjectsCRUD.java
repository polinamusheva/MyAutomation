package com.example.my_automation;

import com.example.my_automation.clients.LayoutServiceClient;
import com.example.my_automation.auth.Authentication;
import com.example.my_automation.config.ApiConfiguration;
import com.example.my_automation.constants.Constants;
import com.example.my_automation.dto.LayoutDTO;
import com.example.my_automation.utils.CustomTestNgListener;
import com.example.my_automation.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import org.jetbrains.annotations.NotNull;

import static com.example.my_automation.config.ApiConfiguration.PASSWORD;
import static com.example.my_automation.config.ApiConfiguration.USERNAME;
import static com.example.my_automation.constants.Constants.Parameters.ARCHIVE;
import static com.example.my_automation.constants.Constants.Parameters.CLOSED;
import static com.example.my_automation.constants.Constants.Sorting.*;
import static com.example.my_automation.expected.Investigations.EXPECTED_INVESTIGATIONS_NAMES_ASC;
import static com.example.my_automation.expected.Investigations.EXPECTED_INVESTIGATIONS_NAMES_DESC;

@ContextConfiguration(classes = ApiConfiguration.class)
@Authentication(username = USERNAME, password = PASSWORD)
@Listeners(CustomTestNgListener.class)
public class ProjectsCRUD extends BaseTests {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    private static final Logger log = LoggerFactory.getLogger(ProjectsCRUD.class);
    private final ClassPathResource LAYOUT_CREATION = new ClassPathResource("input/create_case_options.json");
    private final ClassPathResource LAYOUT_FILTERING = new ClassPathResource("input/cases_filter_options.json");
    private final ClassPathResource LAYOUT_SORTING = new ClassPathResource("input/cases_sort_options.json");
    private final ClassPathResource LAYOUT_SEARCHING = new ClassPathResource("input/cases_search_options.json");
    private static final String NEW = " new";

    private static final String NEW_DESCRIPTION = "some description";
    private final LayoutDTO[] inputProjects = mapper.readValue(LAYOUT_CREATION.getInputStream(), LayoutDTO[].class);

    @Autowired
    private LayoutServiceClient layoutServiceClient;


    @DataProvider(name = "projectsSearch")
    public Object[][] projectsSearch() throws IOException {
        return super.prepareTestDataToSearchProjects(LAYOUT_SEARCHING);
    }

    @DataProvider(name = "projectsSorting")
    public Object[][] projectsSorting() throws IOException {
        return super.prepareTestDataToSearchProjects(LAYOUT_SORTING);
    }

    @DataProvider(name = "projectsFilter")
    public Object[][] projectsFilter() throws IOException {
        return super.prepareTestDataToSearchProjects(LAYOUT_FILTERING);
    }

    public ProjectsCRUD() throws IOException {
    }

    @Test(description = "Delete projects", priority = 1)
    public void clearProjects() {
        LayoutDTO[] projectsByUser = super.searchProjects(List.of(USERNAME));
        for (LayoutDTO layoutDTO : projectsByUser) {
            super.deleteProjectById(layoutDTO.getId(), USERNAME, PASSWORD, true);
            TestUtils.sleep(500);
        }
    }

    @Test(description = "Create projects with different values", priority = 2)
    public void createProject(ITestContext context) {
        HashMap<String, LayoutDTO> tempProjects = new HashMap<>();

        for (LayoutDTO inputProjects : inputProjects) {
            Response response = layoutServiceClient.getAllProjects(inputProjects.getName());
            LayoutDTO[] cases = response.getBody().as(LayoutDTO[].class);
            List<LayoutDTO> foundProjects = Arrays.stream(cases).filter(c -> c.getName().equals(inputProjects.getName())).toList();

            if (foundProjects.isEmpty()) {
                LayoutDTO createdProject = layoutServiceClient.createProject(
                        inputProjects.getName(),
                        inputProjects.getDescription(),
                        inputProjects.getPriority(),
                        inputProjects.getDueDate(),
                        null);
                tempProjects.put(createdProject.getId(), createdProject);
            } else {
                log.info("Case with name {} exists. New case won't be created. Getting the id of the existing case with the same name", inputProjects.getName());
                tempProjects.put(foundProjects.get(0).getId(), foundProjects.get(0));
            }
        }
        context.setAttribute(Constants.Parameters.TEMP_PROJECTS, tempProjects);
    }

    @Test(description = "Get existing case",
            dependsOnMethods = "createProject",
            priority = 3)
    public void getProject(ITestContext context) {
        @SuppressWarnings("unchecked")
        HashMap<String, LayoutDTO> projects = (HashMap<String, LayoutDTO>) context.getAttribute(Constants.Parameters.TEMP_PROJECTS);
        log.info("Get all newly created cases by Id");
        for (String id : projects.keySet()) {
            LayoutDTO foundCase = super.getProjectById(id, true);
            log.info("Assert that project name is \"{}\"", projects.get(id).getName());
            Assert.assertEquals(foundCase.getName(), projects.get(id).getName());
            log.info("Assert project priority is \"{}\"", projects.get(id).getPriority());
            Assert.assertEquals(foundCase.getPriority(), projects.get(id).getPriority());
            log.info("Assert that project description is \"{}\"", projects.get(id).getDescription());
            Assert.assertEquals(foundCase.getDescription(), projects.get(id).getDescription());
            log.info("Assert that project is created by user \"{}\"", projects.get(id).getCreatedBy());
            Assert.assertEquals(foundCase.getCreatedBy(), projects.get(id).getCreatedBy());
        }
        log.info("All projects are retrieved successfully");
    }

    @Test(description = "Search existing projects",
            dependsOnMethods = "createProject",
            dataProvider = "projectsSearch",
            priority = 4)
    public void searchProjectsByString(String name, String sort, List<String> creators, List<String> status) {
        List<LayoutDTO> foundProjects = List.of(super.searchProjects(name, sort, creators, status));
        for (LayoutDTO foundProject : foundProjects) {
            Assert.assertTrue(foundProject.getName().toLowerCase().contains(name.toLowerCase()) ||
                    foundProject.getDescription().toLowerCase().contains(name.toLowerCase()));

        }
    }

    @Test(description = "Sort existing projects",
            dependsOnMethods = "createProject",
            dataProvider = "projectsSorting",
            priority = 5)
    public void sortProjects(String name, String sort, List<String> creators, List<String> status) throws ParseException {
        List<LayoutDTO> sorted = List.of(super.searchProjects(name, sort, creators, status));
        Assert.assertFalse(sorted.isEmpty(), "No cases were returned from sorting by " + sort);
        LocalDateTime expectedDate;
        List<String> resultNames = sorted.stream().map(LayoutDTO::getName).map(String::toLowerCase).toList();
        List<Integer> priorities = getPriorities(sorted);
        List<String> expectedNames;
        Integer expectedPriority;
        LayoutDTO tempCase;
        SimpleDateFormat format = new SimpleDateFormat(Constants.SHORT_FORMAT);
        switch (sort) {
            case DEFAULT_SORTING:
                expectedDate = LocalDateTime.now().plusDays(1);
                for (LayoutDTO caseDTO : sorted) {
                    LocalDateTime creationDate = caseDTO.getCreationDate();
                    log.info("Creation Date is: {}", creationDate);
                    log.info("Expected Date is: {}", expectedDate);
                    Assert.assertTrue(creationDate.isBefore(expectedDate));
                    expectedDate = creationDate;
                }
                break;
            case CREATION_DATE_ASC:
                expectedDate = LocalDateTime.now().minusYears(5);
                for (LayoutDTO caseDTO : sorted) {
                    LocalDateTime creationDate = caseDTO.getCreationDate();
                    log.info("Creation Date is: {}", creationDate);
                    log.info("Expected Date is: {}", expectedDate);
                    Assert.assertTrue(creationDate.isAfter(expectedDate));
                    expectedDate = creationDate;
                }
                break;
            case NAME_DESC:
                if (!creators.isEmpty()) {
                    expectedNames = EXPECTED_INVESTIGATIONS_NAMES_DESC.stream().map(String::toLowerCase).toList();
                    Assert.assertEquals(resultNames, expectedNames, "Sorting by name descending is not correct");
                }
                break;
            case NAME_ASC:
                if (!creators.isEmpty()) {
                    expectedNames = EXPECTED_INVESTIGATIONS_NAMES_ASC.stream().map(String::toLowerCase).toList();
                    Assert.assertEquals(resultNames, expectedNames, "Sorting by name ascending is not correct");
                }
                break;
            case MODIFIED_DESC:
                expectedDate = LocalDateTime.now().plusDays(1);
                for (LayoutDTO caseDTO : sorted) {
                    LocalDateTime modifiedDate = caseDTO.getLastModifiedDate();
                    log.info("Modified Date is: {}", modifiedDate);
                    log.info("Expected Date is: {}", expectedDate);
                    Assert.assertTrue(modifiedDate.isBefore(expectedDate));
                    expectedDate = modifiedDate;
                }
                break;
            case MODIFIED_ASC:
                expectedDate = LocalDateTime.now().minusYears(5);
                for (LayoutDTO caseDTO : sorted) {
                    LocalDateTime modifiedDate = caseDTO.getLastModifiedDate();
                    log.info("Modified Date is: {}", modifiedDate);
                    log.info("Expected Date is: {}", expectedDate);
                    Assert.assertTrue(modifiedDate.isAfter(expectedDate));
                    expectedDate = modifiedDate;
                }
                break;
            case PRIORITY_DESC:
                expectedPriority = priorities.get(0);
                for (Integer priority : priorities) {
                    Assert.assertTrue(priority <= expectedPriority, "Sorting by priority descending is not correct");
                }
                break;
            case PRIORITY_ASC:
                expectedPriority = priorities.get(0);
                for (Integer priority : priorities) {
                    Assert.assertTrue(priority >= expectedPriority, "Sorting by priority ascending is not correct");
                }

                break;
            case DUE_DATE_DESC:
                tempCase = sorted.stream().filter(c -> c.getDueDate() != null).findFirst().orElse(null);
                if (tempCase != null) {
                    int i = sorted.indexOf(tempCase);
                    List<LayoutDTO> sortedNoNull = sorted.subList(i, sorted.size());
                    expectedDate = tempCase.getDueDate();

                    for (LayoutDTO caseDTO : sortedNoNull) {
                        if (caseDTO.getDueDate() != null) {
                            LocalDateTime dueDate = caseDTO.getDueDate();
                            Assert.assertFalse(dueDate.isAfter(expectedDate));
                            expectedDate = dueDate;
                        } else {
                            throw new AssertionError("Sorting by dueDate descending is not correct");
                        }
                    }
                }
                break;

            default:
                LocalDateTime date = sorted.get(0).getDueDate();
                if (date != null) {
                    tempCase = sorted.stream().filter(c -> c.getDueDate() == null).findFirst().orElse(null);
                    if (tempCase != null) {
                        int i = sorted.indexOf(tempCase);
                        List<LayoutDTO> sortedNoNull = sorted.subList(0, i - 1);
                        expectedDate = date;
                        for (LayoutDTO caseDTO : sortedNoNull) {
                            if (caseDTO.getDueDate() != null) {
                                LocalDateTime dueDate = caseDTO.getDueDate();
                                Assert.assertFalse(dueDate.isBefore(expectedDate));
                                expectedDate = dueDate;
                            } else {
                                throw new AssertionError("Sorting by dueDate ascending is not correct");
                            }
                        }
                    }
                } else {
                    tempCase = sorted.stream().filter(c -> c.getDueDate() != null).findFirst().orElse(null);
                    if (tempCase != null) {
                        throw new AssertionError("Sorting by dueDate ascending is not correct");
                    }
                }
        }

    }

    @Test(description = "Update existing cases", dependsOnMethods = "createProject", priority = 6)
    public void updateCase(ITestContext context) throws ParseException {
        log.info("Change the priority, name, description, dueDate and status for each caseDTO");
        ArrayList<LayoutDTO> casesWithChanges = new ArrayList<>();
        @SuppressWarnings("unchecked")
        HashMap<String, LayoutDTO> cases = (HashMap<String, LayoutDTO>) context.getAttribute(Constants.Parameters.TEMP_PROJECTS);
        for (String id : cases.keySet()) {
            log.info("Get case by id {}", id);
            LayoutDTO inputCase = super.getProjectById(id, true);
            if (inputCase.getDueDate() != null) {
                LocalDateTime date = inputCase.getDueDate();
                inputCase.setDueDate(date);
            }

            log.info("Change priority for case \"{}\"", inputCase.getName());
            changePriority(inputCase);
            LayoutDTO updatedCase = super.updateCase(inputCase, true);
            Assert.assertEquals(updatedCase.getPriority(), inputCase.getPriority(), "Priority is not successfully updated");

            log.info("Change name for case \"{}\"", inputCase.getName());
            log.debug("Append \"new\" to the name of case \"{}\"", inputCase.getName());
            inputCase.setName(inputCase.getName() + NEW);
            updatedCase = super.updateCase(inputCase, true);
            Assert.assertEquals(updatedCase.getName(), inputCase.getName(), "Name is not successfully updated");

            log.info("Change description for case \"{}\"", inputCase.getName());
            log.debug("Append or add \"new\" to the description of case \"{}\"", inputCase.getName());
            if (inputCase.getDescription() != null) {
                inputCase.setDescription(inputCase.getDescription() + NEW);
            } else {
                inputCase.setDescription(NEW_DESCRIPTION);
            }
            updatedCase = super.updateCase(inputCase, true);
            Assert.assertEquals(updatedCase.getDescription(), inputCase.getDescription(), "Description is not successfully updated");

            LocalDateTime dueDate;
            if (inputCase.getDueDate() != null) {
                dueDate = inputCase.getDueDate().plus(Period.ofDays(10));

                log.info("Update dueDate for case \"{}\"", inputCase.getName());
                inputCase.setDueDate(dueDate);
            }
            updatedCase = super.updateCase(inputCase, true);
            if (inputCase.getDueDate() != null) {
                Assert.assertEquals(updatedCase.getDueDate(), inputCase.getDueDate(), "DueDate is not successfully updated");
            }

            casesWithChanges.add(inputCase);
        }

        for (LayoutDTO projectWithChanges : casesWithChanges) {
            if (casesWithChanges.indexOf(projectWithChanges) % 2 == 0) {
                log.info("Set status \"closed\" for case \"{}\"", projectWithChanges.getName());
                projectWithChanges.setStatus(CLOSED);
            } else if (casesWithChanges.indexOf(projectWithChanges) % 3 == 0) {
                log.info("Set status \"archive\" for case \"{}\"", projectWithChanges.getName());
                projectWithChanges.setStatus(ARCHIVE);
            }
            LayoutDTO updatedCase = super.updateCase(projectWithChanges, true);
            Assert.assertEquals(updatedCase.getUploadedAvatar(), projectWithChanges.getUploadedAvatar(), "Status is not successfully updated");
        }

        log.info("Publish the updated data to cases");
        for (LayoutDTO caseDTO : casesWithChanges) {
            LayoutDTO updatedCase = super.updateCase(caseDTO, true);
            Assert.assertEquals(updatedCase.getName(), caseDTO.getName());
            Assert.assertEquals(updatedCase.getPriority(), caseDTO.getPriority());

            Assert.assertEquals(updatedCase.getUploadedAvatar(), caseDTO.getUploadedAvatar());
            Assert.assertEquals(updatedCase.getPriority(), caseDTO.getPriority());

            if (caseDTO.getDueDate() != null) {
                Assert.assertEquals(updatedCase.getDueDate(), caseDTO.getDueDate());
            }
        }
    }

    @Test(description = "Filter projects", dataProvider = "projectsFilter", dependsOnMethods = "clearProjects", priority = 8)
    public void filterProjects(ITestContext context, String name, String sort, List<String> creators, List<String> status) {

        log.info("Filter cases by creators: {} and status: {}", creators.toString(), status.toString());
        List<LayoutDTO> filteredCases = List.of(super.searchProjects(name, sort, creators, status));
        log.info("Get all cases, created by user: {}", USERNAME);
        List<LayoutDTO> updatedCases = new ArrayList<>();
        @SuppressWarnings("unchecked")
        HashMap<String, LayoutDTO> cases = (HashMap<String, LayoutDTO>) context.getAttribute(Constants.Parameters.TEMP_PROJECTS);
        for (String id : cases.keySet()) {
            updatedCases.add(super.getProjectById(id, true));
        }
        log.info("Select the expected cases");
        List<LayoutDTO> expected = updatedCases.stream().filter(c -> (status.contains(c.getStatus()) && creators.contains(c.getCreatedBy()))).toList();
        log.info("Assert that filtered cases equal expected");
        Assert.assertEquals(filteredCases.size(), expected.size());
        for (LayoutDTO filteredCase : filteredCases) {
            Assert.assertTrue(status.contains(filteredCase.getStatus()) || creators.contains(filteredCase.getCreatedBy()));
        }
    }

    @Test(description = "Delete cases", priority = 9)
    public void deleteProject() {
        LayoutDTO[] casesByUser = super.searchProjects(List.of(USERNAME));
        for (LayoutDTO caseDTO : casesByUser) {
            super.deleteProjectById(caseDTO.getId(), USERNAME, PASSWORD, true);
            TestUtils.sleep(500);
        }
    }

    private void changePriority(LayoutDTO caseDTO) {
        if (caseDTO.getPriority().equals(Constants.Parameters.REGULAR)) {
            caseDTO.setPriority(Constants.Parameters.MEDIUM);
        } else if (caseDTO.getPriority().equals(Constants.Parameters.MEDIUM)) {
            caseDTO.setPriority(Constants.Parameters.HIGH);
        } else {
            caseDTO.setPriority(Constants.Parameters.REGULAR);
        }
    }

    @NotNull
    private static List<Integer> getPriorities(List<LayoutDTO> sorted) {
        List<Integer> priorities = new ArrayList<>();
        for (LayoutDTO caseDTO : sorted) {
            String expectedPriority = caseDTO.getPriority();
            switch (expectedPriority) {
                case Constants.Parameters.HIGH:
                    priorities.add(2);
                    break;
                case Constants.Parameters.MEDIUM:
                    priorities.add(1);
                    break;
                default:
                    priorities.add(0);
            }
        }
        return priorities;
    }
}
