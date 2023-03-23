package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServicesImpl implements LoanServices {
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan findByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }
}
