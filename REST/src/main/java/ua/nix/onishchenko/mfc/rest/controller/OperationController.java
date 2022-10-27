package ua.nix.onishchenko.mfc.rest.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.Operation;
import ua.nix.onishchenko.mfc.rest.entity.OperationType;
import ua.nix.onishchenko.mfc.rest.service.AccountService;
import ua.nix.onishchenko.mfc.rest.service.OperationService;
import ua.nix.onishchenko.mfc.rest.service.OperationTypeService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// TODO: Create new filter that checks if authorized user has such access
//      and for second method if he has such Operation on one of his accounts

@CommonsLog
@RestController
@RequestMapping("api/operations")
public class OperationController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationTypeService operationTypeService;

    @Autowired
    private OperationService operationService;

    @PostMapping(path="s/createOperation")
    public Map<String, Object> createOperation(@RequestParam("accountId") UUID accountId,
                                               @RequestParam("sum") BigDecimal sum,
                                               @RequestParam(value = "operationTypeId", required = false) UUID operationTypeId,
                                               @RequestParam(value = "localDateTime", required = false) String localDateTime) {
        log.debug("accountId: " + accountId);

        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("Sum is zero");
            return ControllerUtils.error("Sum is zero");
        }

        Optional<Account> accountOptional = accountService.findById(accountId);
        Optional<OperationType> operationTypeOptional = Optional.empty();
        if (operationTypeId != null) {
             operationTypeOptional = operationTypeService.findById(operationTypeId);
        } else {
            log.debug("OperationType provided: false");
        }

        if (accountOptional.isEmpty()) {
            log.warn("Account isn't found");
            return ControllerUtils.error("Account isn't found");
        }

        Operation operation = new Operation();
        operation.setAccount(accountOptional.get());
        operation.setSum(sum);
        operationTypeOptional.ifPresent(operation::setType);
        if (localDateTime != null) operation.setCreation(LocalDateTime.parse(localDateTime).toInstant(ZoneOffset.UTC));
        try {
            operation = operationService.save(operation);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }
        return ControllerUtils.getMap(operation);
    }

    @DeleteMapping(path="s/deleteOperation")
    public Map<String, Object> deleteOperation(@RequestParam("operationId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<Operation> operationOptional = operationService.findById(id);
        if (operationOptional.isEmpty()) {
            log.warn("Operation UUID isn't found");
            return ControllerUtils.error("Operation UUID isn't found");
        }

        Operation operation = operationOptional.get();
        try {
            operationService.delete(operation);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }

        return ControllerUtils.getMap(operation);
    }

}
