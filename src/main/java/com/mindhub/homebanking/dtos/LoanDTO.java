package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import java.util.ArrayList;
import java.util.List;


public class LoanDTO {
    private long id;
    private String name, description;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private double interest;
    public LoanDTO(){ }
    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.description = loan.getDescription();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interest = loan.getInterest();
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public double getMaxAmount() {return maxAmount;}
    public List<Integer> getPayments() {return payments;}
    public double getInterest() {return interest;}
}
