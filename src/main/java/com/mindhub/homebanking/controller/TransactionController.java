package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transactions;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;


@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionServices transactionServices;
    @Autowired
    private ClientServices clientServices;
    @Autowired
    private AccountServices accountServices;

    @Transactional
    @PostMapping("/clients/current/transactions/{id}")
    public ResponseEntity<Object> addTransaction(Authentication authentication, @RequestParam  long id, @RequestParam @Nullable Double amount,
                                                 @RequestParam String description, @RequestParam String accountToSend) {
        Client client = clientServices.findByEmail(authentication.getName());
        Account ownAccount = accountServices.findById(id);
        Account sendAccount = accountServices.findByNumber(accountToSend.toUpperCase());

        if(ownAccount == null){
            return new ResponseEntity<>("This account doesn't exist.", HttpStatus.FORBIDDEN);
        }else if(sendAccount == null){
            return new ResponseEntity<>("The account you want to send to doesn't exist.", HttpStatus.FORBIDDEN);
        }else if(id == sendAccount.getId()){
            return new ResponseEntity<>("You can't send money to your own account.", HttpStatus.FORBIDDEN);
        }else if(!client.getAccount().stream().anyMatch(oneAccount -> oneAccount.getId() == id)){
            return new ResponseEntity<>("This account doesn't yours.", HttpStatus.FORBIDDEN);
        }else if (amount == null) {
            return new ResponseEntity<>("You need to send money.", HttpStatus.FORBIDDEN);
        }else if (amount < 1) {
            return new ResponseEntity<>("You need to send more than $0.", HttpStatus.FORBIDDEN);
        }else if(ownAccount.getBalance() < amount ){
            return new ResponseEntity<>("You don't have enough money to send.", HttpStatus.FORBIDDEN);
        }else if(description.trim().isEmpty()){
            return new ResponseEntity<>("Missing description.", HttpStatus.FORBIDDEN);
        }

        String ownDescription = description.toUpperCase() + " - " + sendAccount.getNumber();
        String sendDescription = description.toUpperCase() + " - " + ownAccount.getNumber();

        double modifyOwnBalance = ownAccount.getBalance() - amount;
        double modifySendBalance = sendAccount.getBalance() + amount;

        Transactions newOwnTransaction = new Transactions(amount, modifyOwnBalance, ownDescription, LocalDateTime.now(), DEBIT);
        Transactions newSendTransaction = new Transactions(amount, modifySendBalance, sendDescription, LocalDateTime.now(), CREDIT);

        sendAccount.setBalance(modifySendBalance);
        ownAccount.setBalance(modifyOwnBalance);

        sendAccount.addTransaction(newSendTransaction);
        ownAccount.addTransaction(newOwnTransaction);

        transactionServices.save(newSendTransaction);
        transactionServices.save(newOwnTransaction);
        return new ResponseEntity<>("You made a transaction to " + sendAccount.getNumber(),HttpStatus.OK);
    }
}
