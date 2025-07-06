package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;
	
	@InjectMocks
	private TeacherService underTest;
	
	@Test
	void findAllTest() {
		 List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
		  Mockito.when(teacherRepository.findAll()).thenReturn(teachers);
		  
		  List<Teacher> result = underTest.findAll();
		  Mockito.verify(teacherRepository).findAll();
	}
	@Test
	void findByIdTest() {
		Long id = 21L;
		Teacher teacher = new Teacher();
		teacher.setId(id);
		Mockito.when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
		
		Teacher result = underTest.findById(id);
	    Assertions.assertNotNull(result);
	    Assertions.assertEquals(id, result.getId());
	    Assertions.assertEquals(teacher, result);
	    
		Mockito.verify(teacherRepository).findById(id);
	}

}
