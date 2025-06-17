package org.example.ordersservice.services;

import jakarta.transaction.Transactional;
import org.example.ordersservice.db_entities.Order;
import org.example.ordersservice.db_entities.OutboxMessage;
import org.example.ordersservice.db_entities.Status;
import org.example.ordersservice.repositories.OrderRepository;
import org.example.ordersservice.repositories.OutboxRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;

    public OrdersService(OrderRepository orderRepository, OutboxRepository outboxRepository) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public Order createOrder(long userId, double sum, String name) {
        Order order = new Order();
        order.setAccountId(userId);
        order.setSum(sum);
        order.setName(name);
        order.setStatus(Status.NEW);
        order = orderRepository.save(order);

        OutboxMessage message = new OutboxMessage();
        message.setOrderId(order.getId());
        message.setAccountId(order.getAccountId());
        message.setSum(order.getSum());
        message.setPublished(false);

        outboxRepository.save(message);
        return order;

    }

    public void changeOrderStatus(long orderId, Status status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    public Order getOrder(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("order with id = " + id + " not found");
        }
        return order.get();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String msg) { super(msg); }
    }
}
