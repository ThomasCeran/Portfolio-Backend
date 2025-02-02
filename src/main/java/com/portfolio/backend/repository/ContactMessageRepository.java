package com.portfolio.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.portfolio.backend.entity.ContactMessage;

/**
 * Repository interface for managing ContactMessage entity operations.
 * This interface extends JpaRepository, providing CRUD functionality
 * and additional custom queries.
 */
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    /**
     * Finds messages by sender's email.
     *
     * @param email the sender's email address.
     * @return a list of contact messages from the specified email.
     */
    List<ContactMessage> findByEmail(String email);

    /**
     * Finds messages sent after a specific date.
     *
     * @param createdAt the timestamp for filtering messages.
     * @return a list of contact messages created after the given date.
     */
    List<ContactMessage> findByCreatedAtAfter(LocalDateTime createdAt);

    /**
     * Finds all unread messages.
     *
     * @return a list of unread contact messages.
     */
    List<ContactMessage> findByReadFalse();

    /**
     * Finds messages by a specific keyword in the subject or message body.
     *
     * @param keyword the keyword to search for.
     * @return a list of contact messages containing the keyword.
     */
    @Query("SELECT cm FROM ContactMessage cm WHERE LOWER(cm.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cm.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ContactMessage> searchByKeyword(@Param("keyword") String keyword);
}
