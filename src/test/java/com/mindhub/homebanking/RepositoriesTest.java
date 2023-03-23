package com.mindhub.homebanking.models;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;

    //Loan test
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("PERSONAL"))));
    }

    //Card test
    @Test
    public void existCard(){
        List<Card> cards = cardRepository.findAll();
        //assert se aseguran de algo, assertTrue el resultado sea verdadero, aasertThat asegura que, assertEqueal que los resultados sean iguales
        assertThat(cards,is(not(empty())));
    }
    @Test
    public void disableCard(){
        List<Card> cards = cardRepository.findAll();
        assertTrue(cards.stream().anyMatch(card -> card.getDisable() == false));
    }

    @Test
    public void numberNullCard(){
        Card cards = cardRepository.findAll().stream().findFirst().get();
        assertThat(cards.getNumber(), not(nullValue(String.class)));
    }

    //Account test
    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }
    @Test
    public void totalAccount(){
        int accounts = accountRepository.findAll().size();
        assertEquals(true, accounts > 1);
    }

    @Test
    public void balanceAccount(){
        Account accounts = accountRepository.findAll().stream().findFirst().get();
        assertThat((int) accounts.getBalance(), is(greaterThan(0)));
    }

    //Transaction test
    @Test
    public void existTransaction(){
        List<Transactions> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }
    @Test
    public void descriptionTransaction(){
        List<Transactions> transactions = transactionRepository.findAll();
        assertEquals(false, transactions.stream().anyMatch(transactions1 -> transactions1.getDescription() == "PEPITO"));
    }

    @Test
    public void descriptionTransaction8(){
        Transactions transactions = transactionRepository.findAll().stream().findFirst().get();
        assertThat(transactions.getBalance(), allOf(greaterThan(1000.00), lessThan(6000.00), not(equalTo(3567.00))));
    }

    //ClientLoan test
    @Test
    public void existClientLoan(){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans,is(not(empty())));
    }
    @Test
    public void greaterClientLoan(){
        ClientLoan clientLoans1 = clientLoanRepository.findAll().stream().findFirst().get();
        ClientLoan clientLoans2 = clientLoanRepository.findAll().stream().filter(clientLoan2 -> clientLoan2.getId() == 73).findFirst().get();
        assertThat(clientLoans1.getAmount(), greaterThanOrEqualTo(clientLoans2.getAmount()));
    }

}