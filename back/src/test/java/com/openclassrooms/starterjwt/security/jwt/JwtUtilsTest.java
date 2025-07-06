package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.Date;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;


@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000); 
    }

    @Test
    void generate_and_validate_token_shouldWorkCorrectly() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L,"testuser","Test","User",false,"password");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(auth);

        assertNotNull(token);
        assertEquals("testuser", jwtUtils.getUserNameFromJwtToken(token));
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenInvalidToken() {
        String invalidToken = "invalid.jwt.token";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
    
    @Test
    void validateJwtToken_shouldReturnFalse_whenSignatureInvalid() {
        String invalidToken = Jwts.builder()
            .setSubject("user")
            .signWith(SignatureAlgorithm.HS512, "wrongSecret")
            .compact();

            ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "actualSecret");

        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
    
    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenMalformed() {
        String malformedToken = "not.a.jwt.token";

        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }
    
    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenExpired() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 10000); 

        String expiredToken = Jwts.builder()
            .setSubject("user")
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS512, "testSecret")
            .compact();

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }
    
    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsEmpty() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }


}