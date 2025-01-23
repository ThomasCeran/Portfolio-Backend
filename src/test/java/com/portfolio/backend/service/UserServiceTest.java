package com.portfolio.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.portfolio.backend.entity.User;
import com.portfolio.backend.repository.UserRepository;

/**
 * Unit tests for the UserService class.
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testUserExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userService.userExistsByEmail("test@example.com");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testUserExistsByUsername() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        boolean exists = userService.userExistsByUsername("testuser");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername("testuser");
    }

    @Test
    void testFindUsersByRoleName() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByRoleName("ADMIN")).thenReturn(users);

        List<User> result = userService.findUsersByRoleName("ADMIN");

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByRoleName("ADMIN");
    }

    @Test
    void testFindUsersCreatedAfter() {
        LocalDateTime date = LocalDateTime.now();
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByCreatedAtAfter(date)).thenReturn(users);

        List<User> result = userService.findUsersCreatedAfter(date);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByCreatedAtAfter(date);
    }

    @Test
    void testFindUserByEmailAndRoleName() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmailAndRoleName("test@example.com", "ADMIN"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByEmailAndRoleName("test@example.com", "ADMIN");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmailAndRoleName("test@example.com", "ADMIN");
    }

    @Test
    void testFindUsersByRoleNameOrderedByCreatedAtDesc() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByRoleNameOrderByCreatedAtDesc("ADMIN")).thenReturn(users);

        List<User> result = userService.findUsersByRoleNameOrderedByCreatedAtDesc("ADMIN");

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByRoleNameOrderByCreatedAtDesc("ADMIN");
    }

    @Test
    void testSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUserById() {
        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
