package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanServices {
    List<Loan> findAll();
    Loan findById(long id);
    Loan findByName(String name);
    void save(Loan loan);
}
