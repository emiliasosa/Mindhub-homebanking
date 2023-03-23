package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientLoanServices clientLoanServices;
    @Autowired
    private ClientServices clientServices;
    @Autowired
    private LoanServices loanServices;
    @Autowired
    private AccountServices accountServices;
    @Autowired
    private TransactionServices transactionServices;

    @GetMapping("/clients/current/typeOfloan")
    public List<LoanDTO> getLoans() {
        return loanServices.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }

    @Transactional
    @PostMapping("/clients/current/loan")
    public ResponseEntity<Object> addLoan(Authentication authentication, @RequestBody @Nullable LoanApplicationDTO loanApplicationDTO) {
        Client client = clientServices.findByEmail(authentication.getName());
        Account account = accountServices.findByNumber(loanApplicationDTO.getDestinationAccount());
        Loan loan =  loanServices.findByName(loanApplicationDTO.getName());

        if(loanApplicationDTO.getName().isEmpty()) {
            return new ResponseEntity<>("The loan doesn't exist.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getAmount() == null) {
            return new ResponseEntity<>("Complete the amount field.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getAmount() < 0) {
            return new ResponseEntity<>("You need to request more than $0.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getPayment() == null) {
            return new ResponseEntity<>("You need to select a payment.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getPayment() <= 0){
            return new ResponseEntity<>("You need to set payments greater than 0.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getDestinationAccount().isEmpty()) {
            return new ResponseEntity<>("You need to add an existent account", HttpStatus.FORBIDDEN);
        }else if(loan == null){
            return new ResponseEntity<>("That loan doesn't exist.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The amount you want exceeds the maximum amount of the requested loan.", HttpStatus.FORBIDDEN);
        }else if(loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("You can't send negative or 0 amount.", HttpStatus.FORBIDDEN);
        }else if(!loan.getPayments().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("The payment does not match the payment options of the loan", HttpStatus.FORBIDDEN);
        }else if(account == null){
            return new ResponseEntity<>("The account doesn't exist.", HttpStatus.FORBIDDEN);
        }else if(!client.getAccount().stream().anyMatch(number -> number.getNumber().equals(loanApplicationDTO.getDestinationAccount()))){
            return new ResponseEntity<>("The account doesn't yours.", HttpStatus.FORBIDDEN);
        }else if(client.getLoans().stream().anyMatch(loans -> loans.getId() == loanApplicationDTO.getLoanId())){
            return new ResponseEntity<>("You already have that type of loan.", HttpStatus.FORBIDDEN);
        }

        double finalAmountRequestedTransaction = loanApplicationDTO.getAmount() + (loan.getInterest() * loanApplicationDTO.getAmount() / 100);
        String descriptionTransaction = loan.getName() + " - loan approved";
        double finalAmountAccount = account.getBalance() + loanApplicationDTO.getAmount();

        Transactions addLoan = new Transactions(loanApplicationDTO.getAmount(), finalAmountAccount, descriptionTransaction.toUpperCase(), LocalDateTime.now(), CREDIT);

        account.setBalance(finalAmountAccount);
        account.addTransaction(addLoan);

        transactionServices.save(addLoan);
        clientLoanServices.save(new ClientLoan(loanApplicationDTO.getAmount(),finalAmountRequestedTransaction, loanApplicationDTO.getPayment(), client, loan));
        return new ResponseEntity<>("Congratulations, your loan was accepted",HttpStatus.CREATED);
    }

    @PostMapping("/admin/create-loan")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestParam String name,
                                             @RequestParam @Nullable Double maxAmount, @RequestParam @Nullable List<Integer> payments,
                                             @RequestParam @Nullable Double interest, @RequestParam String description) {
        String nameLoan = name.toUpperCase();

        if(name.isEmpty() ||maxAmount == null || interest == null ||  payments.size() == 0 || description.isEmpty()){
            return new ResponseEntity<>("You need to complete all the fields.", HttpStatus.FORBIDDEN);
        }else if(maxAmount <= 0 || interest <= 0){
            return new ResponseEntity<>("You need to complete with more than $0.", HttpStatus.FORBIDDEN);
        }else if(payments.stream().anyMatch(pay -> pay < 0)){
            return new ResponseEntity<>("You need to set payments greater than 0.", HttpStatus.FORBIDDEN);
        }else if(loanServices.findByName(nameLoan) != null){
            return new ResponseEntity<>("The name requested is alredy in use.", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(nameLoan, maxAmount, interest, payments, description);

        loanServices.save(loan);
        return new ResponseEntity<>(nameLoan + " loan created.",HttpStatus.CREATED);
    }
}
