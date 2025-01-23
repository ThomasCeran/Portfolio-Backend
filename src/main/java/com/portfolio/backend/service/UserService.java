package com.portfolio.backend.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.backend.entity.User;
import com.portfolio.backend.repository.UserRepository;

/**
 * Service class for managing users. Encapsulates business logic and database
 * operations related to users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository the UserRepository instance to interact with the database.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users.
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check.
     * @return true if the user exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username to check.
     * @return true if the user exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Finds all users associated with a specific role name.
     *
     * @param roleName the name of the role.
     * @return a list of users with the given role name.
     */
    @Transactional(readOnly = true)
    public List<User> findUsersByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    /**
     * Finds all users created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of users created after the given date.
     */
    @Transactional(readOnly = true)
    public List<User> findUsersCreatedAfter(LocalDateTime createdAt) {
        return userRepository.findByCreatedAtAfter(createdAt);
    }

    /**
     * Finds a user by their email and role name.
     *
     * @param email    the email of the user.
     * @param roleName the name of the role.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmailAndRoleName(String email, String roleName) {
        return userRepository.findByEmailAndRoleName(email, roleName);
    }

    /**
     * Finds all users with a specific role name, ordered by creation date in descending order.
     *
     * @param roleName the name of the role.
     * @return a list of users with the given role name, ordered by creation date.
     */
    @Transactional(readOnly = true)
    public List<User> findUsersByRoleNameOrderedByCreatedAtDesc(String roleName) {
        return userRepository.findByRoleNameOrderByCreatedAtDesc(roleName);
    }

    /**
     * Creates or updates a user in the database.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     */
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
