package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;
    private String firstName,  lastName, email, picture;
    private Set<AccountDTO> accounts;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.picture = client.getPicture();
        this.accounts = client.getAccount().stream().map(account -> new AccountDTO(account)).collect(toSet());
        this.loans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toList());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    public long getId() {
        return id;
    }
    public Set<AccountDTO>  getAccount() {
        return accounts;
    }
    public String getFirstName() {return firstName;}
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPicture() {return picture;}
    public List<ClientLoanDTO> getLoans() {return loans;}
    public List<CardDTO> getCards() {return cards;}
}
