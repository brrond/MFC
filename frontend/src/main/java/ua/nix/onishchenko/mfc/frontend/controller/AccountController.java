package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.api.AccountRequests;
import ua.nix.onishchenko.mfc.api.Util;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

@Controller
@CommonsLog
@RequestMapping("/s/")
public class AccountController {

    @GetMapping("/account/{accountId}")
    public String account(@PathVariable("accountId") String accountId,
                          HttpSession httpSession,
                          Model model) {
        String token = httpSession.getAttribute("access_token").toString();

        Map<String, String> accountInfo = AccountRequests.getGeneralInfo(token, accountId);
        model.addAttribute("title", accountInfo.get("title"));
        model.addAttribute("balance", accountInfo.get("balance"));
        model.addAttribute("creation", accountInfo.get("creation"));

        Set<Map<String, String>> setOfOperations = AccountRequests.getAllOperations(token, accountId);
        model.addAttribute("setOfOperations", (setOfOperations == null || setOfOperations.isEmpty()) ? Set.of() : setOfOperations);

        // TODO: Add    'Add Operation'*
        //              'Delete Operation'

        // TODO: Add filters

        // TODO: Add simple visualization on page*

        return "account";
    }

    @GetMapping("/new_account")
    public String newAccount() {
        return "new_account";
    }

    @PostMapping("/new_account_register")
    public String newAccountRegistration(Model model,
                                         HttpSession httpSession,
                                         @RequestParam("title") String title) {

        String token = httpSession.getAttribute("access_token").toString();

        // TODO: Move regex to some Util class
        if (!title.matches("[a-zA-z0-9_. ]+")) {
            return MessageController.generateMessage(model, "Title doesn't match regex [a-zA-z0-9_. ]+", "/s/new_account");
        }

        String uuid = AccountRequests.create(token, title);
        if (uuid != null) {
            return MessageController.generateMessage(model, "Account was registered successfully", "/s/");
        }
        return MessageController.generateMessage(model, "Something went wrong. Try again", "/s/new_account");
    }

}
