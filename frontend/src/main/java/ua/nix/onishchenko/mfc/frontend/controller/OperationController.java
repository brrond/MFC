package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nix.onishchenko.mfc.api.OperationRequests;
import ua.nix.onishchenko.mfc.api.UserRequests;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@CommonsLog
@RequestMapping("/s/")
public class OperationController {

    @GetMapping("/new_operation")
    public String newOperation(Model model,
                               HttpSession httpSession,
                               @RequestParam("accountId") String accountId) {
        String token = httpSession.getAttribute("access_token").toString();

        Set<Map<String, String>> types = UserRequests.getAllOperationTypes(token);
        if (types == null) types = new HashSet<>();
        Map<String, String> empty = new HashMap<>(); empty.put("id", "null"); empty.put("title", "default");
        types.add(empty);
        model.addAttribute("operationtypes", types);
        model.addAttribute("accountId", accountId);

        return "new_operation";
    }

    @PostMapping("/new_operation_register")
    public String newOperationRegister(Model model,
                                       HttpSession httpSession,
                                       @RequestParam("accountId") String accountId,
                                       @RequestParam("sum") BigDecimal sum,
                                       @RequestParam("operationtpye") String operationTypeId,
                                       @RequestParam("datetime") String dateTime) {
        String token = httpSession.getAttribute("access_token").toString();

        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        String uuid = OperationRequests.create(token, accountId, sum, operationTypeId, localDateTime);
        if (uuid != null) return MessageController.generateMessage(model, "Operation added", "/s/account/" + accountId);
        return MessageController.generateMessage(model, "Something went wrong. Try again", "/s/new_operation?accoundId=" + accountId);
    }

}
