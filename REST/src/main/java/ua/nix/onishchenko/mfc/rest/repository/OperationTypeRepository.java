package ua.nix.onishchenko.mfc.rest.repository;

import ua.nix.onishchenko.mfc.rest.entity.OperationType;
import ua.nix.onishchenko.mfc.rest.entity.User;

import java.util.Set;
import java.util.UUID;

public interface OperationTypeRepository extends Repository<OperationType> {

    Set<OperationType> getOperationTypesByCreator(User user);

    Set<OperationType> getOperationTypesByCreatorId(UUID id);

}
