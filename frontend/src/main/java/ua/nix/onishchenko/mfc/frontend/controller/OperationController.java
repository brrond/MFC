package ua.nix.onishchenko.mfc.frontend.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nix.onishchenko.mfc.api.OperationRequests;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@CommonsLog
@RequestMapping("/s/")
public class OperationController {

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
        if (uuid != null) return MessageController.generateMessage(model, "Success", "Operation added.", "/s/account/" + accountId, "To account");
        return MessageController.generateMessage(model, "Error", "Something went wrong. Try again", "/s/account/" + accountId, "To account");
    }

}
