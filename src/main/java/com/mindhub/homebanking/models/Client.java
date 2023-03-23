package com.mindhub.homebanking.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName, lastName, email, password, picture;
    @OneToMany(mappedBy = "client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client(){ }

    public Client(String firstName, String lastName, String email, String password, String picture){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    public long getId() { return id;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getPassword() {return password;}
    public void setPassword(String password) {
        this.password = password;
    }
    public String getLastName() { return lastName;}
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPicture() {return picture;}
    public void setPicture(String picture) {this.picture = picture;}
    public Set<Account> getAccount(){
        return accounts;
    }
    public void setAccount(Set<Account> accounts){ this.accounts = accounts;}
    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
    @JsonIgnore
    public Set<Loan> getLoans() {
        return clientLoans.stream().map(sub -> sub.getLoan()).collect(toSet());
    }
    public Set<ClientLoan> getClientLoans() { return clientLoans;}
    public Set<Card> getCards() { return cards;}
    public void setCard(Set<Card> cards){this.cards = cards;}
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }
}
