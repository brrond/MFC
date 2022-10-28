package ua.nix.onishchenko.mfc.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.repository.AccountRepository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class AccountService implements Service<Account> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(Account entity) {
        return accountRepository.save(entity);
    }

    @Override
    public void delete(Account entity) {
        accountRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }

}
