package ua.nix.onishchenko.mfc.api;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
public final class OperationTypeRequests extends AbstractAPIRequests {

    private static final String URL = getBaseUrl() + "operationtypes/s/";

    public static String getTitle(String token, String typeId) {
        String url = URL + "getGeneralInfo";

        HttpHeaders headers = buildHeaders(token);

        Map<String, Object> params = new HashMap<>();
        params.put("operationTypeId", typeId);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("operationTypeId", "{operationTypeId}")
                .encode()
                .toUriString();

        try {
            ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate, HttpMethod.GET, entity, Map.class, params);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (String) ((Map)response.getBody().get("operationType")).get("title");
            }
            return null;
        } catch(Exception e) {
            log.error(e.getMessage());
            log.debug(e);
            return null;
        }
    }

    public static String create(String token, String title) {
        String url = URL + "createOperationType";

        HttpHeaders headers = buildHeaders(token);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("title", title);

        HttpEntity<MultiValueMap> entity = new HttpEntity<>(params, headers);

        return (String) exchange(url, entity, HttpMethod.POST, Map.class).get("id");
    }

}
