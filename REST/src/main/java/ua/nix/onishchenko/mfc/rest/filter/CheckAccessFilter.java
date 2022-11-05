package ua.nix.onishchenko.mfc.rest.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.nix.onishchenko.mfc.rest.entity.Account;
import ua.nix.onishchenko.mfc.rest.entity.Operation;
import ua.nix.onishchenko.mfc.rest.entity.OperationType;
import ua.nix.onishchenko.mfc.rest.service.AccountService;
import ua.nix.onishchenko.mfc.rest.service.OperationService;
import ua.nix.onishchenko.mfc.rest.service.OperationTypeService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CommonsLog
public class CheckAccessFilter extends OncePerRequestFilter {

    private OperationTypeService operationTypeService = null;

    private AccountService accountService = null;

    private OperationService operationService = null;

    private void checkAccountBelongsToUser(String accountId, UUID userId) {
        if (accountId != null && !accountId.equals("")) {
            Optional<Account> optionalAccount = accountService.findById(UUID.fromString(accountId));
            if (optionalAccount.isEmpty()) throw new RuntimeException("Invalid AccountUUID");
            Account account = optionalAccount.get();
            if (!Objects.equals(account.getHolder().getId(), userId))
                throw new SecurityException("Account doesn't belong to current User");
        }
    }

    private void checkOperationTypeBelongsToUser(String operationTypeId, UUID userId) {
        if (operationTypeId != null) {
            // check if authorized user has such OperationType
            Optional<OperationType> optionalOperationType = operationTypeService.findById(UUID.fromString(operationTypeId));
            if (optionalOperationType.isEmpty())
                throw new RuntimeException("Invalid OperationTypeUUID");
            OperationType type = optionalOperationType.get();
            if (!Objects.equals(type.getCreator().getId(), userId)) throw new SecurityException("CreatorId and authorized UserId don't match");
        }
    }

    private void checkOperationBelongsToUser(String operationId, UUID userId) {
        // if user works with specific operation
        if (operationId != null) {
            Optional<Operation> optionalOperation = operationService.findById(UUID.fromString(operationId));
            if (optionalOperation.isEmpty()) throw new RuntimeException("Invalid OperationUUID");
            Operation operation = optionalOperation.get();
            if (!Objects.equals(operation.getAccount().getHolder().getId(), userId)) throw new SecurityException("Operation doesn't belong to current User");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Crunch
        if (operationTypeService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            assert webApplicationContext != null;
            operationTypeService = webApplicationContext.getBean(OperationTypeService.class);
            accountService = webApplicationContext.getBean(AccountService.class);
            operationService = webApplicationContext.getBean(OperationService.class);
        }

        if (request.getServletPath().contains("/s/")) {
            UUID userId = (UUID) request.getAttribute("userId");

            try {
                String operationTypeId = request.getParameter("operationTypeId");
                String operationId = request.getParameter("operationId");
                String accountId = request.getParameter("accountId");

                checkAccountBelongsToUser(accountId, userId);
                checkOperationTypeBelongsToUser(operationTypeId, userId);
                checkOperationBelongsToUser(operationId, userId);
            } catch (Exception e) {
                log.warn(e.getMessage());
                log.debug(e);
                response.setStatus(403); // 403 FORBIDDEN
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        ControllerUtils.error(e.getMessage()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
