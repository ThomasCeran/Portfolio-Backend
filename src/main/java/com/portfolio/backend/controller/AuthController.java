package com.portfolio.backend.controller;

import com.portfolio.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for handling secure authentication and JWT token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * Constructs the AuthController with authentication manager and JWT utility.
     *
     * @param authenticationManager Spring Security authentication manager
     * @param jwtUtil               utility for JWT token operations
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates a user and returns a JWT token if authentication is successful.
     * <p>
     *
     * @param loginRequest the login credentials
     * @return a JWT token if authentication succeeds, error otherwise
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
            // Authentication successful, generate JWT
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException ex) {
            // Invalid credentials
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
    }

    /**
     * DTO for authentication requests.
     */
    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
