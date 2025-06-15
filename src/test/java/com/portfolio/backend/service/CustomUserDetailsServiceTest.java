package com.portfolio.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.portfolio.backend.entity.Role;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.repository.UserRepository;

/**
 * Unit tests for CustomUserDetailsService.
 */
@SpringBootTest
public class CustomUserDetailsServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Tests that loadUserByUsername returns the correct UserDetails
     * when the user exists in the database.
     */
    @Test
    public void testLoadUserByUsername_UserExists() {
        // Arrange: Prepare a dummy user with the ADMIN role.
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("password");
        user.setRole(adminRole);

        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        // Act: Call the service to load user details.
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // Assert: Check that the username and authorities are correct.
        assertEquals("admin", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    /**
     * Tests that loadUserByUsername throws an exception
     * when the user is not found in the database.
     */
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange: No user found for the username.
        Mockito.when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert: The service should throw UsernameNotFoundException.
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
    }
}