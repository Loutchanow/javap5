package com.openclassrooms.starterjwt.services;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;

import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.repository.SessionRepository;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
	
	@Mock
	private SessionRepository sessionRepository;
	
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
	
}
