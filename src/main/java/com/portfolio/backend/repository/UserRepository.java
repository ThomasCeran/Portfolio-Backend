package com.portfolio.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portfolio.backend.entity.User;

/**
 * Repository interface for managing User entity operations.
 * This interface extends JpaRepository, providing CRUD functionality and
 * additional custom queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check.
     * @return true if a user exists with the given email, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check.
     * @return true if a user exists with the given username, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Finds all users associated with a specific role name.
     *
     * @param roleName the name of the role.
     * @return a list of users with the given role name.
     */
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    /**
     * Finds all users created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of users created after the given date.
     */
    List<User> findByCreatedAtAfter(LocalDateTime createdAt);

    /**
     * Finds a user by their email and role name.
     *
     * @param email    the email of the user.
     * @param roleName the name of the role.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.role.name = :roleName")
    Optional<User> findByEmailAndRoleName(@Param("email") String email, @Param("roleName") String roleName);

    /**
     * Finds all users with a specific role name, ordered by creation date in
     * descending order.
     *
     * @param roleName the name of the role.
     * @return a list of users with the given role name, ordered by creation date.
     */
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName ORDER BY u.createdAt DESC")
    List<User> findByRoleNameOrderByCreatedAtDesc(@Param("roleName") String roleName);

    /**
     * Finds all contact messages associated with a specific user.
     * This method uses a custom JPQL query to fetch related messages.
     *
     * @param userId the ID of the user.
     * @return a list of contact messages associated with the user.
     */
    @Query("SELECT cm FROM ContactMessage cm WHERE cm.user.id = :userId")
    List<Object> findContactMessagesByUserId(@Param("userId") Long userId);
}
