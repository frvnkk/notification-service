package com.example.notificationservice.dto;

import java.time.LocalDateTime;

public class UserEvent {

    private String eventType;  // "CREATED" или "DELETED"
    private String email;
    private String username;
    private LocalDateTime timestamp;

    // Конструктор по умолчанию (обязателен для Jackson)
    public UserEvent() {
        this.timestamp = LocalDateTime.now();
    }

    // Конструктор с параметрами
    public UserEvent(String eventType, String email, String username) {
        this.eventType = eventType;
        this.email = email;
        this.username = username;
        this.timestamp = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType='" + eventType + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}