package com.example.notificationservice;

import com.example.notificationservice.dto.UserEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserEventTest {

    @Test
    void testConstructorWithParameters() {
        // Given
        String eventType = "CREATED";
        String email = "test@example.com";
        String username = "Test User";

        // When
        UserEvent event = new UserEvent(eventType, email, username);

        // Then
        assertEquals(eventType, event.getEventType());
        assertEquals(email, event.getEmail());
        assertEquals(username, event.getUsername());
        assertNotNull(event.getTimestamp());
    }

    @Test
    void testDefaultConstructor() {
        // When
        UserEvent event = new UserEvent();

        // Then
        assertNull(event.getEventType());
        assertNull(event.getEmail());
        assertNull(event.getUsername());
        assertNotNull(event.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        UserEvent event = new UserEvent();

        // When
        event.setEventType("DELETED");
        event.setEmail("delete@example.com");
        event.setUsername("Delete User");
        LocalDateTime now = LocalDateTime.now();
        event.setTimestamp(now);

        // Then
        assertEquals("DELETED", event.getEventType());
        assertEquals("delete@example.com", event.getEmail());
        assertEquals("Delete User", event.getUsername());
        assertEquals(now, event.getTimestamp());
    }

    @Test
    void testToString() {
        // Given
        UserEvent event = new UserEvent("CREATED", "user@example.com", "John Doe");

        // When
        String result = event.toString();

        // Then
        assertTrue(result.contains("CREATED"));
        assertTrue(result.contains("user@example.com"));
        assertTrue(result.contains("John Doe"));
    }
}