package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static com.mindhub.homebanking.models.AccountType.*;
import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEnconder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {
			//Client
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEnconder.encode("123melba"), "");
			Client pepe = new Client("Pepe", "Strudel", "pestru@mindhub.com", passwordEnconder.encode("123pepe"), "");
			Client teresa = new Client("Teresa", "Huix", "admin@mindhub.com", passwordEnconder.encode("123"), "");

			clientRepository.save(melba);
			clientRepository.save(pepe);
			clientRepository.save(teresa);


			//Account
			Account one = new Account("VIN001", LocalDateTime.now(), 5000.0, "USD", CURRENT, false);
			Account two = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.0, "USD",SAVING, false);
			Account four = new Account("VIN004", LocalDateTime.now().plusDays(1), 9000.0, "USD",SAVING, true);
			Account three = new Account("VIN003", LocalDateTime.now().plusDays(1), 7500.0, "USD",CURRENT, false);

			melba.addAccount(one);
			melba.addAccount(two);
			melba.addAccount(four);
			pepe.addAccount(three);

			accountRepository.save(one);
			accountRepository.save(two);
			accountRepository.save(three);
			accountRepository.save(four);


			//Transaction
			Transactions oneT = new Transactions(90.67, 5382.99, "Wallmart", LocalDateTime.now().minusWeeks(3), TransactionType.CREDIT);
			Transactions twoT = new Transactions(780.34, 4600.00,"RuPaul Nails", LocalDateTime.now().minusWeeks(2), TransactionType.DEBIT);
			Transactions threeT = new Transactions(500.99, 5102.00,"H&M", LocalDateTime.now().minusDays(5), TransactionType.CREDIT);
			Transactions fourT = new Transactions(2.10, 5100.00,"Cereals", LocalDateTime.now().minusDays(5), TransactionType.DEBIT);
			Transactions fiveT = new Transactions(20.90, 5080.00,"Fish and Chips", LocalDateTime.now().minusDays(4), TransactionType.DEBIT);
			Transactions sixT = new Transactions(480.30, 5560.00,"Boots", LocalDateTime.now().minusDays(3), TransactionType.CREDIT);
			Transactions sevenT = new Transactions(80.20, 5480.00,"Breakfast", LocalDateTime.now().minusDays(3), TransactionType.DEBIT);
			Transactions eigthT = new Transactions(250.70, 5230.00, "ZARA", LocalDateTime.now().minusDays(1), TransactionType.DEBIT);
			Transactions nineT = new Transactions(40.24, 5270.00,  "Pillow", LocalDateTime.now(), TransactionType.CREDIT);
			Transactions tenT = new Transactions(900.76, 6180.00, "Fly tickets", LocalDateTime.now(), TransactionType.CREDIT);
			Transactions elevenT = new Transactions(1180.63, 5000.00, "Ferniture", LocalDateTime.now(), TransactionType.DEBIT);

			one.addTransaction(oneT);
			one.addTransaction(twoT);
			one.addTransaction(threeT);
			one.addTransaction(fourT);
			one.addTransaction(fiveT);
			one.addTransaction(sixT);
			one.addTransaction(sevenT);
			one.addTransaction(eigthT);
			one.addTransaction(nineT);
			one.addTransaction(tenT);
			one.addTransaction(elevenT);

			transactionRepository.save(oneT);
			transactionRepository.save(twoT);
			transactionRepository.save(threeT);
			transactionRepository.save(fourT);
			transactionRepository.save(fiveT);
			transactionRepository.save(sixT);
			transactionRepository.save(sevenT);
			transactionRepository.save(eigthT);
			transactionRepository.save(nineT);
			transactionRepository.save(tenT);
			transactionRepository.save(elevenT);


			//Loan
			Loan mortgage = new Loan("MORTGAGE", 500000, 8.15 , List.of(12,24,36,48,60), "Mortgage loans are used to buy a home or to borrow money against the value of a home you already own.");
			Loan personal = new Loan("PERSONAL", 100000, 15, List.of(6,12,24), "A personal loan is an amount of money you can borrow to use for a variety of purposes.");
			Loan auto = new Loan("AUTO", 300000, 9.50, List.of(6,12,24,36), "An auto loan allows you to borrow money from a lender and use that money to purchase a car.");

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(auto);

			//ClientLoan
			ClientLoan oneCL = new ClientLoan( 400000.00, 400000.00*20/100,60, melba, mortgage);
			ClientLoan twoCL = new ClientLoan( 50000.00, 50000.00*20/100,12, melba, personal);
			ClientLoan threeCL = new ClientLoan( 100000.00,100000.00*20/100, 24, pepe, personal);
			ClientLoan fourCL = new ClientLoan( 200000.00,200000.00*20/100 ,36, pepe, auto);

			clientLoanRepository.save(oneCL);
			clientLoanRepository.save(twoCL);
			clientLoanRepository.save(threeCL);
			clientLoanRepository.save(fourCL);

			//Cards
			Card card1 = new Card(melba, DEBIT, GOLD,this.number() + "-" + this.number() + "-" + this.number() + "-" + this.number(),  numberCvv(), LocalDate.now().minusYears(5), LocalDate.now().minusDays(1), false);
			Card card2 = new Card(melba, CREDIT, GOLD,this.number() + "-" + this.number() + "-" + this.number() + "-" + this.number(), numberCvv(), LocalDate.now(), LocalDate.now().plusYears(5), false);
			Card card3 = new Card(pepe, CREDIT, SILVER,this.number() + "-" + this.number() + "-" + this.number() + "-" + this.number(), numberCvv(), LocalDate.now(), LocalDate.now().plusYears(5), false);
			Card card4 = new Card(melba, DEBIT, SILVER,this.number() + "-" + this.number() + "-" + this.number() + "-" + this.number(),  numberCvv(), LocalDate.now(), LocalDate.now().plusYears(5), false);
			Card card5 = new Card(melba, DEBIT, TITANIUM,this.number() + "-" + this.number() + "-" + this.number() + "-" + this.number(),  numberCvv(), LocalDate.now(), LocalDate.now().plusYears(5), false);
			Card card6 = new Card(melba, CREDIT, SILVER,this.number() + "-" + this.number() + "-" + this.number() + "-" + this.number(), numberCvv(), LocalDate.now(), LocalDate.now().plusYears(5), false);

			melba.addCard(card1);
			melba.addCard(card2);
			melba.addCard(card6);
			pepe.addCard(card3);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);
			cardRepository.save(card5);
			cardRepository.save(card6);
		};

	}
	public int number(){
		return (int)(Math.random()*9999+1);
	}
	public int numberCvv(){
		return (int)(Math.random()*999+1);
	}

}
