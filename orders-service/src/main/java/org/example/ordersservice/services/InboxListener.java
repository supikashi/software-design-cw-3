package org.example.ordersservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rabbitmq.client.Channel;
import org.example.ordersservice.RabbitConfig;
import org.example.ordersservice.db_entities.InboxMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InboxListener {
    private final OrdersService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public InboxListener(OrdersService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.STATUS_QUEUE, ackMode = "MANUAL")
    public void receive(String json, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            InboxMessage message = mapper.readValue(json, InboxMessage.class);
            System.out.println("Parsed InboxMessage: " + message);
            service.changeOrderStatus(message.getOrderId(), message.getStatus());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            System.err.println("Ошибка обработки сообщения: " + e.getMessage());
            channel.basicNack(tag, false, true);
        }
    }
}