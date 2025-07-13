package com.openclassrooms.starterjwt.controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.User;

import com.openclassrooms.starterjwt.repository.UserRepository;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testRegister_shouldSucceed() throws Exception {
        String json = "{\n" +
                "  \"email\": \"newuser@example.com\",\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"password\": \"securePassword\"\n" +
                "}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void testRegister_shouldFail_whenEmailExists() throws Exception {
        User user = new User("exist@example.com", "Doo", "Mike", "encodedpass", false);
        userRepository.save(user);

        String json = "{\n" +
                "  \"email\": \"exist@example.com\",\n" +
                "  \"firstName\": \"Mike\",\n" +
                "  \"lastName\": \"Doo\",\n" +
                "  \"password\": \"password\"\n" +
                "}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }
    @Test
    void testLogin_shouldReturnJwt() throws Exception {
        String rawPassword = "testpass";
        User user = new User("loginuser@example.com", "Doe", "John", passwordEncoder.encode(rawPassword), false);
        userRepository.save(user);

        String json = "{\n" +
                "  \"email\": \"loginuser@example.com\",\n" +
                "  \"password\": \"testpass\"\n" +
                "}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("loginuser@example.com"));
    }
}
