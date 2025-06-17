package org.example.ordersservice.repositories;

import org.example.ordersservice.db_entities.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findAllByPublished(boolean published);
}
