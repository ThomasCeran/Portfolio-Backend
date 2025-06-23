package com.portfolio.backend.controller;

import com.portfolio.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
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
                            loginRequest.getEmail().toLowerCase(Locale.ROOT),
                            loginRequest.getPassword()));
            // Get the user role
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication
                    .getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");
            // Generate a JWT with a role as claim
            Map<String, Object> claims = Map.of("role", role);
            String token = jwtUtil.generateToken(loginRequest.getEmail(), claims);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException ex) {
            // Invalid credentials
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
    }

    /**
     * DTO for authentication requests.
     */
    public static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
