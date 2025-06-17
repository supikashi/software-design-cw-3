package org.example.paymentsservice.repositories;

import org.example.paymentsservice.db_entities.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findAllByPublished(boolean published);
}