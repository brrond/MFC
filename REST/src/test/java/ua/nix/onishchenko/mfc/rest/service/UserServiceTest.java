package ua.nix.onishchenko.mfc.rest.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import ua.nix.onishchenko.mfc.rest.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest(classes = {ua.nix.onishchenko.mfc.rest.RESTApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    private static UserService userService;

    private static final Integer N = 10;

    private static final List<User> userList = new ArrayList<>();

    @Test
    @Order(1)
    public void save_Saves10Users_NoExceptionThrow() {
        // load necessary beans
        userService = applicationContext.getBean(UserService.class);
        assertNotNull(userService);

        // create new users
        assertDoesNotThrow(() -> {
            for (int i = 0; i < N; i++) {
                User user = new User();
                user.setName("Some Random Dude#" + i);
                user.setPassword("qwerty");
                user.setEmail("My" + i + "@gmail.com");

                user = userService.save(user);
                userList.add(user);
            }
        });
    }

    @Test
    @Order(2)
    public void findByEmail_TriesToFindUserByGivenEmail_UserIsPresent() {
        String email = "My2@gmail.com";
        Optional<User> user = userService.findByEmail(email);
        assertTrue(user.isPresent());
    }

    @Test
    @Order(3)
    public void findByEmail_TriesToFindUserByGivenEmail_UserIsNOTPresent() {
        String email = "My222@gmail.com";
        Optional<User> user = userService.findByEmail(email);
        assertTrue(user.isEmpty());
    }

    @Test
    @Order(4)
    public void findById_TriesToFindUserByGivenId_UserIsPresent() {
        Optional<User> user = userService.findById(userList.get(0).getId());
        assertTrue(user.isPresent());
    }

    @Test
    @Order(5)
    public void loadUserByUsername_TriesToFindUserById_ReturnsUserDetails() {
        UserDetails user = userService.loadUserByUsername(userList.get(5).getId().toString());
        assertEquals(userList.get(5).getId().toString(), user.getUsername());
    }

    @Test
    @Order(6)
    public void deleteById_TriesToDeleteUserWithSpecificId_IdIsPresent() {
        assertDoesNotThrow(() -> userService.deleteById(userList.get(1).getId()));
        userList.remove(1);
    }

    @Test
    @Order(7)
    public void delete_DeletesAllPreviouslyCreatedUsers() {
        assertDoesNotThrow(() -> userList.forEach(user -> userService.delete(user)));
    }

}
