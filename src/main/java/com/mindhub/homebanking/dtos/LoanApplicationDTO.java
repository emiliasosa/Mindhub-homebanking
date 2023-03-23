package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {
    private long loanId;
    private String name;
    private Double amount, amountWithInterest;
    private Integer payment;
    private String destinationAccount;

    public LoanApplicationDTO(long id, String name, Double amount, Double amountWithInterest, Integer payment, String destinationAccount){
        this.loanId = id;
        this.name = name;
        this.amount = amount;
        this.amountWithInterest = amountWithInterest;
        this.payment = payment;
        this.destinationAccount = destinationAccount;
    }

    public long getLoanId() {return loanId;}
    public void setLoanId(long id) {this.loanId = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Double getAmount() {return amount;}
    public void setAmount(Double amount) {this.amount = amount;}
    public Double getAmountWithInterest() {return amountWithInterest;}
    public void setAmountWithInterest(Double amountWithInterest) {this.amountWithInterest = amountWithInterest;}
    public Integer getPayment() {return payment;}
    public void setPayment(Integer payment) {this.payment = payment;}
    public String getDestinationAccount() {return destinationAccount;}
    public void setDestinationAccount(String destinationAccount) {this.destinationAccount = destinationAccount;}
}
