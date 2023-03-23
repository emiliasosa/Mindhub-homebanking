package com.mindhub.homebanking.dtos;

public class PostnetDTO {
    private long id;
    private String number;
    private Double amount;
    private int cvv;
    private String description;

    public PostnetDTO(long id, String number, int cvv, double amount, String description){
        this.id = id;
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public long getId() {return id;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public Double getAmount() {return amount;}
    public void setAmount(Double amount) {this.amount = amount;}
    public int getCvv() {return cvv;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

}
