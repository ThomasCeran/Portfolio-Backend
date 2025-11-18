package com.portfolio.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.portfolio.backend.entity.Role;
import com.portfolio.backend.service.RoleService;

/**
 * Unit tests for the RoleController class.
 */
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    void testGetAllRoles() throws Exception {
        // Arrange
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");

        List<Role> roles = Arrays.asList(role1, role2);

        when(roleService.findAllRoles()).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("ADMIN"))
                .andExpect(jsonPath("$[1].name").value("USER"));

        verify(roleService, times(1)).findAllRoles();
    }

    @Test
    void testGetRoleById() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        when(roleService.findRoleById(1L)).thenReturn(role);

        // Act & Assert
        mockMvc.perform(get("/api/admin/roles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("ADMIN"));

        verify(roleService, times(1)).findRoleById(1L);
    }

    @Test
    void testCreateRole() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        when(roleService.createRole(any(Role.class))).thenReturn(role);

        // Act & Assert
        mockMvc.perform(post("/api/admin/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("USER"));

        verify(roleService, times(1)).createRole(any(Role.class));
    }

    @Test
    void testUpdateRole() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        when(roleService.updateRole(eq(1L), any(Role.class))).thenReturn(role);

        // Act & Assert
        mockMvc.perform(put("/api/admin/roles/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("ADMIN"));

        verify(roleService, times(1)).updateRole(eq(1L), any(Role.class));
    }

    @Test
    void testDeleteRole() throws Exception {
        // Arrange
        doNothing().when(roleService).deleteRole(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/roles/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(roleService, times(1)).deleteRole(1L);
    }
}
