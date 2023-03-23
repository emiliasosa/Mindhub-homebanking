package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    private double interest;
    private String description;
    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans;

    public Loan(){ }
    public Loan(String name, double maxAmount, double interest, List<Integer> payments, String description){
        this.name = name;
        this.maxAmount = maxAmount;
        this.interest = interest;
        this.payments = payments;
        this.description = description;
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public double getMaxAmount() {return maxAmount;}
    public void setMaxAmount(long maxAmount) {this.maxAmount = maxAmount;}
    public double getInterest() {return interest;}
    public void setInterest(double interest) {this.interest = interest;}
    public List<Integer> getPayments() {return payments;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}
    public List<Client> getClients() {
        return clientLoans.stream().map(sub -> sub.getClient()).collect(toList());
    }

}
