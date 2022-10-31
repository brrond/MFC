package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@CommonsLog
@Controller
public class MessageController {

    public static String generateMessage(Model model, String msg, String href) {
        model.addAttribute("msg", msg);
        model.addAttribute("href", href);
        return "msg";
    }

    @GetMapping("/login_error")
    public String loginError(Model model) {
        log.warn("Login error");
        return generateMessage(model, "Login error. Check your credentials and try again.", "/login");
    }

}
