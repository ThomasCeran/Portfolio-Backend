package com.portfolio.backend.service;

import com.portfolio.backend.entity.ContactMessage;

/**
 * Contract for downstream notifications triggered by public contact messages
 * (e.g., SMS, email, chat).
 */
public interface NotificationService {

    /**
     * Called after a new contact message has been persisted.
     *
     * @param message saved contact message
     */
    void notifyNewContact(ContactMessage message);
}
