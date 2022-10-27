package ua.nix.onishchenko.mfc.rest.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.rest.dto.OperationDTO;
import ua.nix.onishchenko.mfc.rest.entity.OperationType;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.OperationTypeService;
import ua.nix.onishchenko.mfc.rest.service.UserService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CommonsLog
@RestController
@RequestMapping("api/operationtypes")
public class OperationTypeController {

    @Autowired
    private OperationTypeService operationTypeService;

    @Autowired
    private UserService userService;

    @GetMapping(path="s/getGeneralInfo")
    public Map<String, Object> getGeneralInfo(@RequestParam("operationTypeId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<OperationType> optionalOperationType = operationTypeService.findById(id);
        if (optionalOperationType.isPresent()) {
            log.debug("UUID id = " + id + " present");
            return ControllerUtils.getMap("operationType", optionalOperationType.get());
        }
        return ControllerUtils.error("Account is not presented");
    }

    @GetMapping(path="s/getAllOperations")
    public Set<OperationDTO> getAllOperations(@RequestParam("operationTypeId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<OperationType> optionalOperationType = operationTypeService.findById(id);
        if (optionalOperationType.isPresent()) {
            log.debug("UUID id = " + id + " present");
            return optionalOperationType.get().getOperations().parallelStream().map(OperationDTO::new).collect(Collectors.toSet());
        }
        return Set.of();
    }

    @PostMapping(path="s/createOperationType")
    public Map<String, Object> createOperationType(@RequestAttribute("userId") UUID id,
                                                   @RequestParam("title") String title) {
        if (!title.matches(ControllerUtils.getOperationTypeTitleRegex())) {
            log.debug("Title doesn't match regex");
            return ControllerUtils.error("Title doesn't match regex " + ControllerUtils.getOperationTypeTitleRegex());
        }

        log.debug("User UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("User UUID id = " + id + " present");
            OperationType operationType = new OperationType();
            operationType.setCreator(optionalUser.get());
            operationType.setTitle(title);

            operationType = operationTypeService.save(operationType);
            log.debug("UUID id of account is " + operationType.getId() + " present");
            return ControllerUtils.getMap(operationType.getId());
        }
        return ControllerUtils.error("User is not presented");
    }

    @PutMapping(path="s/updateOperationType")
    public Map<String, Object> updateOperationType(@RequestParam("operationTypeId") UUID id,
                                                   @RequestParam("title") String title) {
        log.debug("UUID id = " + id + ", title = " + title);
        Optional<OperationType> operationTypeOptional = operationTypeService.findById(id);
        if (operationTypeOptional.isEmpty()) {
            return ControllerUtils.error("OperationType is not presented");
        }

        if (!title.matches(ControllerUtils.getOperationTypeTitleRegex())) {
            return ControllerUtils.error("Title doesn't match regex " + ControllerUtils.getOperationTypeTitleRegex());
        }

        OperationType operationType = operationTypeOptional.get();
        operationType.setTitle(title);

        try {
            operationType = operationTypeService.save(operationType);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }
        return ControllerUtils.getMap(operationType.getId());
    }

    @DeleteMapping(path="s/deleteOperationType")
    public Map<String, Object> deleteOperationType(@RequestParam("operationTypeId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<OperationType> operationTypeOptional = operationTypeService.findById(id);
        if (operationTypeOptional.isEmpty()) {
            return ControllerUtils.error("account is not presented");
        }

        OperationType operationType = operationTypeOptional.get();
        try {
            operationTypeService.delete(operationType);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }
        return ControllerUtils.getMap(operationType);
    }

}
