package com.portfolio.backend.service;


import com.portfolio.backend.entity.Role;
import com.portfolio.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the RoleService class.
 */
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllRoles() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        // Act
        List<Role> roles = roleService.findAllRoles();

        // Assert
        assertNotNull(roles);
        assertEquals(2, roles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testFindRoleById() {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Act
        Role foundRole = roleService.findRoleById(1L);

        // Assert
        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindRoleById_NotFound() {
        // Arrange
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.findRoleById(1L));
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindRoleByName() {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));

        // Act
        Role foundRole = roleService.findRoleByName("ADMIN");

        // Assert
        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    void testCreateRole() {
        // Arrange
        Role role = new Role();
        role.setName("USER");

        when(roleRepository.existsByName("USER")).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role createdRole = roleService.createRole(role);

        // Assert
        assertNotNull(createdRole);
        assertEquals("USER", createdRole.getName());
        verify(roleRepository, times(1)).existsByName("USER");
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testCreateRole_DuplicateName() {
        // Arrange
        Role role = new Role();
        role.setName("USER");

        when(roleRepository.existsByName("USER")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(role));
        verify(roleRepository, times(1)).existsByName("USER");
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testUpdateRole() {
        // Arrange
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("USER");

        Role updatedRole = new Role();
        updatedRole.setName("ADMIN");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(existingRole)).thenReturn(existingRole);

        // Act
        Role result = roleService.updateRole(1L, updatedRole);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(existingRole);
    }

    @Test
    void testDeleteRole() {
        // Arrange
        when(roleRepository.existsById(1L)).thenReturn(true);

        // Act
        roleService.deleteRole(1L);

        // Assert
        verify(roleRepository, times(1)).existsById(1L);
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRole_NotFound() {
        // Arrange
        when(roleRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.deleteRole(1L));
        verify(roleRepository, times(1)).existsById(1L);
        verify(roleRepository, never()).deleteById(1L);
    }
}
