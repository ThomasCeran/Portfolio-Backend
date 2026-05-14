package com.portfolio.backend.service;

import com.portfolio.backend.entity.ContactMessage;

public interface EmailNotificationService extends NotificationService {

    @Override
    void notifyNewContact(ContactMessage message);
}
