package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime date;
    private double balance;
    private String typeOfMoney;
    private AccountType type;
    private boolean disable;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch=FetchType.EAGER)
    private Set<Transactions> transactions = new HashSet<>();

    public Account () {}

    public Account(String number, LocalDateTime date, double balance, String typeOfMoney, AccountType type, boolean disable){
        this.number = number;
        this.date = date;
        this.balance = balance;
        this.typeOfMoney = typeOfMoney;
        this.type = type;
        this.disable = disable;
    }

    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getTypeOfMoney(){return typeOfMoney;}
    public void setTypeOfMoney(String typeOfMoney){this.typeOfMoney = typeOfMoney;}
    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
    public boolean getDisable() {
        return disable;
    }
    public void setDisable(boolean disable) {
        this.disable = disable;
    }
    public Set<Transactions> getTransactions(){
        return transactions;
    }
    public void addTransaction(Transactions transaction) {
        transaction.setClient(this);
        transactions.add(transaction);
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

}
