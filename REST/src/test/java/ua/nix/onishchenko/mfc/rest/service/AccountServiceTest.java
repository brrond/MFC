package ua.nix.onishchenko.mfc.rest.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.AccountService;
import ua.nix.onishchenko.mfc.rest.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ua.nix.onishchenko.mfc.rest.RESTApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    private static AccountService accountService;

    private static UserService userService;

    private static final List<Account> accounts = new ArrayList<>();

    private static User USER;

    @Test
    @Order(1)
    public void save_Saves2Accounts_NoExceptionThrow() {
        // load necessary beans
        accountService = applicationContext.getBean(AccountService.class);
        userService = applicationContext.getBean(UserService.class);
        assertNotNull(accountService);
        assertNotNull(userService);

        // create new user
        assertDoesNotThrow(() -> {
            User user = new User();
            user.setName("Some Random Dude#1");
            user.setPassword("qwerty");
            user.setEmail("My123@gmail.com");
            USER = userService.save(user);
        });

        // create two accounts
        assertDoesNotThrow(() -> {
            Account acc = new Account();
            acc.setTitle("First account");
            acc.setHolder(USER);
            accountService.save(acc);
            accounts.add(acc);

            acc = new Account();
            acc.setTitle("Second account");
            acc.setHolder(USER);
            accountService.save(acc);
            accounts.add(acc);
        });
    }

    @Test
    @Order(2)
    public void findById_TriesToFindAccountByGivenId_AccountIsPresent() {
        Optional<Account> account = accountService.findById(accounts.get(0).getId());
        assertTrue(account.isPresent());
    }

    @Test
    @Order(3)
    public void deleteById_TriesToDeleteAccountWithSpecificId_IdIsPresent() {
        assertDoesNotThrow(() -> accountService.deleteById(accounts.get(1).getId()));
        accounts.remove(1);
    }

    @Test
    @Order(4)
    public void delete_DeletesAllPreviouslyCreatedAccounts() {
        assertDoesNotThrow(() -> accounts.forEach(acc -> accountService.delete(acc)));
        assertDoesNotThrow(() -> userService.delete(USER));
    }

}
