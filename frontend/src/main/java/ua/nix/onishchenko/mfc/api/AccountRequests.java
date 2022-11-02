package ua.nix.onishchenko.mfc.api;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@CommonsLog
public class AccountRequests extends AbstractAPIRequests {

    private static final String URL = getBaseUrl() + "accounts/s/";

    public static Map<String, String> getGeneralInfo(String token, String accountId) {
        String url = URL + "getGeneralInfo";

        HttpHeaders headers = buildHeaders(token);

        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accountId", "{accountId}")
                .encode()
                .toUriString();

        try {
            ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate, HttpMethod.GET, entity, Map.class, params);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (Map<String, String>) response.getBody().get("account");
            }
            return null;
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return null;
        }
    }

    public static Set<Map<String, String>> getAllOperations(String token, String accountId) {
        String url = URL + "getAllOperations";

        HttpHeaders headers = buildHeaders(token);

        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accountId", "{accountId}")
                .encode()
                .toUriString();

        try {
            ResponseEntity<Set> response = getRestTemplate().exchange(urlTemplate, HttpMethod.GET, entity, Set.class, params);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (Set<Map<String, String>>) response.getBody();
            }
            return null;
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return null;
        }
    }
}
