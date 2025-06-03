package com.portfolio.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.backend.dto.ContactMessageRequest;
import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.ContactMessageService;
import com.portfolio.backend.repository.UserRepository;
import com.portfolio.backend.security.JwtUtil;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PublicMessageController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})

class PublicMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ContactMessageService contactMessageService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void receiveMessage_shouldReturnSuccess_whenValidRequest() throws Exception {
        // Arrange : Créer une requête valide
        ContactMessageRequest request = new ContactMessageRequest();
        request.setEmail("test@email.com");
        request.setSubject("Sujet de test");
        request.setMessage("Ceci est un message de test.");

        // Simuler la sauvegarde du message côté service
        Mockito.when(contactMessageService.saveMessage(any(ContactMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent successfully."));
    }

    @Test
    void receiveMessage_shouldReturnBadRequest_whenMissingFields() throws Exception {
        // Arrange : requête sans email ni message
        ContactMessageRequest request = new ContactMessageRequest();
        request.setEmail(""); // Champ vide
        request.setMessage(""); // Champ vide

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}