package com.portfolio.backend.controller;

import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.ContactMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContactMessageController class.
 */
class ContactMessageControllerTest {

    @Mock
    private ContactMessageService contactMessageService;

    @InjectMocks
    private AdminContactMessageController contactMessageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMessages() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageService.findAllMessages()).thenReturn(messages);

        ResponseEntity<List<ContactMessage>> response = contactMessageController.getAllMessages();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(contactMessageService, times(1)).findAllMessages();
    }

    @Test
    void testGetMessageById() {
        ContactMessage message = new ContactMessage();
        UUID messageId = UUID.randomUUID();
        message.setId(messageId);
        when(contactMessageService.findMessageById(messageId)).thenReturn(Optional.of(message));

        ResponseEntity<ContactMessage> response = contactMessageController.getMessageById(messageId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(messageId, response.getBody().getId());
        verify(contactMessageService, times(1)).findMessageById(messageId);
    }

    @Test
    void testGetMessagesByEmail() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageService.findMessagesByEmail("user@example.com")).thenReturn(messages);

        ResponseEntity<List<ContactMessage>> response = contactMessageController.getMessagesByEmail("user@example.com");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(contactMessageService, times(1)).findMessagesByEmail("user@example.com");
    }

    @Test
    void testGetMessagesCreatedAfter() {
        LocalDateTime date = LocalDateTime.now().minusDays(5);
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageService.findMessagesCreatedAfter(date)).thenReturn(messages);

        ResponseEntity<List<ContactMessage>> response = contactMessageController.getMessagesCreatedAfter(date);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(contactMessageService, times(1)).findMessagesCreatedAfter(date);
    }

    @Test
    void testGetUnreadMessages() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageService.findUnreadMessages()).thenReturn(messages);

        ResponseEntity<List<ContactMessage>> response = contactMessageController.getUnreadMessages();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(contactMessageService, times(1)).findUnreadMessages();
    }

    @Test
    void testSearchMessagesByKeyword() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageService.searchMessagesByKeyword("portfolio")).thenReturn(messages);

        ResponseEntity<List<ContactMessage>> response = contactMessageController.searchMessagesByKeyword("portfolio");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(contactMessageService, times(1)).searchMessagesByKeyword("portfolio");
    }

    @Test
    void testSaveMessage() {
        ContactMessage message = new ContactMessage();
        when(contactMessageService.saveMessage(message)).thenReturn(message);

        ResponseEntity<ContactMessage> response = contactMessageController.saveMessage(message);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(contactMessageService, times(1)).saveMessage(message);
    }

    @Test
    void testDeleteMessageById() {
        UUID messageId = UUID.randomUUID();

        ResponseEntity<Void> response = contactMessageController.deleteMessageById(messageId);

        assertEquals(204, response.getStatusCode().value());
        verify(contactMessageService, times(1)).deleteMessageById(messageId);
    }
}
