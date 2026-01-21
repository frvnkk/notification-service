package com.example.notificationservice;

import com.example.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendUserCreatedEmail() {
        assertDoesNotThrow(() -> {
            emailService.sendUserCreatedEmail("test@example.com", "John");
        });
    }

    @Test
    void testSendUserDeletedEmail() {
        assertDoesNotThrow(() -> {
            emailService.sendUserDeletedEmail("test@example.com", "Jane");
        });
    }

    @Test
    void testSendCustomEmail() {
        assertDoesNotThrow(() -> {
            emailService.sendCustomEmail("test@example.com", "Subject", "Text");
        });
    }
}