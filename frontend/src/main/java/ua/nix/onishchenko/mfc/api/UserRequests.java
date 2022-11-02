package ua.nix.onishchenko.mfc.api;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.*;

import java.util.Map;
import java.util.Set;

@CommonsLog
public final class UserRequests extends AbstractAPIRequests {

    private static final String URL = getBaseUrl() + "users/s/";

    public static Map<String, String> getGeneralInfo(String token) {
        String url = URL + "getGeneralInfo";

        HttpHeaders headers = buildHeaders(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = getRestTemplate().exchange(url, HttpMethod.GET, entity, Map.class, new Object());
            if (response.getStatusCode() == HttpStatus.OK) {
                Map map = response.getBody();
                if (map == null || map.isEmpty()) {
                    return null;
                }
                return map;
            }
            return null;
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return null;
        }
    }

    public static Set<Map<String, String>> getAllAccounts(String token) {
        String url = URL + "getAllAccounts";

        HttpHeaders headers = buildHeaders(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return exchange(url, entity, HttpMethod.GET, Set.class);
    }

    public static Set<Map<String, String>> getAllOperationTypes(String token) {
        String url = URL + "getAllOperationTypes";

        HttpHeaders headers = buildHeaders(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return exchange(url, entity, HttpMethod.GET, Set.class);
    }

}
