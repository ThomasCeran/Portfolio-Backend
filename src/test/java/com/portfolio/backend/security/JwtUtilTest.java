package com.portfolio.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("mysecretkeymysecretkeymysecretkeymysecretkey", 10000); // Ajout du param expirationTime
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testuser");

        assertNotNull(token);
        assertTrue(token.length() > 10); // Vérifie que le token est bien généré
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testuser");
        String username = jwtUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testuser");
        boolean isValid = jwtUtil.validateToken(token, "testuser");

        assertTrue(isValid);
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        // Création d'un utilitaire JWT avec une expiration très courte (1ms)
        JwtUtil jwtUtilWithShortExpiration = new JwtUtil("mysecretkeymysecretkeymysecretkeymysecretkey", 1);

        String expiredToken = jwtUtilWithShortExpiration.generateToken("testuser");

        // Attendre que le token expire
        Thread.sleep(10); 

        boolean isValid = jwtUtilWithShortExpiration.validateToken(expiredToken, "testuser");

        assertFalse(isValid);
    }
}
