package ua.nix.onishchenko.mfc.rest.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.rest.dto.UserDTO;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.UserService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;

import java.util.*;

@CommonsLog
@RestController
@RequestMapping(path="/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> createUser(@RequestBody UserDTO userDTO) {
        log.debug("User email : " + userDTO.getEmail());
        if (userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty() || userDTO.getName().isEmpty()) {
            log.debug("One or more fields are empty");
            return ControllerUtils.error("Email, password and name must be specified");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());

        try {
            user = userService.save(user);
        } catch(Exception e) {
            user = null;
            log.error(e.getMessage(), e);
        }

        if (user == null) {
            log.debug("Creation unsuccessful");
            return ControllerUtils.error("Something went wrong");
        }
        log.debug("Creation successful. " + user.getId());
        return ControllerUtils.getMap(user);
    }

    @PutMapping(path="s/updateUser")
    public Map<String, Object> updateUser(@RequestBody UserDTO userDTO) {
        log.debug("userId : " + userDTO.getUserId());
        if (userDTO.getUserId() == null) {
            log.warn("User id is null");
            return ControllerUtils.error("userId is null");
        }

        Optional<User> userOptional = userService.findById(userDTO.getUserId());
        if (userOptional.isEmpty()) {
            log.warn("User id isn't found");
            return ControllerUtils.error("userId isn't found");
        }
        User user = userOptional.get();
        if (!Objects.equals(userDTO.getEmail(), "")) {
            user.setEmail(userDTO.getEmail());
        }

        boolean passwordChanged = false;
        if (!Objects.equals(userDTO.getPassword(), "")) {
            user.setPassword(userDTO.getPassword());
            passwordChanged = true;
        }

        if (!Objects.equals(userDTO.getName(), "")) {
            user.setName(userDTO.getName());
        }

        try {
            user = userService.save(user, passwordChanged);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            return ControllerUtils.error("Something went wrong");
        }
        return ControllerUtils.getMap(user);
    }

    @GetMapping(path="s/getAllAccounts")
    public Set<Account> getAllAccounts(@RequestParam("userId")UUID id) {
        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " is present");
            return optionalUser.get().getAccounts();
        }
        return Set.of();
    }

    @DeleteMapping(path="s/deleteUser")
    public Map<String, Object> deleteUser(@RequestParam("userId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " is present");
            User user = optionalUser.get();
            userService.delete(user);
            return ControllerUtils.getMap(user);
        }
        return ControllerUtils.error("User isn't present");
    }

}
