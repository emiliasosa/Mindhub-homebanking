package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
//se coloca el nombre de las dos cuentas asociadas, cada registro sera un cliente solicitando un prestamo
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;
    private Double amountWithInterest;
    private int payments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan(){ }

    public ClientLoan(double amount, Double amountWithInterest, int payments, Client client, Loan loan){
        this.id = id;
        this.amount = amount;
        this.amountWithInterest = amountWithInterest;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
    }

    public long getId() {return id;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public Double getAmountWithInterest() {return amountWithInterest;}
    public void setAmountWithInterest(Double amountWithInterest) {this.amountWithInterest = amountWithInterest;}

    public int getPayments() {return payments;}
    public void setPayments(int payments) {this.payments = payments;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

    public Loan getLoan() {return loan;}
    public void setLoan(Loan loan) {this.loan = loan;}
}
