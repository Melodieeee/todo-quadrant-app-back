package com.melody.todoquadrantappback.controller;

import com.melody.todoquadrantappback.model.User;
import com.melody.todoquadrantappback.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserService userService;

    @BeforeEach
    void setup() {
        userService.deleteAllUsers();
        // Create a test user
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPicture("https://pic.com/test.png");
        userService.createUser(testUser);
    }

    @AfterEach
    void tearDown() {
        userService.deleteAllUsers();
    }

    @Test
    void getCurrentUser_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/user/info"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("User not authenticated."));
        ;
    }

    @Test
    void getCurrentUser_shouldReturnUserInfo_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/user/info")
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                .attributes(attrs -> {
                                    attrs.put("name", "Test User");
                                    attrs.put("email", "test@example.com");
                                    attrs.put("picture", "https://pic.com/test.png");
                                })))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.picture").value("https://pic.com/test.png"));
    }

    @Test
    void logout_shouldInvalidateSession() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/user/logout")
                        .sessionAttr("someAttr", "someValue")
                        .with(oauth2Login()))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNull(session, "Session should be clear after logout");
    }

}