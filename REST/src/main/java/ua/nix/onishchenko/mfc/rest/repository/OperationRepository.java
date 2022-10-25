package ua.nix.onishchenko.mfc.rest.repository;

import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.Operation;

import java.util.UUID;

public interface OperationRepository extends Repository<Operation> {

    long countByAccount(Account account);

    long countByAccountId(UUID id);

}
