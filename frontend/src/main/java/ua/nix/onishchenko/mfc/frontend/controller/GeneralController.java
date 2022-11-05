package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nix.onishchenko.mfc.api.AuthorizationRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@CommonsLog
@Controller
public class GeneralController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @SneakyThrows
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (!session.isNew() && session.getAttribute("access_token") != null) {
            response.sendRedirect("/s/");
        }
        return "login";
    }

    @SneakyThrows
    @GetMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (!session.isNew() && session.getAttribute("access_token") != null) {
            response.sendRedirect("/s/");
        }
        return "register";
    }

    @PostMapping("/registration")
    public String registration(@RequestParam("email") String email,
                           @RequestParam("name") String name,
                           @RequestParam("password") String password,
                           Model model) {
        String error = AuthorizationRequests.register(name, email, password);
        if (error == null) {
            log.info("Registration successful");
            return MessageController.generateMessage(model, "Registration successful", "Now you can login.", "/login", "Go to login page");
        } else {
            log.warn("Login error");
            return MessageController.generateMessage(model, "Registration error : ", error, "/register", "Go to registration page");
        }
    }

}
