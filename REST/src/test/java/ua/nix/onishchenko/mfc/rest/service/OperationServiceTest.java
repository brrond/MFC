package ua.nix.onishchenko.mfc.rest.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.Operation;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.AccountService;
import ua.nix.onishchenko.mfc.rest.service.OperationService;
import ua.nix.onishchenko.mfc.rest.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = {ua.nix.onishchenko.mfc.rest.RESTApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OperationServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    private static OperationService operationService;

    private static AccountService accountService;

    private static UserService userService;

    private static final List<Operation> operations = new ArrayList<>();

    private static User USER;

    private static Account ACCOUNT;

    @Test
    @Order(1)
    public void save_Saves2Operations_NoExceptionThrow() {
        // load necessary beans
        operationService = applicationContext.getBean(OperationService.class);
        accountService = applicationContext.getBean(AccountService.class);
        userService = applicationContext.getBean(UserService.class);
        assertNotNull(operationService);
        assertNotNull(userService);

        // create new user
        assertDoesNotThrow(() -> {
            User user = new User();
            user.setName("Some Random Dude#1");
            user.setPassword("qwerty");
            user.setEmail("My123@gmail.com");
            USER = userService.save(user);
        });

        // create new account
        assertDoesNotThrow(() -> {
            Account account = new Account();
            account.setTitle("My Account");
            account.setHolder(USER);
            ACCOUNT = accountService.save(account);
        });

        // create two operations
        assertDoesNotThrow(() -> {
            Operation operation = new Operation();
            operation.setSum(BigDecimal.valueOf(100));
            operation.setAccount(ACCOUNT);
            operations.add(operationService.save(operation));

            operation = new Operation();
            operation.setSum(BigDecimal.valueOf(250));
            operation.setAccount(ACCOUNT);
            operations.add(operationService.save(operation));
        });
    }

    @Test
    @Order(2)
    public void checkIfAccountsBalanceIsChanged() {
        var optional = accountService.findById(ACCOUNT.getId());
        assertTrue(optional.isPresent());

        ACCOUNT = optional.get();
        assertEquals(BigDecimal.valueOf(350).add(BigDecimal.valueOf(0.01)),
                ACCOUNT.getBalance().add(BigDecimal.valueOf(0.01)));
    }

    @Test
    @Order(3)
    public void findById_TriesToFindOperationByGivenId_OperationTypeIsPresent() {
        Optional<Operation> operation = operationService.findById(operations.get(0).getId());
        assertTrue(operation.isPresent());
    }

    @Test
    @Order(4)
    public void deleteById_TriesToDeleteAccountWithSpecificId_IdIsPresent() {
        assertDoesNotThrow(() -> operationService.deleteById(operations.get(1).getId()));
        operations.remove(1);
    }

    @Test
    @Order(5)
    public void checkIfAccountsBalanceIsChanged_2() {
        var optional = accountService.findById(ACCOUNT.getId());
        assertTrue(optional.isPresent());

        ACCOUNT = optional.get();
        assertEquals(BigDecimal.valueOf(100.).add(BigDecimal.valueOf(0.01)),
                ACCOUNT.getBalance().add(BigDecimal.valueOf(0.01)));
    }

    @Test
    @Order(6)
    public void delete_DeletesAll() {
        assertDoesNotThrow(() -> operationService.delete(operations.get(0)));
        assertDoesNotThrow(() -> accountService.delete(ACCOUNT));
        assertDoesNotThrow(() -> userService.delete(USER));
    }

}
