package com.portfolio.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.backend.entity.Role;

/**
 * Repository interface for managing Role entity operations.
 * This interface extends JpaRepository, providing CRUD functionality and
 * additional custom queries.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role.
     * @return an Optional containing the role if found, or empty otherwise.
     */
    Optional<Role> findByName(String name);

    /**
     * Checks if a role exists with the given name.
     *
     * @param name the name of the role to check.
     * @return true if a role exists with the given name, false otherwise.
     */
    boolean existsByName(String name);
}
