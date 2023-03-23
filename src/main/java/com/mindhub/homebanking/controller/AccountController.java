package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
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
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.AccountType.CURRENT;
import static com.mindhub.homebanking.models.AccountType.SAVING;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountServices accountServices;
    @Autowired
    private ClientServices clientServices;
    @Autowired
    private TransactionServices transactionServices;

    @GetMapping("/account")
    public List<AccountDTO> getAccounts(){
        return accountServices.findAll().stream().filter(account -> account.getDisable() == false).map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/account/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return new AccountDTO(accountServices.findById(id));
    }

    @GetMapping("/clients/current/accounts/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());
        Account account = accountServices.findById(id);
        if(account == null || client.getId() != account.getClient().getId()){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }

    @PostMapping("/clients/current/accountsNewUser")
    public ResponseEntity<Object>  addAccountNewClient(Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());

        if (client.getAccount().stream().filter(account -> account.getDisable() == false).collect(toList()).size() ==  3) {
            return new ResponseEntity<>("Maximum capacity of accounts, you cannot have more.", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account(this.number(), LocalDateTime.now(), 0, "USD", CURRENT, false);

        client.addAccount(newAccount);
        accountServices.save(newAccount);
        return new ResponseEntity<>("New account created successful.",HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object>  addAccount(Authentication authentication, @RequestParam AccountType type) {
        Client client = clientServices.findByEmail(authentication.getName());

        if (client.getAccount().stream().filter(account -> account.getDisable() == false).collect(toList()).size() ==  3) {
            return new ResponseEntity<>("Maximum capacity of accounts, you cannot have more.", HttpStatus.FORBIDDEN);
        }else if(type == null){
           return new ResponseEntity<>("You need to select a type of account to continued.", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account(this.number(), LocalDateTime.now(), 0, "USD", type, false);

        client.addAccount(newAccount);
        accountServices.save(newAccount);
        return new ResponseEntity<>("New account created successful.",HttpStatus.CREATED);
    }

    private String number(){
        String finalNumber;
        do{
            finalNumber = "VIN" + (int)(Math.random()*99999999+1);
        }while(accountServices.findByNumber(finalNumber) != null);

        return finalNumber;
    }

    @PatchMapping("/clients/current/account/delete")
    public ResponseEntity<Object> delete(Authentication authentication,@RequestParam boolean disable, @RequestParam long id) {
        Account deleteAccount = accountServices.findById(id);

        if(deleteAccount.getBalance() > 0){
            return new ResponseEntity<>("You want to delete an account with money. Send the money to another account.", HttpStatus.FORBIDDEN);
        }
        
        deleteAccount.setDisable(disable);

        accountServices.save(deleteAccount);
        return new ResponseEntity<>("Account successfully deleted.", HttpStatus.OK);
    }

    @PostMapping("/clients/current/account/exchange")
    public ResponseEntity<Object> exchange(Authentication authentication, @RequestParam long id, @RequestParam @Nullable String typeOfMoney, @RequestParam @Nullable  Double amountExchange,
                                           @RequestParam @Nullable Double amount){
        Client client = clientServices.findByEmail(authentication.getName());
        Account account = accountServices.findById(id);
        int compareConditionTypeOfMoney = client.getAccount().stream().filter(account1 -> !account1.getDisable() && account1.getTypeOfMoney().equals(typeOfMoney)).toArray().length;
        int compareConditionDisable = client.getAccount().stream().filter(account1 -> !account1.getDisable()).toArray().length;

        if(amount == null || amountExchange == null) {
            return new ResponseEntity<>("You need complete the amount field.", HttpStatus.FORBIDDEN);
        }else if(amount <= 0) {
            return new ResponseEntity<>("You need to send more than $0.", HttpStatus.FORBIDDEN);
        }else if(account.getBalance() == 0 || account.getBalance() < amount){
            return new ResponseEntity<>("You don't have enough money to exchange.", HttpStatus.FORBIDDEN);
        }else if(typeOfMoney.isEmpty()){
            return new ResponseEntity<>("You need to select a type of change.", HttpStatus.FORBIDDEN);
        }else if(compareConditionTypeOfMoney == 0 && compareConditionDisable == 3){
            return new ResponseEntity<>("You don't have a type of account of the type of money you want.", HttpStatus.FORBIDDEN);
        }else if(compareConditionTypeOfMoney == 0 && compareConditionDisable < 3){
            String numberNewAccount = this.number();
            Account createAccount = new Account(numberNewAccount, LocalDateTime.now(), amountExchange, typeOfMoney, SAVING, false);
            client.addAccount(createAccount);
            accountServices.save(createAccount);

            Account newAccount = accountServices.findByNumber(numberNewAccount);

            String ownDescription = ("Exchange ").toUpperCase() + typeOfMoney + " - " + newAccount.getNumber();
            String sendDescription = ("Exchange ").toUpperCase() + typeOfMoney + " - " + account.getNumber();

            double amountFinalOriginAccount = account.getBalance() - amount;

            Transactions newOwnTransaction = new Transactions(amount, amountFinalOriginAccount,ownDescription, LocalDateTime.now(), DEBIT);
            Transactions newSendTransaction = new Transactions(amountExchange, amountExchange,sendDescription, LocalDateTime.now(), CREDIT);

            account.setBalance(amountFinalOriginAccount);

            newAccount.addTransaction(newSendTransaction);
            account.addTransaction(newOwnTransaction);

            accountServices.save(account);
            transactionServices.save(newSendTransaction);
            transactionServices.save(newOwnTransaction);

            return  new ResponseEntity<>("You made an exchange and create a new account.", HttpStatus.OK);
        }else if(compareConditionTypeOfMoney != 0 && compareConditionDisable <= 3){

            Account addAccount = accountServices.findByTypeOfMoney(typeOfMoney);

            String ownDescription = ("Exchange ").toUpperCase() + typeOfMoney + " - " + addAccount.getNumber();
            String sendDescription = ("Exchange ").toUpperCase() + typeOfMoney + " - " + account.getNumber();

            double amountFinalOriginAccount = account.getBalance() - amount;
            double addMoney = addAccount.getBalance() + amountExchange;

            Transactions newOwnTransaction = new Transactions(amount, amountFinalOriginAccount,ownDescription, LocalDateTime.now(), DEBIT);
            Transactions newSendTransaction = new Transactions(amountExchange, addMoney, sendDescription, LocalDateTime.now(), CREDIT);

            account.setBalance(amountFinalOriginAccount);
            addAccount.setBalance(addMoney);

            addAccount.addTransaction(newSendTransaction);
            account.addTransaction(newOwnTransaction);
            accountServices.save(addAccount);
            accountServices.save(account);
            transactionServices.save(newSendTransaction);
            transactionServices.save(newOwnTransaction);
            return  new ResponseEntity<>("You made an exchange you can see the amount exchanged in the account " + addAccount.getNumber(), HttpStatus.OK);
        }

        return  new ResponseEntity<>("You made an exchange.", HttpStatus.OK);
    }
}
