package com.portfolio.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.ResponseEntity;

import com.portfolio.backend.entity.User;
import com.portfolio.backend.service.UserService;

/**
 * Unit tests for the UserController class.
 */
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).findUserByEmail("test@example.com");
    }

    @Test
    void testGetUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userService.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        verify(userService, times(1)).findUserByUsername("testuser");
    }

    @Test
    void testGetUsersByRoleName() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findUsersByRoleName("ADMIN")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsersByRoleName("ADMIN");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findUsersByRoleName("ADMIN");
    }

    @Test
    void testGetUsersCreatedAfter() {
        LocalDateTime date = LocalDateTime.now();
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findUsersCreatedAfter(date)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsersCreatedAfter(date);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findUsersCreatedAfter(date);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.saveUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testDeleteUserById() {
        Long userId = 1L;

        ResponseEntity<Void> response = userController.deleteUserById(userId);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUserById(userId);
    }
}
