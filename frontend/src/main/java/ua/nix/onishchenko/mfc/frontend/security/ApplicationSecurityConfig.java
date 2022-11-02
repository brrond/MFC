package ua.nix.onishchenko.mfc.frontend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.nix.onishchenko.mfc.frontend.filter.CustomAuthenticationFilter;
import ua.nix.onishchenko.mfc.frontend.filter.CustomAuthorizationFilter;

import java.util.UUID;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
        customAuthenticationFilter.setFilterProcessesUrl("/authenticate");
        return http
                .csrf().disable()// TODO: Enable csrf
                .authorizeRequests()
                .antMatchers("/s/**").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .defaultSuccessUrl("/s/")
                    .failureUrl("/login_error")
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authentication -> {
            Object principal = authentication.getPrincipal();
            if (principal != null && principal.toString() != null) {
                String UUIDStr = principal.toString();
                try {
                    UUID uuid = UUID.fromString(UUIDStr);
                    authentication.setAuthenticated(true);
                } catch (Exception ignored) {

                }
            }
            return authentication;
        };
    }

}
