package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.models.Transactions;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Double amount, balance;
    private TransactionType type;
    private String description;
    private LocalDateTime date;

    public TransactionDTO(Transactions transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.balance = transaction.getBalance();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
    }

    public Long getId() {return id;}
    public Double getAmount() {
        return amount;
    }
    public Double getBalance() {
        return balance;
    }
    public TransactionType getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public LocalDateTime getDate() {
        return date;
    }

}
