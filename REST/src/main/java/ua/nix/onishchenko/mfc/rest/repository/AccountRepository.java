package ua.nix.onishchenko.mfc.rest.repository;

import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.User;

import java.util.Set;
import java.util.UUID;

public interface AccountRepository extends Repository<Account> {

    Set<Account> getAccountsByHolderId(UUID holderId);

    Set<Account> getAccountsByHolder(User user);

}