package org.example.paymentsservice.db_entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "outbox")
public class OutboxMessage {
    @Id @GeneratedValue
    private long id;
    private long orderId;

    private Status status;
    private boolean published = false;

    public OutboxMessage() {
    }

    public OutboxMessage(
            long id,
            long orderId,
            Status status
    ) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public enum Status {
        NEW,
        FINISHED,
        CANCELLED
    }

    @Override
    public String toString() {
        return "InboxMessage id " + id + " orderId " + orderId + " status " + status.toString();
    }
}