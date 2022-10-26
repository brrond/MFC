package ua.nix.onishchenko.mfc.rest.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.stereotype.Component
public final class SecurityUtils {
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getSecret() {
        return "1fChgDB5WelGYg*iphjsm7U@OMGexW52mOu&DNMw31C3cmSsJM"; // TODO: Secret isn't secure
    }

}
