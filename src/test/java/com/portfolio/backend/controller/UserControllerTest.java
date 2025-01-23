package com.portfolio.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Should return all users successfully")
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    @DisplayName("Should return user by email successfully")
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).findUserByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return user by username successfully")
    void testGetUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userService.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        verify(userService, times(1)).findUserByUsername("testuser");
    }

    @Test
    @DisplayName("Should return users by role name successfully")
    void testGetUsersByRoleName() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findUsersByRoleName("ADMIN")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsersByRoleName("ADMIN");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findUsersByRoleName("ADMIN");
    }

    @Test
    @DisplayName("Should return users created after a specific date")
    void testGetUsersCreatedAfter() {
        LocalDateTime date = LocalDateTime.now();
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findUsersCreatedAfter(date)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsersCreatedAfter(date);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findUsersCreatedAfter(date);
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUser() {
        User user = new User();
        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.saveUser(user);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    @DisplayName("Should delete user by ID successfully")
    void testDeleteUserById() {
        Long userId = 1L;

        ResponseEntity<Void> response = userController.deleteUserById(userId);

        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    @DisplayName("Should handle case when user by email is not found")
    void testGetUserByEmail_NotFound() {
        when(userService.findUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByEmail("notfound@example.com");

        assertEquals(404, response.getStatusCode().value());
        verify(userService, times(1)).findUserByEmail("notfound@example.com");
    }

    @Test
    @DisplayName("Should handle exception during user deletion")
    void testDeleteUserById_Exception() {
        Long userId = 999L;
        doThrow(new RuntimeException("User not found")).when(userService).deleteUserById(userId);

        Exception exception = assertThrows(RuntimeException.class, () -> userController.deleteUserById(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).deleteUserById(userId);
    }

}