package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.nix.onishchenko.mfc.api.UserRequests;
import ua.nix.onishchenko.mfc.frontend.util.Util;

import java.util.Map;
import java.util.Set;

import static ua.nix.onishchenko.mfc.frontend.controller.MessageController.generateMessage;

@Controller
@CommonsLog
@RequestMapping("/s/")
public class UserController {

    @GetMapping("/")
    public String personalPage(@CookieValue("access_token") String token,
                               Model model) {
        Map<String, String> map = UserRequests.getGeneralInfo(token);
        if (map == null || map.isEmpty()) {
            return generateMessage(model, "Error", "Something went wrong", "/", "Go to Home");
        }

        model.addAttribute("name", map.get("name"));
        model.addAttribute("email", map.get("email"));
        model.addAttribute("creation", Util.convertISOToString(map.get("creation")));

        Set<Map<String, String>> setOfAccounts = UserRequests.getAllAccounts(token);
        if (setOfAccounts != null && !setOfAccounts.isEmpty()) {
            for (var account : setOfAccounts) {
                account.put("creation", Util.convertISOToString(account.get("creation")));
            }
        }
        model.addAttribute("setOfAccounts", (setOfAccounts == null || setOfAccounts.isEmpty()) ? Set.of() : setOfAccounts);

        Set<Map<String, String>> setOfOperationTypes = UserRequests.getAllOperationTypes(token);
        model.addAttribute("setOfOperationTypes", (setOfOperationTypes == null || setOfOperationTypes.isEmpty()) ? Set.of() : setOfOperationTypes);

        // TODO:    'Delete Account'
        //          'Rename Account'
        //  'Delete Profile'
        //  'Update Profile'
        //      'Delete OperationType'
        //      'Rename OperationType'

        return "personal_page";
    }

}
