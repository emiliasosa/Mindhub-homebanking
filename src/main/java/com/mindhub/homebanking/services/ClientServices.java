package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClientServices {
    List<Client> findAll();
    Client findById(Long id);
    Client findByEmail(String email);

    void save(Client client);
}
