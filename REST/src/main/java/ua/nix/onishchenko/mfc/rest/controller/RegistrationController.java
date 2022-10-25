package ua.nix.onishchenko.mfc.rest.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.nix.onishchenko.mfc.rest.dto.UserDTO;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.UserService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;

import java.util.Map;

@CommonsLog
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> createUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user = userService.save(user);
        if (user == null) {
            log.debug("Creation successful");
            return Map.of();
        }
        return ControllerUtils.getMap(user);
    }

}
