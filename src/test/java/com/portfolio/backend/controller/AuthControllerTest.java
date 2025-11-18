package com.portfolio.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.portfolio.backend.dto.AuthRequest;
import com.portfolio.backend.dto.AuthResponse;
import com.portfolio.backend.security.JwtUtil;
import com.portfolio.backend.security.TokenBlacklistService;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, jwtUtil, tokenBlacklistService);
    }

    @Test
    void login_returnsToken_whenCredentialsAreValid() {
        AuthRequest request = new AuthRequest();
        request.setEmail("admin@example.com");
        request.setPassword("secret");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new User("admin@example.com".toLowerCase(Locale.ROOT), "secret",
                        java.util.List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))),
                null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateToken(eq("admin@example.com"), anyMap())).thenReturn("jwt-token");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        AuthResponse body = (AuthResponse) response.getBody();
        assertEquals("jwt-token", body.getToken());
        verify(jwtUtil, times(1)).generateToken(eq("admin@example.com"), anyMap());
    }

    @Test
    void login_returns401_whenAuthenticationFails() {
        AuthRequest request = new AuthRequest();
        request.setEmail("admin@example.com");
        request.setPassword("bad");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(401, response.getStatusCode().value());
        verify(jwtUtil, never()).generateToken(any(), anyMap());
    }

    @Test
    void logout_revokesToken_whenHeaderPresent() {
        org.springframework.mock.web.MockHttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        when(jwtUtil.isTokenExpired("token")).thenReturn(false);
        when(jwtUtil.extractExpirationInstant("token")).thenReturn(Instant.now().plusSeconds(60));

        ResponseEntity<Void> response = authController.logout(request);

        assertEquals(204, response.getStatusCode().value());
        verify(tokenBlacklistService, times(1)).revoke(eq("token"), any(Instant.class));
    }
}
