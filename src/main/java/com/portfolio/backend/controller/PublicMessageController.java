package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ContactMessageRequest;
import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.ContactMessageService;
import com.portfolio.backend.service.RecaptchaService;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages")
public class PublicMessageController {

    private final ContactMessageService contactMessageService;
    private final RecaptchaService recaptchaService;

    public PublicMessageController(ContactMessageService contactMessageService, RecaptchaService recaptchaService) {
        this.contactMessageService = contactMessageService;
        this.recaptchaService = recaptchaService;
    }

    @Operation(summary = "Lister les messages reçus", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessage>> getMessages() {
        return ResponseEntity.ok(contactMessageService.findAllMessages());
    }

    @Operation(summary = "Envoyer un message de contact",
            responses = @ApiResponse(responseCode = "202", description = "Message enregistré"))
    @PostMapping
    public ResponseEntity<Void> receiveMessage(@Valid @RequestBody ContactMessageRequest request) {
        String remoteIp = null;
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
            remoteIp = attrs.getRequest().getRemoteAddr();
        }

        if (!recaptchaService.isTokenValid(request.getRecaptcha(), remoteIp)) {
            return ResponseEntity.badRequest().build();
        }

        ContactMessage message = new ContactMessage();
        message.setName(request.getName());
        message.setEmail(request.getEmail());
        message.setPhone(request.getPhone());
        message.setSubject(request.getSubject());
        message.setMessage(request.getMessage());
        message.setRead(false);

        contactMessageService.saveMessage(message);
        return ResponseEntity.accepted().build();
    }
}
