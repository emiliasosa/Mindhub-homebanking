package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Transactions;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServicesImpl implements TransactionServices {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void save(Transactions transaction) {
        transactionRepository.save(transaction);
    }
}
