package ua.nix.onishchenko.mfc.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/s/")
public class UserController {

    @GetMapping("/")
    public String personalPage() {return "personal_page";}

    @PostMapping("/authenticate")
    public String authenticate(Model model) {
        model.addAttribute("msg", "Hello world");
        return "msg";
    }

    @PostMapping("/register")
    public String register(Model model) {
        model.addAttribute("msg", "Hello world");
        return "msg";
    }

}
