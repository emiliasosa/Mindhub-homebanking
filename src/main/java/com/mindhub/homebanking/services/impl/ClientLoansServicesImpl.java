package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoansServicesImpl implements ClientLoanServices {
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void save(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
