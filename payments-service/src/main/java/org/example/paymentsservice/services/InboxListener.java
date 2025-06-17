package org.example.paymentsservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.example.paymentsservice.RabbitConfig;
import org.example.paymentsservice.db_entities.InboxMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InboxListener {
    private final PaymentsService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public InboxListener(PaymentsService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE, ackMode = "MANUAL")
    public void receive(String json, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            InboxMessage message = mapper.readValue(json, InboxMessage.class);
            System.out.println("Parsed InboxMessage: " + message);
            service.payForOrder(message);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            System.err.println("Ошибка обработки сообщения: " + e.getMessage());
            channel.basicNack(tag, false, true);
        }
    }
}
