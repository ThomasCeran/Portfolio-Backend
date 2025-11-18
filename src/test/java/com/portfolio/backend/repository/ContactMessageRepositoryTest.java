package com.portfolio.backend.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.portfolio.backend.entity.ContactMessage;

/**
 * Unit tests for ContactMessageRepository.
 */
@DataJpaTest
class ContactMessageRepositoryTest {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    private ContactMessage message1;
    private ContactMessage message2;

    @BeforeEach
    void setUp() {
        message1 = new ContactMessage();
        message1.setName("User 1");
        message1.setEmail("user1@example.com");
        message1.setSubject("Inquiry about services");
        message1.setMessage("I would like to know more about your portfolio.");
        message1.setRead(false); // Par défaut, un message est non lu

        message2 = new ContactMessage();
        message2.setName("User 2");
        message2.setEmail("user2@example.com");
        message2.setSubject("Collaboration request");
        message2.setMessage("Would you be interested in a joint project?");
        message2.setRead(true);

        contactMessageRepository.save(message1);
        contactMessageRepository.save(message2);
    }

    @Test
    void testFindByEmail() {
        List<ContactMessage> messages = contactMessageRepository.findByEmail("user1@example.com");
        assertEquals(1, messages.size());
        assertEquals("Inquiry about services", messages.get(0).getSubject());
    }

    @Test
    void testFindByCreatedAtAfter() {
        List<ContactMessage> messages = contactMessageRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(3));
        assertEquals(2, messages.size());
    }

    @Test
    void testFindByReadFalse() {
        List<ContactMessage> unreadMessages = contactMessageRepository.findByReadFalse();
        assertEquals(1, unreadMessages.size());
        assertFalse(unreadMessages.get(0).isRead());
    }

    @Test
    void testSearchByKeyword() {
        List<ContactMessage> messages = contactMessageRepository.searchByKeyword("portfolio");
        assertEquals(1, messages.size());
        assertEquals("Inquiry about services", messages.get(0).getSubject());
    }
}
