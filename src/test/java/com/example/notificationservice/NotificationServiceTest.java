package com.example.notificationservice;

import com.example.notificationservice.dto.UserEvent;
import com.example.notificationservice.service.EmailService;
import com.example.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testProcessUserEvent_Created() {
        // Given
        UserEvent event = new UserEvent("CREATED", "test@example.com", "Test User");

        // When
        notificationService.processUserEvent(event);

        // Then
        verify(emailService, times(1))
                .sendAccountCreatedEmail("test@example.com", "Test User");
    }

    @Test
    void testProcessUserEvent_Deleted() {
        // Given
        UserEvent event = new UserEvent("DELETED", "test@example.com", "Test User");

        // When
        notificationService.processUserEvent(event);

        // Then
        verify(emailService, times(1))
                .sendAccountDeletedEmail("test@example.com", "Test User");
    }

    @Test
    void testProcessUserEvent_InvalidEmail() {
        // Given
        UserEvent event = new UserEvent("CREATED", "invalid-email", "Test User");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.processUserEvent(event);
        });
    }

    @Test
    void testProcessUserEvent_UnknownEventType() {
        // Given
        UserEvent event = new UserEvent("UPDATED", "test@example.com", "Test User");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.processUserEvent(event);
        });
    }

    @Test
    void testSendCustomEmail() {
        // When
        notificationService.sendCustomEmail("test@example.com", "Subject", "Message");

        // Then
        verify(emailService, times(1))
                .sendSimpleEmail("test@example.com", "Subject", "Message");
    }

    @Test
    void testSendTestEmail() {
        // When
        notificationService.sendTestEmail("test@example.com");

        // Then
        verify(emailService, times(1))
                .sendTestEmail("test@example.com");
    }
}