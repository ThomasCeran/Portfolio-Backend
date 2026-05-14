package com.portfolio.backend.service;

import com.portfolio.backend.entity.ContactMessage;

public interface TelegramNotificationService extends NotificationService {

    @Override
    void notifyNewContact(ContactMessage message);
}
