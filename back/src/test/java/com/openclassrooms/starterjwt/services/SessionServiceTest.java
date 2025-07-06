package com.openclassrooms.starterjwt.services;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
	
	@Mock
	private SessionRepository sessionRepository;
	
	@Mock
	private UserRepository userRepository;
	
	
	@InjectMocks
	private SessionService underTest;
	
	@Test
	void createTest() {
		Session session = new Session();
		Mockito.doReturn(session).when(sessionRepository).save(Mockito.any(Session.class));
		
		Session result = underTest.create(session);
		
		Assertions.assertEquals(session, result);
		Mockito.verify(sessionRepository).save(Mockito.eq(session));
	}
	
	@Test
	void deleteTest() {
	    final Long id = 21L;
	    Mockito.doNothing().when(sessionRepository).deleteById(Mockito.any());

	    underTest.delete(id);

	    Mockito.verify(sessionRepository).deleteById(Mockito.eq(id));
	}
	
	@Test
	void findAllTest() {
	    List<Session> sessions = Arrays.asList(new Session(), new Session());
	    Mockito.when(sessionRepository.findAll()).thenReturn(sessions);

	    List<Session> result = underTest.findAll();

	    Assertions.assertEquals(sessions, result);
	    Mockito.verify(sessionRepository).findAll();
	}
	
	@Test 
	void getByIdTest() {
	    Long id = 1L;
	    Session session = new Session();
	    session.setId(id);
	    Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

	    Session result = underTest.getById(id);

	    Assertions.assertNotNull(result);
	    Assertions.assertEquals(id, result.getId());
	    Assertions.assertEquals(session, result);
	    Mockito.verify(sessionRepository).findById(id);
	}
	
	@Test
	void getById_NotFoundTest() {
	    Long id = 99L;
	    Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.empty());

	    Session result = underTest.getById(id);
 
	    Assertions.assertNull(result);
	    Mockito.verify(sessionRepository).findById(id);
	}
	
	@Test
	void updateTest() {
	    Long id = 5L;
	    Session session = new Session();
	    session.setName("Updated Name");
	    
	    Session returnedSession = new Session();
	    returnedSession.setId(id);
	    returnedSession.setName("Updated Name");

	    Mockito.when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(returnedSession);

	    Session result = underTest.update(id, session);

	    Assertions.assertNotNull(result);
	    Assertions.assertEquals(id, result.getId());
	    Assertions.assertEquals("Updated Name", result.getName());
	    Mockito.verify(sessionRepository).save(session);
	}
	
	@Test
	void participate() {
	    Long sessionId = 1L;
	    Long userId = 2L;
	    Session session = new Session();
	    session.setId(sessionId);
	    session.setUsers(new ArrayList<>());
	    User user = new User();
	    user.setId(userId);

	    Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
	    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	    Mockito.when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);

	    underTest.participate(sessionId, userId);

	    Assertions.assertTrue(session.getUsers().contains(user));
	    Mockito.verify(sessionRepository).save(session);
	}
	
	@Test
	void participate_shouldThrowNotFound_whenSessionOrUserMissing() {
	    Long sessionId = 1L;
	    Long userId = 2L;
	    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
	    Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

	    Assertions.assertThrows(NotFoundException.class, () -> {
	        underTest.participate(sessionId, userId);
	    });
	}
	
	@Test
	void participate_shouldThrowBadRequest_whenUserAlreadyParticipates() {
	    Long sessionId = 1L;
	    Long userId = 2L;
	    User user = new User();
	    user.setId(userId);

	    Session session = new Session();
	    session.setId(sessionId);
	    session.setUsers(new ArrayList<>(Arrays.asList(user)));

	    Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
	    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

	    Assertions.assertThrows(BadRequestException.class, () -> {
	        underTest.participate(sessionId, userId);
	    });
	}
	
	@Test
	void noLongerParticipate() {
	    Long sessionId = 1L;
	    Long userId = 2L;

	    User user = new User();
	    user.setId(userId);

	    Session session = new Session();
	    session.setId(sessionId);
	    session.setUsers(new ArrayList<>(Arrays.asList(user)));

	    Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
	    Mockito.when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);

	    underTest.noLongerParticipate(sessionId, userId);

	    Assertions.assertFalse(session.getUsers().stream().anyMatch(u -> u.getId().equals(userId)));
	    Mockito.verify(sessionRepository).save(session);
	}
	
	@Test
	void noLongerParticipate_shouldThrowNotFound_whenSessionMissing() {
	    Long sessionId = 1L;
	    Long userId = 2L;

	    Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

	    Assertions.assertThrows(NotFoundException.class, () -> {
	        underTest.noLongerParticipate(sessionId, userId);
	    });
	}
	@Test
	void noLongerParticipate_shouldThrowBadRequest_whenUserNotParticipating() {
	    Long sessionId = 1L;
	    Long userId = 2L;

	    Session session = new Session();
	    session.setId(sessionId);
	    session.setUsers(new ArrayList<>()); 

	    Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
 
	    Assertions.assertThrows(BadRequestException.class, () -> {
	        underTest.noLongerParticipate(sessionId, userId);
	    });
	}
}
