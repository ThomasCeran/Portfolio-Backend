package com.portfolio.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.backend.entity.User;
import com.portfolio.backend.service.UserService;

/**
 * REST controller for managing User-related operations.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve.
     * @return the user if found, or 404 Not Found otherwise.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve.
     * @return the user if found, or 404 Not Found otherwise.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all users with a specific role name.
     *
     * @param roleName the role name to filter users by.
     * @return a list of users with the specified role name.
     */
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<User>> getUsersByRoleName(@PathVariable String roleName) {
        List<User> users = userService.findUsersByRoleName(roleName);
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves all users created after a specific date.
     *
     * @param date the cutoff creation date.
     * @return a list of users created after the given date.
     */
    @GetMapping("/created-after/{date}")
    public ResponseEntity<List<User>> getUsersCreatedAfter(@PathVariable LocalDateTime date) {
        List<User> users = userService.findUsersCreatedAfter(date);
        return ResponseEntity.ok(users);
    }

    /**
     * Creates or updates a user.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     * @return a 204 No Content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
