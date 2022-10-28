package ua.nix.onishchenko.mfc.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.nix.onishchenko.mfc.rest.entity.Operation;
import ua.nix.onishchenko.mfc.rest.repository.OperationRepository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class OperationService implements Service<Operation> {

    @Autowired
    private OperationRepository operationRepository;

    @Override
    public Optional<Operation> findById(UUID id) {
        return operationRepository.findById(id);
    }

    @Override
    public Operation save(Operation entity) {
        return operationRepository.save(entity);
    }

    @Override
    public void delete(Operation entity) {
        operationRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        operationRepository.deleteById(id);
    }

}
