package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Double amount, balance;
    private String description;
    private LocalDateTime date;
    private TransactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transactions(){ }

    public Transactions(Double amount, Double balance, String description, LocalDateTime date, TransactionType type){
        this.amount = amount;
        this.balance = balance;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public long getId() {return id;}

    public Double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public Double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    public TransactionType getType() {return type;}
    public void setType(TransactionType type) {this.type = type;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public LocalDateTime getDate() {return date;}
    public void setDate(LocalDateTime date) {this.date = date;}

    public Account getAccount() {
        return account;
    }
    public void setClient(Account account) {
        this.account = account;
    }
}
