package com.portfolio.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

/**
 * Represents a contact message sent by a user.
 */
@Entity
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email of the sender (optional, useful for unregistered users).
     */
    @Column(nullable = false)
    @Email(message = "Invalid email format")
    private String email;

    /**
     * Subject of the contact message.
     */
    private String subject;

    /**
     * Content of the contact message.
     */
    @Column(nullable = false)
    private String message;

    /**
     * Indicates whether the message has been read.
     */
    @Column(nullable = false)
    private boolean read = false;

    /**
     * Timestamp indicating when the message was created.
     * This field cannot be updated once the message is created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Reference to the user who sent the message (optional).
     * Many-to-One relationship with the `User` entity.
     */
    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = true)
    // private User user;

    /**
     * Automatically sets the creation date before persisting the entity.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // public User getUser() {
    //     return user;
    // }

    // public void setUser(User user) {
    //     this.user = user;
    // }
}
