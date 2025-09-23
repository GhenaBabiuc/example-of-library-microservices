package org.example.borrowingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.borrowingservice.model.BorrowingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${borrowing.kafka.topic:borrowing-events}")
    private String topic;

    public void sendBorrowingEvent(BorrowingEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, event.getBorrowingId().toString(), eventJson);
            log.info("Sent borrowing event: {} for borrowing ID: {}", event.getEventType(), event.getBorrowingId());
        } catch (Exception e) {
            log.error("Failed to send borrowing event: {}", event, e);
        }
    }
}
