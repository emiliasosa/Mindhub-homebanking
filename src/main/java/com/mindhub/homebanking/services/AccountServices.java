package com.mindhub.homebanking.services;
import com.mindhub.homebanking.models.Account;
import java.util.List;

public interface AccountServices {
    List<Account> findAll();
    Account findById(Long id);
    Account findByNumber(String number);
    Account findByTypeOfMoney(String type);
    void save(Account account);
}
