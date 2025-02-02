package com.portfolio.backend.service;

import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing ContactMessage operations.
 */
@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    /**
     * Retrieves all contact messages.
     *
     * @return a list of all contact messages.
     */
    public List<ContactMessage> findAllMessages() {
        return contactMessageRepository.findAll();
    }

    /**
     * Retrieves a contact message by its ID.
     *
     * @param id the ID of the message.
     * @return an Optional containing the message if found, or empty otherwise.
     */
    public Optional<ContactMessage> findMessageById(Long id) {
        return contactMessageRepository.findById(id);
    }

    /**
     * Retrieves contact messages sent by a specific email.
     *
     * @param email the sender's email.
     * @return a list of contact messages from the specified email.
     */
    public List<ContactMessage> findMessagesByEmail(String email) {
        return contactMessageRepository.findByEmail(email);
    }

    /**
     * Retrieves contact messages created after a specific date.
     *
     * @param createdAt the cutoff creation date.
     * @return a list of messages created after the given date.
     */
    public List<ContactMessage> findMessagesCreatedAfter(LocalDateTime createdAt) {
        return contactMessageRepository.findByCreatedAtAfter(createdAt);
    }

    /**
     * Retrieves all unread messages.
     *
     * @return a list of unread contact messages.
     */
    public List<ContactMessage> findUnreadMessages() {
        return contactMessageRepository.findByReadFalse();
    }

    /**
     * Searches for contact messages that contain a specific keyword in the subject
     * or body.
     *
     * @param keyword the keyword to search for.
     * @return a list of matching contact messages.
     */
    public List<ContactMessage> searchMessagesByKeyword(String keyword) {
        return contactMessageRepository.searchByKeyword(keyword);
    }

    /**
     * Saves a new or updated contact message.
     *
     * @param message the contact message to save.
     * @return the saved contact message.
     */
    @Transactional
    public ContactMessage saveMessage(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    /**
     * Deletes a contact message by its ID.
     *
     * @param id the ID of the message to delete.
     */
    @Transactional
    public void deleteMessageById(Long id) {
        contactMessageRepository.deleteById(id);
    }
}
