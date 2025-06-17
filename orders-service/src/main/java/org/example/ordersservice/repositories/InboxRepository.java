package org.example.ordersservice.repositories;

import org.example.ordersservice.db_entities.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<InboxMessage, Long> {
}
