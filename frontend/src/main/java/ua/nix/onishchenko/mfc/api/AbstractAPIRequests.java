package ua.nix.onishchenko.mfc.api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public abstract class AbstractAPIRequests {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    // TODO: Change base url in production
    private static final String BASE_URL = "http://localhost:8080/api/";

    protected static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

    protected static String getBaseUrl() {
        return BASE_URL;
    }

}
