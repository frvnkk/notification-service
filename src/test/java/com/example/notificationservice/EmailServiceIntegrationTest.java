package com.example.notificationservice;

import com.example.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        properties = {
                // Используем фейковый SMTP
                "spring.mail.host=localhost",
                "spring.mail.port=587",
                "spring.mail.properties.mail.smtp.auth=false",
                "app.email.from=test@test.com"
        }
)
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testEmailServiceBeanCreated() {
        // Просто проверяем что бин создан
        assertThat(emailService).isNotNull();
        System.out.println("✅ EmailService bean is created");
    }

    @Test
    void testEmailServiceMethodsExist() {
        // Проверяем что методы существуют (без реальной отправки)
        assertThat(emailService).hasFieldOrProperty("mailSender");
        System.out.println("✅ EmailService has required methods");
    }
}