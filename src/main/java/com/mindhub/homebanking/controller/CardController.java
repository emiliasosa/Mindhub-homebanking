package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.PostnetDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.services.TransactionServices;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardServices cardServices;
    @Autowired
    private ClientServices clientServices;
    @Autowired
    private TransactionServices transactionServices;
    @Autowired
    private AccountServices accountServices;

    int cvv = CardUtils.getCvv();

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        Client client = clientServices.findByEmail(authentication.getName());

        if( client.getCards().stream().anyMatch(card -> card.getColor() == color && card.getType() == type && card.getDisable() == false)){
            return new ResponseEntity<>("You already have that card.", HttpStatus.FORBIDDEN);
        }else if(type == null){
            return new ResponseEntity<>("You need to select a type of card.", HttpStatus.BAD_REQUEST);
        }else if(color == null){
            return new ResponseEntity<>("You need to select a color of card.", HttpStatus.BAD_REQUEST);
        }
        
        Card newCard = new Card(client, type, color, getNumber(),  cvv, LocalDate.now(), LocalDate.now().plusYears(5), false);

        client.addCard(newCard);
        cardServices.save(newCard);
        return new ResponseEntity<>("New card",HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/card/delete")
    public ResponseEntity<Object> delete(Authentication authentication,@RequestParam boolean disable, @RequestParam String number) {
        Card deleteCard = cardServices.findByNumber(number);

        deleteCard.setDisable(disable);

        cardServices.save(deleteCard);
        return new ResponseEntity<>("Card successfully deleted.", HttpStatus.OK);
    }

    @PatchMapping("/clients/current/card/renew")
    public ResponseEntity<Object> renew(Authentication authentication, @RequestParam String numberRenewCard) {
        Card renewCard = cardServices.findByNumber(numberRenewCard);

        renewCard.setNumber(getNumber());
        renewCard.setFromDate(LocalDate.now());
        renewCard.setThruDate(LocalDate.now().plusYears(5));

        cardServices.save(renewCard);
        return new ResponseEntity<>("Card successfully renew.", HttpStatus.OK);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/clients/current/postnet")
    public ResponseEntity<Object> addPayment(Authentication authentication, @RequestBody @Nullable PostnetDTO postnetDTO){
        String cardNumber = postnetDTO.getNumber();
        Card card = cardServices.findByNumber(cardNumber);

        if(postnetDTO.getNumber().isEmpty() || postnetDTO.getDescription().isEmpty() || postnetDTO.getAmount() == null
                || postnetDTO.getCvv() <= 0){
            return new ResponseEntity<>("You need to complete all the fields.", HttpStatus.FORBIDDEN);
        }else if(postnetDTO.getAmount() <= 0 || postnetDTO.getCvv() <= 0){
            return new ResponseEntity<>("You can't send fields with 0 or less in it.", HttpStatus.FORBIDDEN);
        }else if(card == null){
            return new ResponseEntity<>("The card does not exist", HttpStatus.FORBIDDEN);
        }else if(!(postnetDTO.getCvv() == card.getCvv())){
            return new ResponseEntity<>("Some of the fields are wrong, check the data on your card.", HttpStatus.FORBIDDEN);
        }else if(card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("The card is expired", HttpStatus.FORBIDDEN);
        }

        Account account = card.getClient().getAccount().stream().filter(account1 -> account1.getDisable() == false).findFirst().get();

        if(account == null){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }else if(account.getBalance() < postnetDTO.getAmount()){
            return new ResponseEntity<>("You don't have enough money to pay.", HttpStatus.FORBIDDEN);
        }


        double amountFinal = account.getBalance() - postnetDTO.getAmount();
        String description = postnetDTO.getDescription().toUpperCase();

        Transactions newTransaction = new Transactions(postnetDTO.getAmount(), amountFinal, description, LocalDateTime.now(), TransactionType.DEBIT);

        account.addTransaction(newTransaction);

        account.setBalance(amountFinal);
        accountServices.save(account);

        transactionServices.save(newTransaction);
        return new ResponseEntity<>("Congratulations, your paid.",HttpStatus.CREATED);
    }

    private String getNumber(){
        String finalNumber;
        do{
            finalNumber = (int)(Math.random()*(9999 - 1000)+1000)  + "-" +(int)(Math.random()*(9999 - 1000)+1000)  + "-" + (int)(Math.random()*(9999 - 1000)+1000)  + "-" +(int)(Math.random()*(9999 - 1000)+1000);
        }while(cardServices.findByNumber(finalNumber) != null);

        return finalNumber;
    }

}
