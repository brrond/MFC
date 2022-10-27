package ua.nix.onishchenko.mfc.rest.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.nix.onishchenko.mfc.rest.entity.User;

import java.util.Date;

@org.springframework.stereotype.Component
public final class SecurityUtils {

    private final static Algorithm ALGORITHM = Algorithm.HMAC256(getSecret().getBytes());

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getSecret() {
        return "1fChgDB5WelGYg*iphjsm7U@OMGexW52mOu&DNMw31C3cmSsJM"; // TODO: Secret isn't secure
    }

    // TODO: Maybe manage it like bean?
    public static Algorithm getAlgorithm() {
        return ALGORITHM;
    }

    public static String getEmail(String token) {
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

}
