package ua.nix.onishchenko.mfc.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ua.nix.onishchenko.mfc.rest.filter.CheckAccessFilter;
import ua.nix.onishchenko.mfc.rest.filter.CustomAuthenticationFilter;
import ua.nix.onishchenko.mfc.rest.filter.CustomAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class RESTApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
        filter.setFilterProcessesUrl("/api/users/login");
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/api/*/s/**").authenticated()
                .and()
                .addFilterBefore(
                        filter,
                        BasicAuthenticationFilter.class
                )
                .addFilterBefore(
                        new CustomAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(
                        new CheckAccessFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
