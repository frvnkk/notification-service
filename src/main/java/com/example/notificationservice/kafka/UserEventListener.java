package com.example.notificationservice.kafka;

import com.example.notificationservice.dto.UserEvent;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    @Autowired
    private NotificationService notificationService;

    @Value("${kafka.topics.user-events}")
    private String topicName;

    /**
     * Слушатель событий пользователей из Kafka
     */
    @KafkaListener(
            topics = "${kafka.topics.user-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            @Payload UserEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("=== KAFKA MESSAGE RECEIVED ===");
        logger.info("Topic: {}", topic);
        logger.info("Partition: {}", partition);
        logger.info("Offset: {}", offset);
        logger.info("Event: {}", event);
        logger.info("==============================");

        try {
            notificationService.processUserEvent(event);
            logger.info("Successfully processed Kafka message for user: {}", event.getEmail());
        } catch (Exception e) {
            logger.error("Failed to process Kafka message: {}", event, e);
            // В реальном приложении здесь можно добавить логику обработки ошибок
            // Например, отправку в Dead Letter Queue или повторную попытку
        }
    }

    /**
     * Тестовый слушатель для проверки работы Kafka
     */
    @KafkaListener(
            topics = "${kafka.topics.user-events}",
            groupId = "test-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenTest(UserEvent event) {
        logger.info("[TEST] Received test message: {}", event);
    }
}