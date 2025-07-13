package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private TeacherRepository teacherRepository;
    


    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
 
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Mike");
        teacher1.setLastName("Doo");

        teacherRepository.saveAll(Arrays.asList(teacher, teacher1));
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnTeacher_whenIdExists() throws Exception {
    	
        mockMvc.perform(get("/api/teacher/" + teacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacher.getId()))
                .andExpect(jsonPath("$.firstName").value("John"));
    }
    @Test
    @WithMockUser
    void testFindById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/teacher/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFindById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/teacher/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    void testFindAll_shouldReturnListOfTeachers() throws Exception {
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Mike"));
    }
}