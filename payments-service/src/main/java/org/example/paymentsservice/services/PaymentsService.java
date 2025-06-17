package org.example.paymentsservice.services;

import jakarta.transaction.Transactional;
import org.example.paymentsservice.db_entities.Account;
import org.example.paymentsservice.db_entities.InboxMessage;
import org.example.paymentsservice.db_entities.OutboxMessage;
import org.example.paymentsservice.repositories.AccountRepository;
import org.example.paymentsservice.repositories.InboxRepository;
import org.example.paymentsservice.repositories.OutboxRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentsService {
    private final InboxRepository inboxRepository;
    private final OutboxRepository outboxRepository;
    private final AccountRepository repository;

    public PaymentsService(InboxRepository inboxRepository, OutboxRepository outboxRepository, AccountRepository repository) {
        this.inboxRepository = inboxRepository;
        this.outboxRepository = outboxRepository;
        this.repository = repository;
    }

    public void createAccount(long userId) {
        if (!repository.existsById(userId)) {
            repository.save(new Account(userId, 0.0));
        }
    }

    public Account getAccountById(long userId) {
        Optional<Account> account = repository.findById(userId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("account with id = " + userId + " not found");
        } else {
            return account.get();
        }
    }

    public void changBalance(long userId, double newBalance) {
        if (!repository.existsById(userId)) {
            throw new AccountNotFoundException("account with id = " + userId + " not found");
        }
        repository.save(new Account(userId, newBalance));
    }

    @Transactional
    public void payForOrder(InboxMessage message) {
        if (inboxRepository.existsById(message.getId()))
            return;
        inboxRepository.save(message);
        OutboxMessage outbox = new OutboxMessage();
        outbox.setOrderId(message.getOrderId());

        if (repository.existsById(message.getAccountId())) {
            Account account = getAccountById(message.getAccountId());
            if (account.getBalance() < message.getSum()) {
                outbox.setStatus(OutboxMessage.Status.CANCELLED);
            } else {
                changBalance(message.getAccountId(), account.getBalance() - message.getSum());
                outbox.setStatus(OutboxMessage.Status.FINISHED);
            }
        } else {
            outbox.setStatus(OutboxMessage.Status.CANCELLED);
        }

        outboxRepository.save(outbox);
    }

    public static class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String msg) { super(msg); }
    }
}
