package ua.nix.onishchenko.mfc.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.nix.onishchenko.mfc.rest.entity.OperationType;
import ua.nix.onishchenko.mfc.rest.repository.OperationTypeRepository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class OperationTypeService implements Service<OperationType> {

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Override
    public Optional<OperationType> findById(UUID id) {
        return operationTypeRepository.findById(id);
    }

    @Override
    public OperationType save(OperationType entity) {
        return operationTypeRepository.save(entity);
    }

    @Override
    public void delete(OperationType entity) {
        operationTypeRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        operationTypeRepository.deleteById(id);
    }

}
