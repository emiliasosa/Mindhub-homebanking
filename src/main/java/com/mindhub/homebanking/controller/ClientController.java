package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientServices clientServices;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientServices.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return  new ClientDTO(clientServices.findById(id));
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());
        Set<Account> accountFinal = client.getAccount().stream().filter(account -> !account.getDisable()).collect(toSet());
        Set<Card> cardFinal = client.getCards().stream().filter(card -> !card.getDisable()).collect(toSet());
        client.setAccount(accountFinal);
        client.setCard(cardFinal);
        return  new ClientDTO(client);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password, @RequestParam String picture) {

            if (firstName.isEmpty()) {
                return new ResponseEntity<>("Missing firstname.", HttpStatus.FORBIDDEN);
            }else if (lastName.isEmpty()) {
                return new ResponseEntity<>("Missing lastname.", HttpStatus.FORBIDDEN);
            }else if (email.isEmpty()) {
                return new ResponseEntity<>("Missing email.", HttpStatus.FORBIDDEN);
            }else if (password.isEmpty()) {
                return new ResponseEntity<>("Missing password.", HttpStatus.FORBIDDEN);
            }else if (clientServices.findByEmail(email) !=  null) {
                return new ResponseEntity<>("Email already in use.", HttpStatus.FORBIDDEN);
            }

            clientServices.save(new Client(firstName, lastName, email, passwordEncoder.encode(password), picture));
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current")
    public ResponseEntity<Object> update(Authentication authentication,
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String picture) {

        Client updateClient = clientServices.findByEmail(authentication.getName());

        updateClient.setFirstName(firstName);
        updateClient.setLastName (lastName);
        updateClient.setEmail(email);
        updateClient.setPicture(picture);

        clientServices.save(updateClient);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
