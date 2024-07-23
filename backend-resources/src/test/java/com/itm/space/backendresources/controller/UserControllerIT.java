package com.itm.space.backendresources.controller;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class UserControllerIT extends BaseIntegrationTest {

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void createUserWithModerator() throws Exception {
        UserRequest userRequest = new UserRequest("test", "test@mail.com", "password", "TestName", "TestLastname");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void createUserInvalidUsername() throws Exception {
        UserRequest userRequest = new UserRequest("1", "test@mail.com", "password", "TestName", "TestLastname");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void createWithoutPassword() throws Exception {
        UserRequest userRequest = new UserRequest("test", "test@mail.com", "", "TestName", "TestLastname");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void createWithoutEmail() throws Exception {
        UserRequest userRequest = new UserRequest("test", "", "password", "TestName", "TestLastname");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void createEmptyUser() throws Exception {
        UserRequest userRequest = new UserRequest("", "", "", "", "");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void createWithNoAuthentication() throws Exception {
        UserRequest userRequest = new UserRequest("test", "test@mail.com", "password", "TestName", "TestLastname");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createTestWithUser() throws Exception {
        UserRequest userRequest = new UserRequest("test", "test@mail.com", "password", "TestName", "TestLastname");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void getExistingUserByIdTestWithModerator() throws Exception {
        UUID existingUserId = UUID.fromString("5579a21c-8a14-48d6-9564-a55613e82c0f");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/users/{id}", existingUserId);
        mvc.perform(requestToJson(request))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void getExistingUserByIdTestWithUser() throws Exception {
        UUID existingUserId = UUID.fromString("5579a21c-8a14-48d6-9564-a55613e82c0f");
        mvc.perform(MockMvcRequestBuilders.get("/api/users/" + existingUserId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void helloUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/users/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void helloModerator() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/users/hello"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/nonexistent"))
                .andExpect(status().isNotFound());
    }
}