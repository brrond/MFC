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

    @GetMapping(path="getUser")
    public Map<String, Object> getUser(@RequestParam("email") String email) {
        log.debug("Email = " + email);
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.debug("UUID id = " + user.getId());
            return ControllerUtils.getMap(user);
        }
        log.debug("User not presented");
        return Map.of();
    }

    @PutMapping(path="updateUser")
    public Map<String, Object> updateUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getUserId() == null) {
            log.warn("User id is null");
            return Map.of();
        }
        Optional<User> userOptional = userService.findById(userDTO.getUserId());
        if (userOptional.isEmpty()) {
            log.warn("User id isn't found");
            return Map.of();
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

        user = userService.save(user, passwordChanged);
        return ControllerUtils.getMap(user);
    }

    @GetMapping(path="getAllAccounts")
    public Set<Account> getAllAccounts(@RequestParam("userId")UUID id) {
        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " is present");
            return optionalUser.get().getAccounts();
        }
        return Set.of();
    }

    @DeleteMapping(path="deleteUser")
    public Map<String, Object> deleteUser(@RequestParam("userId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " is present");
            User user = optionalUser.get();
            userService.delete(user);
            return ControllerUtils.getMap(user);
        }
        return Map.of();
    }

}
