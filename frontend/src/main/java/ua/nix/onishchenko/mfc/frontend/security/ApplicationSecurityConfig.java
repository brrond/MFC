package ua.nix.onishchenko.mfc.frontend.security;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.nix.onishchenko.mfc.frontend.filter.CustomAuthenticationFilter;
import ua.nix.onishchenko.mfc.frontend.filter.CustomAuthorizationFilter;

import java.util.UUID;

@CommonsLog
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/authenticate");
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/s/**").authenticated()
                .antMatchers("/**", "/css/*", "/js/*", "/img/*").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .successForwardUrl("/s/")
                    .failureUrl("/login_error")
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "access_token", "refresh_token")
                    .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(
                        customAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new CustomAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            Object principal = authentication.getPrincipal();
            if (principal != null && principal.toString() != null) {
                String UUIDStr = principal.toString();
                try {
                    UUID uuid = UUID.fromString(UUIDStr);
                    authentication.setAuthenticated(true);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                    log.debug(e);
                }
            }
            return authentication;
        };
    }

}
