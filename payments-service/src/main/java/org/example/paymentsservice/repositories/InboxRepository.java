package org.example.paymentsservice.repositories;

import org.example.paymentsservice.db_entities.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<InboxMessage, Long> {
}

