package ua.nix.onishchenko.mfc.rest.controller;

import com.auth0.jwt.JWT;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.rest.dto.UserDTO;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.UserService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;
import ua.nix.onishchenko.mfc.rest.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
            log.error(e.getMessage());
            log.debug(e);
            return ControllerUtils.error(e.getMessage());
        }

        log.debug("Creation successful. " + user.getId());
        return ControllerUtils.getMap(user);
    }

    @GetMapping("/refresh")
    public Map<String, Object> refresh(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                String id = SecurityUtils.getUserId(refreshToken);
                Optional<User> optionalUser = userService.findById(UUID.fromString(id));
                if (optionalUser.isEmpty()) {
                    log.warn("User isn't found");
                    return ControllerUtils.error("No User present");
                }

                User user = optionalUser.get();
                String accessToken = JWT.create()
                        .withSubject(user.getId().toString())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // every 5 minutes
                        .withIssuer(request.getRequestURI())
                        .sign(SecurityUtils.getAlgorithm());

                Map<String, Object> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                return tokens;
            } catch (Exception e) {
                log.warn(e.getMessage());
                log.debug(e);
                return ControllerUtils.error(e.getMessage());
            }
        }
        return ControllerUtils.error("No refresh token");
    }

    @PutMapping(path="s/updateUser")
    public Map<String, Object> updateUser(@RequestAttribute("userId") UUID userId,
                                          @RequestBody UserDTO userDTO) {
        log.debug("userId : " + userId);

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            log.debug("User id isn't found");
            return ControllerUtils.error("UserId isn't found");
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
            log.error(e);
            log.debug(e.getMessage());
            return ControllerUtils.error(e.getMessage());
        }
        return ControllerUtils.getMap(user);
    }

    @GetMapping(path="s/getAllAccounts")
    public Set<Account> getAllAccounts(@RequestAttribute("userId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " is present");
            return optionalUser.get().getAccounts();
        }
        return Set.of();
    }

    @DeleteMapping(path="s/deleteUser")
    public Map<String, Object> deleteUser(@RequestAttribute("userId") UUID id) {
        log.debug("UUID id = " + id);
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            log.debug("UUID id = " + id + " is present");
            User user = optionalUser.get();
            try {
                userService.delete(user);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.debug(e);
                return ControllerUtils.error(e.getMessage());
            }
            return ControllerUtils.getMap(user);
        }
        return ControllerUtils.error("User isn't presented");
    }

}
