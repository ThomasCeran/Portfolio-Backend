package com.portfolio.backend.service;

import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.repository.ContactMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContactMessageService class.
 */
class ContactMessageServiceTest {

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @InjectMocks
    private ContactMessageService contactMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllMessages() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageRepository.findAll()).thenReturn(messages);

        List<ContactMessage> result = contactMessageService.findAllMessages();

        assertEquals(2, result.size());
        verify(contactMessageRepository, times(1)).findAll();
    }

    @Test
    void testFindMessageById() {
        ContactMessage message = new ContactMessage();
        message.setId(1L);
        when(contactMessageRepository.findById(1L)).thenReturn(Optional.of(message));

        Optional<ContactMessage> result = contactMessageService.findMessageById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(contactMessageRepository, times(1)).findById(1L);
    }

    @Test
    void testFindMessagesByEmail() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageRepository.findByEmail("user@example.com")).thenReturn(messages);

        List<ContactMessage> result = contactMessageService.findMessagesByEmail("user@example.com");

        assertEquals(2, result.size());
        verify(contactMessageRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void testFindMessagesCreatedAfter() {
        LocalDateTime date = LocalDateTime.now().minusDays(5);
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageRepository.findByCreatedAtAfter(date)).thenReturn(messages);

        List<ContactMessage> result = contactMessageService.findMessagesCreatedAfter(date);

        assertEquals(2, result.size());
        verify(contactMessageRepository, times(1)).findByCreatedAtAfter(date);
    }

    @Test
    void testFindUnreadMessages() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageRepository.findByReadFalse()).thenReturn(messages);

        List<ContactMessage> result = contactMessageService.findUnreadMessages();

        assertEquals(2, result.size());
        verify(contactMessageRepository, times(1)).findByReadFalse();
    }

    @Test
    void testSearchMessagesByKeyword() {
        List<ContactMessage> messages = Arrays.asList(new ContactMessage(), new ContactMessage());
        when(contactMessageRepository.searchByKeyword("portfolio")).thenReturn(messages);

        List<ContactMessage> result = contactMessageService.searchMessagesByKeyword("portfolio");

        assertEquals(2, result.size());
        verify(contactMessageRepository, times(1)).searchByKeyword("portfolio");
    }

    @Test
    void testSaveMessage() {
        ContactMessage message = new ContactMessage();
        when(contactMessageRepository.save(message)).thenReturn(message);

        ContactMessage result = contactMessageService.saveMessage(message);

        assertNotNull(result);
        verify(contactMessageRepository, times(1)).save(message);
    }

    @Test
    void testDeleteMessageById() {
        Long messageId = 1L;

        contactMessageService.deleteMessageById(messageId);

        verify(contactMessageRepository, times(1)).deleteById(messageId);
    }
}
