package com.example.demo;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_FULL_NAME = "Test User";

    @BeforeEach
    public void setup() {
        // Clean up before each test
        userRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        // Clean up after each test
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.fullName").value(TEST_FULL_NAME))
                .andReturn();

        // Verify the response
        String responseContent = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(responseContent, AuthenticationResponse.class);
        
        assertNotNull(response.token());
        assertEquals(TEST_USERNAME, response.username());
        assertEquals(TEST_EMAIL, response.email());
        assertEquals(TEST_FULL_NAME, response.fullName());

        // Verify the user was saved in the database
        assertTrue(userRepository.findByUsername(TEST_USERNAME).isPresent());
        var user = userRepository.findByUsername(TEST_USERNAME).get();
        assertEquals(TEST_EMAIL, user.getEmail());
        assertEquals(TEST_FULL_NAME, user.getFullName());
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, user.getPassword()));
        assertTrue(user.getRoles().contains(UserRole.ROLE_USER));
    }

    @Test
    public void testRegisterUser_DuplicateUsername() throws Exception {
        // First registration
        RegisterRequest registerRequest1 = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isOk());

        // Second registration with same username
        RegisterRequest registerRequest2 = new RegisterRequest(
                TEST_USERNAME,
                "another@example.com",
                "Another User",
                "anotherpassword",
                "0987654321"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username is already taken"));
    }

    @Test
    public void testRegisterUser_DuplicateEmail() throws Exception {
        // First registration
        RegisterRequest registerRequest1 = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isOk());

        // Second registration with same email
        RegisterRequest registerRequest2 = new RegisterRequest(
                "anotheruser",
                TEST_EMAIL,
                "Another User",
                "anotherpassword",
                "0987654321"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is already in use"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        // First register a user
        RegisterRequest registerRequest = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Now try to login
        AuthenticationRequest loginRequest = new AuthenticationRequest(TEST_USERNAME, TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.fullName").value(TEST_FULL_NAME))
                .andReturn();

        // Verify the response
        String responseContent = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(responseContent, AuthenticationResponse.class);
        
        assertNotNull(response.token());
        assertEquals(TEST_USERNAME, response.username());
        assertEquals(TEST_EMAIL, response.email());
        assertEquals(TEST_FULL_NAME, response.fullName());
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        // First register a user
        RegisterRequest registerRequest = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Try to login with wrong password
        AuthenticationRequest loginRequest = new AuthenticationRequest(TEST_USERNAME, "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    public void testAccessProtectedEndpoint_WithValidToken() throws Exception {
        // First register and login to get a token
        RegisterRequest registerRequest = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String registerResponse = registerResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(registerResponse, AuthenticationResponse.class);
        String token = authResponse.token();

        // Try to access a protected endpoint with the token
        mockMvc.perform(post("/api/test/private")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a private endpoint"));
    }

    @Test
    public void testAccessProtectedEndpoint_WithoutToken() throws Exception {
        // Try to access a protected endpoint without a token
        mockMvc.perform(post("/api/test/private"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAccessAdminEndpoint_AsUser() throws Exception {
        // First register and login to get a token (regular user)
        RegisterRequest registerRequest = new RegisterRequest(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_FULL_NAME,
                TEST_PASSWORD,
                "1234567890"
        );

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String registerResponse = registerResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(registerResponse, AuthenticationResponse.class);
        String token = authResponse.token();

        // Try to access admin endpoint as regular user
        mockMvc.perform(post("/api/test/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
