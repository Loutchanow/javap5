package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.openclassrooms.starterjwt.models.User;

import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	 private UserService underTest;

	@Test
	void deleteTest() {
		Long id = 21L;
	    Mockito.doNothing().when(userRepository).deleteById(Mockito.any());

	    underTest.delete(id);

	    Mockito.verify(userRepository).deleteById(Mockito.eq(id));
	}
	
	@Test
	void findByIdTest() {
		Long id = 21L;
		User user = new User();
		user.setId(id);
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
		
	    User result = underTest.findById(id);
	    
	    Assertions.assertNotNull(result);
	    Assertions.assertEquals(id, result.getId());
	    Assertions.assertEquals(user, result);
	    Mockito.verify(userRepository).findById(id);
	    

	}

}
