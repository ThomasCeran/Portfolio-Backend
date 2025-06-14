package com.portfolio.backend.controller;

import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.ContactMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing contact messages.
 */
@RestController
@RequestMapping("/api/admin/messages")
@PreAuthorize("hasRole('ADMIN')")
public class AdminContactMessageController {

    private final ContactMessageService contactMessageService;

    public AdminContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        return ResponseEntity.ok(contactMessageService.findAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactMessage> getMessageById(@PathVariable Long id) {
        Optional<ContactMessage> message = contactMessageService.findMessageById(id);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<ContactMessage>> getMessagesByEmail(@PathVariable String email) {
        return ResponseEntity.ok(contactMessageService.findMessagesByEmail(email));
    }

    @GetMapping("/after/{date}")
    public ResponseEntity<List<ContactMessage>> getMessagesCreatedAfter(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(contactMessageService.findMessagesCreatedAfter(date));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<ContactMessage>> getUnreadMessages() {
        return ResponseEntity.ok(contactMessageService.findUnreadMessages());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ContactMessage>> searchMessagesByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(contactMessageService.searchMessagesByKeyword(keyword));
    }

    @PostMapping
    public ResponseEntity<ContactMessage> saveMessage(@RequestBody ContactMessage message) {
        return ResponseEntity.ok(contactMessageService.saveMessage(message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long id) {
        contactMessageService.deleteMessageById(id);
        return ResponseEntity.noContent().build();
    }
}
