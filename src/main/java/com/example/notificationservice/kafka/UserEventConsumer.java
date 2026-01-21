package com.example.notificationservice.kafka;

import com.example.notificationservice.event.UserEvent;
import com.example.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationService notificationService;

    public UserEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void listen(String message) {
        logger.info("Получено сообщение из Kafka: {}", message);

        try {
            UserEvent event = objectMapper.readValue(message, UserEvent.class);
            notificationService.processUserEvent(event);

        } catch (Exception e) {
            logger.error("Ошибка обработки сообщения из Kafka: {}", e.getMessage());
        }
    }
}