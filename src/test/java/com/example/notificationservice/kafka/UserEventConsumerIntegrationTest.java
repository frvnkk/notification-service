package com.example.notificationservice.kafka;

import com.example.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class UserEventConsumerIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
    )
            .withReuse(true)
            .withStartupTimeout(java.time.Duration.ofSeconds(120)); 

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private EmailService emailService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
    }

    @Test
    void shouldProcessUserCreatedEvent() {
        // Упрощенный тест
        String message = "{\"eventType\":\"USER_CREATED\",\"email\":\"test@example.com\"}";

        kafkaTemplate.send("user-events", message);

        // Ждем меньше времени
        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(emailService, timeout(3000))
                            .sendUserCreatedEmail("test@example.com", null);
                });
    }
}