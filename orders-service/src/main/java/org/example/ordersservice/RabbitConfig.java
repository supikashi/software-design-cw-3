package org.example.ordersservice;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String STATUS_QUEUE = "status.queue";

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, false);
    }

    @Bean
    public Queue statusQueue() {
        return new Queue(STATUS_QUEUE, false);
    }
}