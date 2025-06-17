package org.example.paymentsservice.repositories;

import org.example.paymentsservice.db_entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository
        extends JpaRepository<Account, Long> {
}
