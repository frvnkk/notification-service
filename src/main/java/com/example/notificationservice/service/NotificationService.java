package com.example.notificationservice.service;

import com.example.notificationservice.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private EmailService emailService;

    /**
     * Обрабатывает событие пользователя (создание/удаление)
     */
    public void processUserEvent(UserEvent event) {
        logger.info("Processing user event: {}", event);

        validateEvent(event);

        String email = event.getEmail();
        String username = event.getUsername() != null ? event.getUsername() : email;

        switch (event.getEventType().toUpperCase()) {
            case "CREATED":
                logger.info("Processing 'CREATED' event for user: {}", email);
                emailService.sendAccountCreatedEmail(email, username);
                break;

            case "DELETED":
                logger.info("Processing 'DELETED' event for user: {}", email);
                emailService.sendAccountDeletedEmail(email, username);
                break;

            default:
                logger.error("Unknown event type: {}", event.getEventType());
                throw new IllegalArgumentException(
                        String.format("Unknown event type: %s. Expected: CREATED or DELETED",
                                event.getEventType())
                );
        }

        logger.info("Successfully processed event for user: {}", email);
    }

    /**
     * Отправляет кастомное письмо через API
     */
    public void sendCustomEmail(String email, String subject, String message) {
        logger.info("Sending custom email to: {}, subject: {}", email, subject);
        emailService.sendSimpleEmail(email, subject, message);
    }

    /**
     * Отправляет тестовое письмо
     */
    public void sendTestEmail(String email) {
        logger.info("Sending test email to: {}", email);
        emailService.sendTestEmail(email);
    }

    /**
     * Валидирует событие
     */
    private void validateEvent(UserEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        if (event.getEventType() == null || event.getEventType().trim().isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }

        if (event.getEmail() == null || event.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (!isValidEmail(event.getEmail())) {
            throw new IllegalArgumentException("Invalid email format: " + event.getEmail());
        }
    }

    /**
     * Простая валидация email
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}