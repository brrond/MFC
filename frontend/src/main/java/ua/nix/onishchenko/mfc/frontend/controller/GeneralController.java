package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nix.onishchenko.mfc.api.AuthorizationRequests;

@CommonsLog
@Controller
public class GeneralController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }

    @PostMapping("/authenticate")
    public String authenticate(Model model) {
        model.addAttribute("msg", "Hello world from authenticate");
        return "msg";
    }

    @PostMapping("/register")
    public String register(@RequestParam("email") String email,
                           @RequestParam("name") String name,
                           @RequestParam("password") String password,
                           Model model) {
        String error = AuthorizationRequests.register(name, email, password);
        if (error == null) {
            log.info("Registration successful");
            return MessageController.generateMessage(model, "Registration successful. Now you can login.", "/login");
        } else {
            log.warn("Login error");
            return MessageController.generateMessage(model, "Registration error : " + error, "/registration");
        }
    }

}
