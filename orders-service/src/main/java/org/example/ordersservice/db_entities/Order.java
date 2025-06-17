package org.example.ordersservice.db_entities;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long accountId;
    private String name;
    private double sum;
    private Status status;

    public Order() {}

    public Order(long id, long accountId, String name, double sum, Status status) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.sum = sum;
        this.status = status;
    }

    public long getId() { return id; }

    public long getAccountId() { return accountId; }

    public String getName() { return name; }

    public double getSum() { return sum; }

    public Status getStatus() { return status; }

    public void setId(long id) { this.id = id; }

    public void setAccountId(long accountId) { this.accountId = accountId; }

    public void setName(String name) { this.name = name; }

    public void setSum(double sum) { this.sum = sum; }

    public void setStatus(Status status) { this.status = status; }
}
