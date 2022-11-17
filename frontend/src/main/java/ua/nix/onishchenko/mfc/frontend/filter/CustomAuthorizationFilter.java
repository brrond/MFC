package ua.nix.onishchenko.mfc.frontend.filter;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.nix.onishchenko.mfc.api.AuthorizationRequests;
import ua.nix.onishchenko.mfc.frontend.util.Util;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@CommonsLog
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("/s/")) {
            Cookie[] cookies = request.getCookies();
            Cookie accessTokenCookie = Util.getAccessTokenCookie(cookies);
            Cookie refreshTokenCookie = Util.getRefreshTokenCookie(cookies);

            if (accessTokenCookie == null) {
                response.sendRedirect("/login");
                return;
            }

            // check accessToken
            Object accessTokenObj = accessTokenCookie.getValue();
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

            if (refreshTokenCookie == null) {
                response.sendRedirect("/login");
                return;
            }

            // try to refresh
            Object refreshTokenObj = refreshTokenCookie.getValue();
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
                accessTokenCookie.setValue(token);
                response.addCookie(accessTokenCookie);

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
