package ua.nix.onishchenko.mfc.api;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
public final class AuthorizationRequests extends AbstractAPIRequests{

    private static final String URL = getBaseUrl() + "users/";

    public static String register(String name, String email, String password) {
        String url = URL + "register";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map> response = getRestTemplate().postForEntity(url, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return null;
            } else {
                return (String) response.getBody().get("error");
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return e.getMessage();
        }
    }

}
