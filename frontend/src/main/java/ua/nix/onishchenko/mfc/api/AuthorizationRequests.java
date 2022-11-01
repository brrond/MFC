package ua.nix.onishchenko.mfc.api;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

    public static Map login(String email, String password) {
        String url = URL + "login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map> response = getRestTemplate().postForEntity(url, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return response.getBody();
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return Util.error(e.getMessage());
        }
    }

    public static String refresh(String token) {
        String url = URL + "refresh";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION, "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = getRestTemplate().exchange(url, HttpMethod.GET, entity, Map.class, new Object());
            if (response.getStatusCode() == HttpStatus.OK &&
                    response.hasBody() && !response.getBody().containsKey("error")) {
                return (String) response.getBody().get("access_token");
            } else {
                return null;
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return null;
        }
    }

    public static boolean isActive(String token) {
        String url = URL + "isActive";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION, "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = getRestTemplate().exchange(url, HttpMethod.GET, entity, Map.class, new Object());
            if (response.getStatusCode() == HttpStatus.OK &&
                    response.hasBody() && response.getBody().containsKey("error")) {
                Map map = response.getBody();
                if (map.get("error").equals("true")) return true;
                return false;
            } else {
                return false;
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return false;
        }
    }

}
