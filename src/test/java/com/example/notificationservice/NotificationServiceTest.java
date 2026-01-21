package com.example.notificationservice;

import com.example.notificationservice.event.UserEvent;
import com.example.notificationservice.service.EmailService;
import com.example.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private EmailService emailService;

    @Test
    void testProcessUserCreatedEvent() {
        // Given
        UserEvent event = new UserEvent();
        event.setEventType("USER_CREATED");
        event.setEmail("test@example.com");
        event.setUsername("John Doe");

        // When
        notificationService.processUserEvent(event);

        // Then
        verify(emailService, times(1))
                .sendUserCreatedEmail("test@example.com", "John Doe");
    }

    @Test
    void testProcessUserDeletedEvent() {
        // Given
        UserEvent event = new UserEvent();
        event.setEventType("USER_DELETED");
        event.setEmail("delete@example.com");
        event.setUsername("Jane Smith");

        // When
        notificationService.processUserEvent(event);

        // Then
        verify(emailService, times(1))
                .sendUserDeletedEmail("delete@example.com", "Jane Smith");
    }

    @Test
    void testProcessUnknownEvent() {
        // Given
        UserEvent event = new UserEvent();
        event.setEventType("UNKNOWN_EVENT");
        event.setEmail("test@example.com");

        // When
        notificationService.processUserEvent(event);

        // Then - не должно быть вызовов emailService
        verify(emailService, times(0))
                .sendUserCreatedEmail(anyString(), anyString());
        verify(emailService, times(0))
                .sendUserDeletedEmail(anyString(), anyString());
    }

    @Test
    void testSendWelcomeEmail() {
        // When
        notificationService.sendWelcomeEmail("test@example.com", "John");

        // Then
        verify(emailService, times(1))
                .sendUserCreatedEmail("test@example.com", "John");
    }

    @Test
    void testSendGoodbyeEmail() {
        // When
        notificationService.sendGoodbyeEmail("test@example.com", "Jane");

        // Then
        verify(emailService, times(1))
                .sendUserDeletedEmail("test@example.com", "Jane");
    }

    @Test
    void testSendCustomEmail() {
        // When
        notificationService.sendCustomEmail("test@example.com", "Subject", "Text");

        // Then
        verify(emailService, times(1))
                .sendCustomEmail("test@example.com", "Subject", "Text");
    }
}