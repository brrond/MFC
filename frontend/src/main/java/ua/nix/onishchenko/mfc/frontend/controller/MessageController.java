package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import static org.springframework.http.HttpStatus.*;

@CommonsLog
@Controller
public class MessageController implements ErrorController {

    public static String generateMessage(Model model, String title, String msg, String href, String hrefMsg) {
        model.addAttribute("title", title);
        model.addAttribute("msg", msg);
        model.addAttribute("href", href);
        model.addAttribute("hrefMsg", hrefMsg);
        return "msg";
    }

    @GetMapping("/login_error")
    public String loginError(Model model) {
        log.warn("Login error");
        return generateMessage(model, "Login error", "Check your credentials and try again.", "/login", "Go to login page");
    }

    @GetMapping("/error")
    public String error(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == NOT_FOUND.value()) {
                return generateMessage(model, "404", "No page exists", "/", "Home");
            }
        }
        return "msg";
    }

}
