package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.nix.onishchenko.mfc.api.UserRequests;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

import static ua.nix.onishchenko.mfc.frontend.controller.MessageController.generateMessage;

@Controller
@CommonsLog
@RequestMapping("/s/")
public class UserController {

    @GetMapping("/")
    public String personalPage(HttpSession httpSession,
                               Model model) {
        String token = httpSession.getAttribute("access_token").toString();
        Map<String, String> map = UserRequests.getGeneralInfo(token);
        if (map == null || map.isEmpty()) {
            // TODO: Almost impossible but still implement error case
            return generateMessage(model, "TODO: Later", "/");
        }

        model.addAttribute("name", map.get("name"));
        model.addAttribute("email", map.get("email"));
        model.addAttribute("creation", map.get("creation"));

        Set<Map<String, String>> setOfAccounts = UserRequests.getAllAccounts(token);
        model.addAttribute("setOfAccounts", (setOfAccounts == null || setOfAccounts.isEmpty()) ? Set.of() : setOfAccounts);

        Set<Map<String, String>> setOfOperationTypes = UserRequests.getAllOperationTypes(token);
        model.addAttribute("setOfOperationTypes", (setOfOperationTypes == null || setOfOperationTypes.isEmpty()) ? Set.of() : setOfOperationTypes);

        // TODO: Add 'Add Account'*
        //  'Delete Account'
        //  'Rename Account'
        //  'Delete Profile'
        //  'Update Profile'
        //  'Add OperationType'*
        //  'Delete OperationType'
        //  'Rename OperationType'

        return "personal_page";
    }

}
