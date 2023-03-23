package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime date;
    private double balance;
    private String typeOfMoney;
    private boolean disable;
    private AccountType type;
    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
        this.typeOfMoney = account.getTypeOfMoney();
        this.type = account.getType();
        this.disable = account.getDisable();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
    }

    public long getId(){return  id;}
    public Set<TransactionDTO>  getTransactions() {
        return transactions;
    }
    public String getNumber() {
        return number;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public double getBalance() {
        return balance;
    }
    public String getTypeOfMoney(){return typeOfMoney;}
    public boolean getDisable() {
        return disable;
    }
    public AccountType getType(){return type;}

}

