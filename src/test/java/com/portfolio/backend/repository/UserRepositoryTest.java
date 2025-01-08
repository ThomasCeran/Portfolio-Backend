package com.portfolio.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.portfolio.backend.entity.Role;
import com.portfolio.backend.entity.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // Create roles
        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setName("USER");

        // Save roles first
        adminRole = roleRepository.save(adminRole);
        userRole = roleRepository.save(userRole);

        // Create users
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("adminpass");
        adminUser.setRole(adminRole); // Reference the persisted role
        adminUser.setCreatedAt(LocalDateTime.now());

        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setEmail("user@example.com");
        regularUser.setPassword("userpass");
        regularUser.setRole(userRole); // Reference the persisted role
        regularUser.setCreatedAt(LocalDateTime.now());

        // Save users
        userRepository.save(adminUser);
        userRepository.save(regularUser);
    }

    @Test
    void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("admin@example.com");
        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), "admin");
    }

    @Test
    void testExistsByEmail() {
        boolean exists = userRepository.existsByEmail("user@example.com");
        assertTrue(exists);
    }

    @Test
    void testFindByUsername() {
        Optional<User> user = userRepository.findByUsername("user");
        assertTrue(user.isPresent());
        assertEquals(user.get().getEmail(), "user@example.com");
    }

    @Test
    void testFindByRoleName() {
        List<User> admins = userRepository.findByRoleName("ADMIN");
        assertEquals(admins.size(), 1);
        assertEquals(admins.get(0).getUsername(), "admin");
    }

    @Test
    void testFindByEmailAndRoleName() {
        Optional<User> user = userRepository.findByEmailAndRoleName("admin@example.com", "ADMIN");
        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), "admin");
    }

    @Test
    void testFindByRoleNameOrderByCreatedAtDesc() {
        List<User> users = userRepository.findByRoleNameOrderByCreatedAtDesc("USER");
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getUsername(), "user");
    }
}
