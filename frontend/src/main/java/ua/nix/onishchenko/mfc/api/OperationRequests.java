package ua.nix.onishchenko.mfc.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class OperationRequests  extends AbstractAPIRequests {

    private static final String URL = getBaseUrl() + "operations/s/";

    public static String create(String token,
                                String accountId,
                                BigDecimal sum,
                                String operationTypeId,
                                LocalDateTime localDateTime) {
        String url = URL + "createOperation";

        HttpHeaders headers = buildHeaders(token);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("accountId", accountId);
        params.add("sum", sum);
        if (operationTypeId != null && !operationTypeId.equals("null")) params.add("operationTypeId", operationTypeId);
        params.add("localDateTime", localDateTime);

        HttpEntity<MultiValueMap> entity = new HttpEntity<>(params, headers);

        return (String) exchange(url, entity, HttpMethod.POST, Map.class).get("id");
    }

}
