package com.melody.todoquadrantappback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melody.todoquadrantappback.dto.CreateTaskRequest;
import com.melody.todoquadrantappback.exception.TaskNotFoundException;
import com.melody.todoquadrantappback.model.Task;
import com.melody.todoquadrantappback.model.User;
import com.melody.todoquadrantappback.service.TaskService;
import com.melody.todoquadrantappback.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb+srv://melody:yu@cluster0.whmavgi.mongodb.net/testdb?retryWrites=true&w=majority",
        "FRONTEND_URL=http://localhost:5173"
})
class TaskControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TaskService taskService;
    @Autowired private UserService userService;
    @Autowired private ObjectMapper objectMapper;

    private final String TEST_EMAIL = "test@example.com";
    private final String OTHER_EMAIL = "hacker@example.com";

    @BeforeEach
    void cleanUp() {
        taskService.deleteAllTasks();
        userService.deleteAllUsers();
    }

    private User prepareTestUser() {
        return userService.getUserByEmail(TEST_EMAIL) != null
                ? userService.getUserByEmail(TEST_EMAIL)
                : userService.createUser(new User(TEST_EMAIL, "Test User", "https://pic.com/test.png"));
    }

    private User prepareOtherUser() {
        return userService.getUserByEmail(OTHER_EMAIL) != null
                ? userService.getUserByEmail(OTHER_EMAIL)
                : userService.createUser(new User(OTHER_EMAIL, "Other User", "https://pic.com/other.png"));
    }

    private Task createMockTaskForUser(User user, String title) {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        request.setCreatedAt(Instant.now());
        return taskService.createTaskForUser(request, user);
    }

    @Test
    void getAllTasks_shouldReturnListOfAllTasks() throws Exception {
        mockMvc.perform(post("/api/tasks/all")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()))
                .andExpect(status().isMethodNotAllowed()) //405
                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                .andExpect(jsonPath("$.message").value("Request method is not supported."));


        mockMvc.perform(get("/api/tasks/all")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllUserTasks_shouldReturnListOfAllUserTasks() throws Exception {
        User testUser = prepareTestUser();
        createMockTaskForUser(testUser, "UserTask Test");

        mockMvc.perform(get("/api/tasks/user")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title").value(Matchers.hasItem("UserTask Test")));
    }

    @Test
    void getAllUserTasks_shouldReturnOnlyTasksOfAuthenticatedUser() throws Exception {
        User testUser = prepareTestUser();
        User otherUser = prepareOtherUser();

        createMockTaskForUser(testUser, "Task of Test User");
        createMockTaskForUser(otherUser, "Task of Other User");

        mockMvc.perform(get("/api/tasks/user")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title").value(Matchers.hasItem("Task of Test User")))
                .andExpect(jsonPath("$[*].title").value(Matchers.not(Matchers.hasItem("Task of Other User"))));
    }

    @Test
    void createTask_shouldSaveToDatabase() throws Exception {
        User testUser = prepareTestUser();

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("title", "Integration Test Task");
        taskData.put("description", "Created from test");
        taskData.put("createdAt", Instant.now().toString());
        taskData.put("important", true);
        taskData.put("urgent", false);
        taskData.put("completed", false);
        taskData.put("orderIndex", 0);

        mockMvc.perform(post("/api/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Task"));

        List<Task> tasks = taskService.getTasksByUserId(testUser.getId());
        assertTrue(tasks.stream().anyMatch(t -> "Integration Test Task".equals(t.getTitle())));
    }

    @Test
    void createTask_shouldReturnBadRequest_whenTitleMissing() throws Exception {
        prepareTestUser();

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("description", "No title");
        taskData.put("createdAt", Instant.now().toString());

        mockMvc.perform(post("/api/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskData)))
                .andExpect(status().isBadRequest())// 400
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed."));
    }

    @Test
    void updateTask_shouldUpdateTask() throws Exception {
        User testUser = prepareTestUser();
        Task task = createMockTaskForUser(testUser, "Original Title");

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("title", "Updated Title");
        updatedData.put("description", "Updated Description");
        updatedData.put("important", true);
        updatedData.put("urgent", true);
        updatedData.put("completed", true);
        updatedData.put("orderIndex", 5);

        mockMvc.perform(put("/api/tasks/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void updateTask_shouldReturnNotFound_whenTaskIdInvalid() throws Exception {
        prepareTestUser();

        Map<String, Object> update = new HashMap<>();
        update.put("title", "Updated");

        mockMvc.perform(put("/api/tasks/invalidId")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                        .andExpect(jsonPath("$.error").value("Not Found"))
                        .andExpect(jsonPath("$.message").value("Task not found."));
    }

    @Test
    void updateTask_shouldReturnForbidden_whenNotOwner() throws Exception {
        User owner = prepareTestUser();
        User otherUser = prepareOtherUser();

        Task task = createMockTaskForUser(owner, "Protected Task");

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("title", "Hacked Title");

        mockMvc.perform(put("/api/tasks/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", otherUser.getEmail())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("You are not allowed to access this resource."));
    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        User testUser = prepareTestUser();
        Task task = createMockTaskForUser(testUser, "Task to Delete");

        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL))))
                .andExpect(status().isNoContent());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(task.getId()));
    }

    @Test
    void deleteTask_shouldReturnNotFound_whenTaskIdInvalid() throws Exception {
        prepareTestUser();

        mockMvc.perform(delete("/api/tasks/invalidId")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", TEST_EMAIL))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Task not found."));
    }

    @Test
    void deleteTask_shouldReturnForbidden_whenNotOwner() throws Exception {
        User owner = prepareTestUser();
        User otherUser = prepareOtherUser();

        Task task = createMockTaskForUser(owner, "Protected Task");

        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> attrs.put("email", OTHER_EMAIL))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("You are not allowed to access this resource."));
    }
}
