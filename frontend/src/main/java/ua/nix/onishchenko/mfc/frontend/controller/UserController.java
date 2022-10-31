package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CommonsLog
@RequestMapping("/s/")
public class UserController {

    @GetMapping("/")
    public String personalPage() {return "personal_page";}

}
