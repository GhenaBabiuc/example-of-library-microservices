package com.example.notificationservice.kafka;

import com.example.notificationservice.model.BorrowingEvent;
import com.example.notificationservice.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${notification.kafka.topic:borrowing-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBorrowingEvent(@Payload String eventJson) {
        try {
            log.info("Received borrowing event: {}", eventJson);

            BorrowingEvent event = objectMapper.readValue(eventJson, BorrowingEvent.class);

            log.info("Processing event: {} for user: {} and book: {}", event.getEventType(), event.getUserEmail(), event.getBookTitle());

            emailService.sendBorrowingNotification(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse borrowing event JSON: {}", eventJson, e);
        } catch (Exception e) {
            log.error("Failed to process borrowing event: {}", eventJson, e);
        }
    }
}
