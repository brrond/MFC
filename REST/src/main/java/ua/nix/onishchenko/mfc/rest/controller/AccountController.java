package ua.nix.onishchenko.mfc.rest.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.rest.dto.OperationDTO;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.AccountService;
import ua.nix.onishchenko.mfc.rest.service.UserService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CommonsLog
@RestController
@RequestMapping("api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping(path="s/getGeneralInfo")
    public Map<String, Object> getGeneralInfo(@RequestParam("accountId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<Account> optionalAccount = accountService.findById(id);
        if (optionalAccount.isPresent()) {
            log.debug("UUID id = " + id + " present");
            return ControllerUtils.getMap("account", optionalAccount.get());
        }
        return ControllerUtils.error("account is not presented");
    }

    @GetMapping(path="s/getAllOperations")
    public Set<OperationDTO> getAllOperations(@RequestParam("accountId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<Account> optionalAccount = accountService.findById(id);
        if (optionalAccount.isPresent()) {
            log.debug("UUID id = " + id + " present");
            return optionalAccount.get().getOperations().parallelStream().map(OperationDTO::new).collect(Collectors.toSet());
        }
        return Set.of();
    }

    @PostMapping(path="s/createAccount")
    public Map<String, Object> createAccount(@RequestAttribute("userId") UUID id, @RequestParam("title") String title) {
        if (!title.matches(ControllerUtils.getAccountTitleRegex())) {
            return ControllerUtils.error("Title doesn't match regex " + ControllerUtils.getAccountTitleRegex());
        }

        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " present");
            Account account = new Account();
            account.setHolder(optionalUser.get());
            account.setTitle(title);

            try {
                account = accountService.save(account);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.debug(e);
                return ControllerUtils.error(e.getMessage());
            }

            log.debug("UUID id of account is " + account.getId() + " present");
            return ControllerUtils.getMap(account);
        }
        return ControllerUtils.error("User is not presented");
    }

    @PutMapping(path="s/updateAccount")
    public Map<String, Object> updateAccount(@RequestParam("accountId") UUID id, @RequestParam("title") String title) {
        log.debug("UUID id = " + id + ", title = " + title);
        Optional<Account> accountOptional = accountService.findById(id);
        if (accountOptional.isEmpty()) {
            return ControllerUtils.error("account is not presented");
        }

        if (!title.matches(ControllerUtils.getAccountTitleRegex())) {
            return ControllerUtils.error("Title doesn't match regex " + ControllerUtils.getAccountTitleRegex());
        }

        Account account = accountOptional.get();
        account.setTitle(title);

        try {
            account = accountService.save(account);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }
        return ControllerUtils.getMap(account.getId());
    }

    @DeleteMapping(path="s/deleteAccount")
    public Map<String, Object> deleteAccount(@RequestParam("accountId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<Account> optionalAccount = accountService.findById(id);
        if (optionalAccount.isEmpty()) {
            return ControllerUtils.error("account is not presented");
        }

        Account account = optionalAccount.get();
        try {
            accountService.delete(account);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }
        return ControllerUtils.getMap(account.getId());
    }

}
