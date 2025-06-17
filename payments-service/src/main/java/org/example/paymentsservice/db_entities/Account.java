package org.example.paymentsservice.db_entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
    @Id
    private long id;
    private double balance;

    public Account() {}
    public Account(long id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
