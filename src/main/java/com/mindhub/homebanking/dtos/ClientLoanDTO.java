package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount, amountWithInterest;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.amountWithInterest = clientLoan.getAmountWithInterest();
        this.payments = clientLoan.getPayments();
    }

    public long getId() {return  id;}
    public long getLoanId() {return loanId;}
    public String getName(){return name;}
    public double getAmount() {return amount;}
    public Double getAmountWithInterest() {return amountWithInterest;}
    public int getPayments() {return payments;}
}
