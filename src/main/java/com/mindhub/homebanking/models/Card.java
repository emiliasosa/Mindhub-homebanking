package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private boolean disable;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card(){ }
    public Card(Client cardHolder, CardType type, CardColor color, String number, int cvv, LocalDate fromDate, LocalDate thruDate, boolean disable){
        this.cardHolder = cardHolder.getFirstName() + " " + cardHolder.getLastName();
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.disable = disable;
    }

    public long getId(){return id;}
    public String getCardHolder(){return cardHolder;}
    public String setCardHolder(){return this.cardHolder = cardHolder;}
    public CardType getType(){return type;}
    public CardColor getColor(){return color;}
    public String getNumber(){return number;}
    public String setNumber(String number){return this.number = number;}
    public int getCvv(){return cvv;}
    public int setCvv(){return this.cvv = cvv;}
    public LocalDate getThruDate(){return thruDate;}
    public LocalDate setThruDate(LocalDate thruDate){return this.thruDate = thruDate;}
    public LocalDate getFromDate(){return fromDate;}
    public LocalDate setFromDate(LocalDate  fromDate){return this.fromDate = fromDate;}
    public Client getClient(){return client;}
    public void setClient(Client client) {
        this.client = client;
    }
    public boolean getDisable(){return disable;}
    public void setDisable(boolean disable){this.disable = disable;}



}
