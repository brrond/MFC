package ua.nix.onishchenko.mfc.frontend.filter;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.nix.onishchenko.mfc.api.AuthorizationRequests;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@CommonsLog
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("/s/")) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("/login");
                return;
            }

            // check accessToken
            Object accessTokenObj = session.getAttribute("access_token");
            if (accessTokenObj == null) {
                log.warn("No access token found");
                response.sendRedirect("/login");
                return;
            }

            String accessToken = accessTokenObj.toString();
            if (AuthorizationRequests.isActive(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            // try to refresh
            Object refreshTokenObj = session.getAttribute("refresh_token");
            if (refreshTokenObj == null) {
                log.warn("No refresh token found");
                response.sendRedirect("/login");
                return;
            }

            String refreshToken = refreshTokenObj.toString();
            if (AuthorizationRequests.isActive(refreshToken)) {
                // then refresh
                String token = AuthorizationRequests.refresh(refreshToken);
                if (token == null) {
                    log.warn("Something completely wrong");
                    response.sendRedirect("/login");
                    return;
                }
                session.removeAttribute("access_token");
                session.setAttribute("access_token", token);

                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) authentication.setAuthenticated(false);
            response.sendRedirect("/login");
            return;
        }
        filterChain.doFilter(request, response);
    }

}
