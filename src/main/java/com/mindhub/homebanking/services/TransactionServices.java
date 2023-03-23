package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transactions;

import java.util.List;

public interface TransactionServices {
    void save(Transactions transaction);
}
