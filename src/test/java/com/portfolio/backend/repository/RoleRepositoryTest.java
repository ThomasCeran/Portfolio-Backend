package com.portfolio.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.portfolio.backend.entity.Role;

/**
 * Test class for RoleRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // Prepare test data
        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setName("USER");

        // Save roles
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
    }

    @Test
    void testFindByName() {
        // Test finding role by name
        Optional<Role> role = roleRepository.findByName("ADMIN");
        assertTrue(role.isPresent(), "Role with name 'ADMIN' should exist");
        assertEquals("ADMIN", role.get().getName(), "Role name should match");
    }

    @Test
    void testExistsByName() {
        // Test checking existence of role by name
        boolean exists = roleRepository.existsByName("USER");
        assertTrue(exists, "Role with name 'USER' should exist");
    }

    @Test
    void testBeforeEachSetup() {
        // Check the amount of roles in the database to see if the annotation @BeforeEachSetup works well 
        long roleCount = roleRepository.count();
        assertEquals(2, roleCount, "The database should contain exactly 2 roles after setup");
    }
}