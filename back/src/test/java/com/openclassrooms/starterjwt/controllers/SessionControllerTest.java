package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Arrays;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private UserRepository userRepository;



    private Session session;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
        
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);
        
        user = new User();
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("securepassword");
        user.setAdmin(false);
        user = userRepository.save(user);
       
        session = new Session();
        session.setName("Test Session");
        session.setDescription("Description de test");
        session.setDate(new Date()); 
        session.setTeacher(teacher);
        
        Session session1 = new Session();
        session1.setName("Session 1");
        session1.setDescription("Description 1");
        session1.setDate(new Date());
        session1.setTeacher(teacher);

        Session session2 = new Session();
        session2.setName("Session 2");
        session2.setDescription("Description 2");
        session2.setDate(new Date());
        session2.setTeacher(teacher);
        sessionRepository.saveAll(Arrays.asList(session, session1, session2));
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnSession_whenIdExists() throws Exception {
    	
        mockMvc.perform(get("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(session.getId()))
                .andExpect(jsonPath("$.name").value("Test Session"));
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    void testFindAll_shouldReturnListOfSessions() throws Exception {
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[1].name").value("Session 1"))
                .andExpect(jsonPath("$[2].name").value("Session 2"));
    }
    @Test
    @WithMockUser
    void testCreateSession_shouldReturnCreatedSession() throws Exception {
    	String json = "{ \"name\": \"Nouvelle Session\", \"date\": \"2012-01-01\", \"teacher_id\": 5, \"users\": null, \"description\": \"Test de création\" }";

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nouvelle Session"))
                .andExpect(jsonPath("$.description").value("Test de création"));
    }
    
    @Test
    @WithMockUser
    void testUpdateSession_shouldReturnUpdatedSession() throws Exception {
        String updateJson = "{ \"name\": \"Nom mis à jour\", \"date\": \"2012-01-01\", \"teacher_id\": 5, \"users\": null, \"description\": \"Description mise à jour\" }";

        mockMvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nom mis à jour"))
                .andExpect(jsonPath("$.description").value("Description mise à jour"));
    }
    
    @Test
    @WithMockUser
    void testDeleteSession_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDeleteSession_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDeleteSession_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser
    void testParticipate_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testParticipate_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(post("/api/session/abc/participate/def"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    void testNoLongerParticipate_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void testNoLongerParticipate_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/abc/participate/def"))
                .andExpect(status().isBadRequest());
    }


}
