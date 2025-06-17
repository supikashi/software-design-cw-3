package org.example.ordersservice.db_entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "outbox")
public class OutboxMessage {
    @Id
    @GeneratedValue
    private long id;
    private long orderId;
    private long accountId;
    private double sum;
    private boolean published = false;

    public OutboxMessage() {
    }

    public OutboxMessage(
            long id,
            long orderId,
            long accountId,
            double sum
    ) {
        this.id = id;
        this.orderId = orderId;
        this.accountId = accountId;
        this.sum = sum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "InboxMessage: id " + id + " orderId " + orderId + " accountId " + accountId + " sum " + sum;
    }
}
