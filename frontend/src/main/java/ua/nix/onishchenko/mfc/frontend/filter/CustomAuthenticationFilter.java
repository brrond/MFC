package ua.nix.onishchenko.mfc.frontend.filter;

import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.nix.onishchenko.mfc.api.AuthorizationRequests;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@CommonsLog
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("remember-me"); // on -> true, null -> false

        log.info("AuthenticationAttempt with email: " + email + ", password: " + password + ", " + " remember-me: " + rememberMe);
        Map map = AuthorizationRequests.login(email, password);
        if (map.containsKey("error")) {
            throw new BadCredentialsException("No user found");
        }

        Cookie accessTokenCookie = new Cookie("access_token", (String) map.get("access_token"));
        accessTokenCookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(accessTokenCookie);
        if (Objects.equals(rememberMe, "true")) {
            Cookie refreshTokenCookie = new Cookie("refresh_token", (String) map.get("refresh_token"));
            refreshTokenCookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(refreshTokenCookie);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(map.get("access_token"), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // super.unsuccessfulAuthentication(request, response, failed);
        log.warn(failed.getMessage());
        log.debug(failed);
        response.sendRedirect("/login_error");
    }

}
