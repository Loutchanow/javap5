package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    


    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(true);
        user.setEmail("email@g.com");
        user.setPassword("pwd");
 
        User user1 = new User();
        user1.setFirstName("Mike");
        user1.setLastName("Doo");
        user.setAdmin(true);
        user.setEmail("email@g.com");
        user.setPassword("pwd");

        userRepository.saveAll(Arrays.asList(user, user1));
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnUser_whenIdExists() throws Exception {
    	 
        mockMvc.perform(get("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value("John"));
    }
    @Test
    @WithMockUser
    void testFindById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/user/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/user/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "testuser@example.com") 
    void testDeleteUser_shouldReturnOk_whenUserIsOwner() throws Exception {

        User user = new User();
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "test2@example.com")
    void testDeleteUser_shouldReturnUnauthorized_whenUserIsNotOwner() throws Exception {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(username = "test3@example.com")
    void testDeleteUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/999999"))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser
    void testDeleteUser_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/user/invalid"))
                .andExpect(status().isBadRequest());
    }


}