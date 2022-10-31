package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@CommonsLog
@Controller
public class MessageController {

    @GetMapping("/login_error")
    public String loginError(Model model) {
        log.warn("Login error");
        model.addAttribute("msg", "Login error. Check your credentials and try again.");
        model.addAttribute("href", "/login");
        return "msg";
    }

}
