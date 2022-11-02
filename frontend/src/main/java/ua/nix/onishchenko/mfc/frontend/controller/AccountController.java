package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.nix.onishchenko.mfc.api.AccountRequests;

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

        // TODO: Add 'Add Operation'*
        //  'Delete Operation'

        // TODO: Add filters

        // TODO: Add simple visualization on page*

        return "account";
    }

}
