package ua.nix.onishchenko.mfc.rest.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.service.UserService;
import ua.nix.onishchenko.mfc.rest.util.ControllerUtils;
import ua.nix.onishchenko.mfc.rest.util.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CommonsLog
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private UserService userService = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (userService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            assert webApplicationContext != null;
            userService = webApplicationContext.getBean(UserService.class);
        }
        if (request.getRequestURI().contains("/s/")) {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    String id = SecurityUtils.getUserId(token);
                    Optional<User> optionalUser = userService.findById(UUID.fromString(id));
                    if (optionalUser.isEmpty()) {
                        log.warn("User isn't found");
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), ControllerUtils.error("No User present"));
                        return;
                    }

                    User user = optionalUser.get();
                    request.setAttribute("userId", user.getId());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user.getId(), null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                    log.debug(e);
                    response.setStatus(403); // 403 FORBIDDEN
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), ControllerUtils.error(e.getMessage()));
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
