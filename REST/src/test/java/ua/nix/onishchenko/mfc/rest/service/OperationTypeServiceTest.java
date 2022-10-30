package ua.nix.onishchenko.mfc.rest.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ua.nix.onishchenko.mfc.rest.entity.OperationType;
import ua.nix.onishchenko.mfc.rest.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest(classes = {ua.nix.onishchenko.mfc.rest.RESTApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OperationTypeServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    private static OperationTypeService operationTypeService;

    private static UserService userService;

    private static final List<OperationType> types = new ArrayList<>();

    private static User USER;

    @Test
    @Order(1)
    public void save_Saves2OperationTypes_NoExceptionThrow() {
        // load necessary beans
        operationTypeService = applicationContext.getBean(OperationTypeService.class);
        userService = applicationContext.getBean(UserService.class);
        assertNotNull(operationTypeService);
        assertNotNull(userService);

        // create new user
        assertDoesNotThrow(() -> {
            User user = new User();
            user.setName("Some Random Dude#1");
            user.setPassword("qwerty");
            user.setEmail("My123@gmail.com");
            USER = userService.save(user);
        });

        // create two operations
        assertDoesNotThrow(() -> {
            OperationType operationType = new OperationType();
            operationType.setTitle("Very first operation type");
            operationType.setCreator(USER);
            types.add(operationTypeService.save(operationType));

            operationType = new OperationType();
            operationType.setTitle("Very second operation type");
            operationType.setCreator(USER);
            types.add(operationTypeService.save(operationType));
        });
    }

    @Test
    @Order(2)
    public void findById_TriesToFindOperationTypeByGivenId_OperationTypeIsPresent() {
        Optional<OperationType> operationType = operationTypeService.findById(types.get(0).getId());
        assertTrue(operationType.isPresent());
    }

    @Test
    @Order(3)
    public void deleteById_TriesToDeleteAccountWithSpecificId_IdIsPresent() {
        assertDoesNotThrow(() -> operationTypeService.deleteById(types.get(1).getId()));
        types.remove(1);
    }

    @Test
    @Order(4)
    public void delete_DeletesAllPreviouslyCreatedOperationTypes() {
        assertDoesNotThrow(() -> types.forEach(type -> operationTypeService.delete(type)));
        assertDoesNotThrow(() -> userService.delete(USER));
    }

}
