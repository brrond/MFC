package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nix.onishchenko.mfc.api.AccountRequests;
import ua.nix.onishchenko.mfc.api.OperationTypeRequests;
import ua.nix.onishchenko.mfc.api.UserRequests;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        model.addAttribute("accountId", accountId);

        Set<Map<String, String>> setOfOperations = AccountRequests.getAllOperations(token, accountId);
        model.addAttribute("setOfOperations", (setOfOperations == null || setOfOperations.isEmpty()) ? Set.of() :
                setOfOperations.stream().sorted((o1, o2) -> {
                    LocalDateTime ldt1 = LocalDateTime.parse(o1.get("creation"), DateTimeFormatter.ISO_DATE_TIME);
                    LocalDateTime ldt2 = LocalDateTime.parse(o2.get("creation"), DateTimeFormatter.ISO_DATE_TIME);
                    return ldt1.compareTo(ldt2);
                }).collect(Collectors.toList()));

        Set<Map<String, String>> setOfAccounts = UserRequests.getAllAccounts(token);
        model.addAttribute("setOfAccounts", (setOfAccounts == null || setOfAccounts.isEmpty()) ? Set.of() : setOfAccounts);

        Set<Map<String, String>> types = UserRequests.getAllOperationTypes(token);
        if (types == null) types = new HashSet<>();
        Map<String, String> empty = new HashMap<>(); empty.put("id", "null"); empty.put("title", "default");
        types.add(empty);
        model.addAttribute("operationtypes", types);

        // TODO: Add 'Delete Operation'
        // TODO: Add filters

        return "account";
    }

    @PostMapping("/new_account_register")
    public String newAccountRegistration(Model model,
                                         HttpSession httpSession,
                                         @RequestParam("title") String title) {

        String token = httpSession.getAttribute("access_token").toString();

        // TODO: Move regex to some Util class
        if (!title.matches("[a-zA-z0-9_. ]+")) {
            return MessageController.generateMessage(model, "Error", "Title doesn't match regex [a-zA-z0-9_. ]+", "/s/", "To personal page");
        }

        String uuid = AccountRequests.create(token, title);
        if (uuid != null) {
            return MessageController.generateMessage(model, "Success", "Account was registered successfully.", "/s/", "To personal page");
        }
        return MessageController.generateMessage(model, "Error", "Something went wrong. Try again.", "/s/", "To personal page");
    }

    @PostMapping("/new_operationtype_register")
    public String newOperationTypeRegistration(Model model,
                                         HttpSession httpSession,
                                         @RequestParam("title") String title) {

        String token = httpSession.getAttribute("access_token").toString();

        // TODO: Move regex to some Util class
        if (!title.matches("[a-zA-z0-9_. ]+")) {
            return MessageController.generateMessage(model, "Error", "Title doesn't match regex [a-zA-z0-9_. ]+", "/s/", "To personal page");
        }

        String uuid = OperationTypeRequests.create(token, title);
        if (uuid != null) {
            return MessageController.generateMessage(model, "Success", "OperationType was registered successfully.", "/s/", "To personal page");
        }
        return MessageController.generateMessage(model, "Error", "Something went wrong. Try again", "/s/", "To personal page");
    }

}
