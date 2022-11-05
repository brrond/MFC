package ua.nix.onishchenko.mfc.api;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@CommonsLog
public abstract class AbstractAPIRequests {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    // TODO: Change base url in production
    private static final String BASE_URL = "http://mfc-rest:8080/api/";

    protected static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

    protected static String getBaseUrl() {
        return BASE_URL;
    }

    protected static HttpHeaders buildHeaders(String token) {
        return buildHeaders(token, MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected static HttpHeaders buildHeaders(String token, MediaType type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        headers.set(AUTHORIZATION, "Bearer " + token);
        return headers;
    }

    protected static <T> T exchange(String url, HttpEntity<?> entity, HttpMethod method, Class<T> returnType) {
        try {
            ResponseEntity<T> response = getRestTemplate().exchange(url, method, entity, returnType, new Object());
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return null;
        }
    }

}
