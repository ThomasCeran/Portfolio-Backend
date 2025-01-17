package com.portfolio.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.backend.entity.Role;
import com.portfolio.backend.repository.RoleRepository;



/**
 * Service class for managing Role entity operations.
 * This class encapsulates business logic and interactions with RoleRepository.
 */
@Service
public class RoleService {
    
    @Autowired
    private final RoleRepository roleRepository;

    
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves all roles from the database.
     *
     * @return a list of all roles.
     */
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Retrieves a role by its ID.
     *
     * @param id the ID of the role to retrieve.
     * @return the found Role.
     * @throws IllegalArgumentException if no role is found with the given ID.
     */
    @Transactional(readOnly = true)
    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role with ID " + id + " not found."));
    }

    /**
     * Retrieves a role by its name.
     *
     * @param name the name of the role to retrieve.
     * @return the found Role.
     * @throws IllegalArgumentException if no role is found with the given name.
     */
    @Transactional(readOnly = true)
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Role with name '" + name + "' not found."));
    }

    /**
     * Creates a new role in the database.
     *
     * @param role the Role to create.
     * @return the created Role.
     * @throws IllegalArgumentException if a role with the same name already exists.
     */
    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role with name '" + role.getName() + "' already exists.");
        }
        return roleRepository.save(role);
    }

    /**
     * Updates an existing role in the database.
     *
     * @param id   the ID of the role to update.
     * @param role the updated role data.
     * @return the updated Role.
     * @throws IllegalArgumentException if no role is found with the given ID.
     */
    @Transactional
    public Role updateRole(Long id, Role role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role with ID " + id + " not found."));
        existingRole.setName(role.getName());
        return roleRepository.save(existingRole);
    }

    /**
     * Deletes a role by its ID.
     *
     * @param id the ID of the role to delete.
     * @throws IllegalArgumentException if no role is found with the given ID.
     */
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role with ID " + id + " not found.");
        }
        roleRepository.deleteById(id);
    }
}
