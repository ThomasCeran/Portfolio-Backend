package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ContactMessageRequest;
import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.repository.UserRepository;
import com.portfolio.backend.service.ContactMessageService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class PublicMessageController {

    private final ContactMessageService contactMessageService;
    private final UserRepository userRepository;

    public PublicMessageController(ContactMessageService contactMessageService,
            UserRepository userRepository) {
        this.contactMessageService = contactMessageService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> receiveMessage(@Valid @RequestBody ContactMessageRequest request) {
        ContactMessage message = new ContactMessage();
        message.setEmail(request.getEmail());
        message.setSubject(request.getSubject());
        message.setMessage(request.getMessage());
        message.setRead(false);

        // Associer un utilisateur si connecté, sinon null
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            User user = userRepository.findByEmail(username).orElse(null);
            if (user != null) {
                message.setUser(user);
            }
        }

        contactMessageService.saveMessage(message);
        return ResponseEntity.ok("Message sent successfully.");
    }
}
