package org.example.ordersservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.RabbitConfig;
import org.example.ordersservice.db_entities.OutboxMessage;
import org.example.ordersservice.repositories.OutboxRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper = new ObjectMapper();

    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbit) {
        this.outboxRepository = outboxRepository;
        this.rabbit = rabbit;
    }

    @Scheduled(fixedDelay = 1000)
    public void publishPending() {
        List<OutboxMessage> pending = outboxRepository.findAllByPublished(false);
        for (OutboxMessage message : pending) {
            String json;
            try {
                json = mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            rabbit.convertAndSend(RabbitConfig.PAYMENT_QUEUE, json);
            message.setPublished(true);
            outboxRepository.save(message);
            System.out.println("Sent: " + message);
        }
    }
}
