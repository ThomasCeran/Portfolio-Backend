package com.portfolio.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.backend.dto.ContactMessageRequest;
import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.ContactMessageService;

class PublicMessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContactMessageService contactMessageService;

    @InjectMocks
    private PublicMessageController publicMessageController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publicMessageController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void receiveMessage_shouldReturnSuccess_whenValidRequest() throws Exception {
        ContactMessageRequest request = new ContactMessageRequest();
        request.setName("John");
        request.setEmail("test@email.com");
        request.setSubject("Sujet de test");
        request.setMessage("Ceci est un message de test.");

        Mockito.when(contactMessageService.saveMessage(any(ContactMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @Test
    void receiveMessage_shouldReturnBadRequest_whenMissingFields() throws Exception {
        ContactMessageRequest request = new ContactMessageRequest();
        request.setName("");
        request.setEmail("");
        request.setSubject("");
        request.setMessage("");

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessages_shouldReturnListForAdmins() throws Exception {
        Mockito.when(contactMessageService.findAllMessages()).thenReturn(List.of(new ContactMessage()));

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk());
    }
}
