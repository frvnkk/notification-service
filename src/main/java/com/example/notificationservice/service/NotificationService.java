package com.example.notificationservice.service;

import com.example.notificationservice.event.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void processUserEvent(UserEvent event) {
        String eventType = event.getEventType();
        String email = event.getEmail();
        String username = event.getUsername();

        logger.info("Processing user event: {} for user: {}", eventType, email);

        try {
            if ("USER_CREATED".equals(eventType)) {
                emailService.sendUserCreatedEmail(email, username);
                logger.info("Account creation email sent to: {}", email);
            } else if ("USER_DELETED".equals(eventType)) {
                emailService.sendUserDeletedEmail(email, username);
                logger.info("Account deletion email sent to: {}", email);
            } else {
                logger.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            logger.error("Failed to process user event: {}", e.getMessage(), e);
        }
    }

    // Дополнительные методы для REST API если нужны
    public void sendWelcomeEmail(String email, String username) {
        emailService.sendUserCreatedEmail(email, username);
    }

    public void sendGoodbyeEmail(String email, String username) {
        emailService.sendUserDeletedEmail(email, username);
    }

    public void sendCustomEmail(String toEmail, String subject, String text) {
        emailService.sendCustomEmail(toEmail, subject, text);
    }
}