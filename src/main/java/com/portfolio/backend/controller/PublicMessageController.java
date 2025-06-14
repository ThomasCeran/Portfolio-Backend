package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ContactMessageRequest;
import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.ContactMessageService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class PublicMessageController {

    private final ContactMessageService contactMessageService;

    public PublicMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<?> receiveMessage(@Valid @RequestBody ContactMessageRequest request) {
        ContactMessage message = new ContactMessage();
        message.setEmail(request.getEmail());
        message.setSubject(request.getSubject());
        message.setMessage(request.getMessage());
        message.setRead(false);

        contactMessageService.saveMessage(message);
        return ResponseEntity.ok("Message sent successfully.");
    }
}
