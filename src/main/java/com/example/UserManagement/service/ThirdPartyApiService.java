package com.example.UserManagement.service;

import com.example.UserManagement.dto.thirdParty.ThirdPartyRequest;
import com.example.UserManagement.dto.thirdParty.ThirdPartyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ThirdPartyApiService {
    private static final String THIRD_PARTY_API_URL = "http://localhost:8080/v1/api/users/create";

    private final RestTemplate restTemplate;

    public ThirdPartyApiService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Calls the third-party API to create a user and handles both success and error responses.
     *
     * @param request the request payload containing user data
     * @return the response from the third-party API, with clear success or error status
     */
    public ThirdPartyResponse callThirdPartyApi(ThirdPartyRequest request) {
        Map<String, Object> payload = buildPayload(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<ThirdPartyResponse> response = restTemplate.exchange(
                    THIRD_PARTY_API_URL,
                    HttpMethod.POST,
                    entity,
                    ThirdPartyResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully called third-party API. Status: {}", response.getStatusCode());
                return buildSuccessResponse(response.getBody().getData());
            } else {
                log.warn("Third-party API returned non-success status: {}", response.getStatusCode());
                return buildErrorResponse("Third-party API returned status: " + response.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            log.error("HTTP error when calling third-party API: {}", ex.getStatusCode(), ex);
            return buildErrorResponse("HTTP error from 3rd-party API: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            log.error("Resource access error when calling third-party API", ex);
            return buildErrorResponse("Resource access error: " + ex.getMessage());
        } catch (RestClientException ex) {
            log.error("Rest client error when calling third-party API", ex);
            return buildErrorResponse("Rest client error: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error when calling third-party API", ex);
            return buildErrorResponse("Unexpected error: " + ex.getMessage());
        }
    }

    /**
     * Builds the request payload map from non-null fields of the request.
     */
    private Map<String, Object> buildPayload(ThirdPartyRequest request) {
        Map<String, Object> payload = new HashMap<>();
        if (request.getUsername() != null) payload.put("username", request.getUsername());
        if (request.getPassword() != null) payload.put("password", request.getPassword());
        if (request.getEmail() != null) payload.put("email", request.getEmail());
        if (request.getPhoneNumber() != null) payload.put("phoneNumber", request.getPhoneNumber());
        if (request.getExtraFields() != null) payload.putAll(request.getExtraFields());
        return payload;
    }

    /**
     * Constructs a standardized success response.
     */
    private ThirdPartyResponse buildSuccessResponse(String data) {
        ThirdPartyResponse response = new ThirdPartyResponse();
        response.setStatus("success");
        response.setData(data);
        return response;
    }

    /**
     * Constructs a standardized error response.
     */
    private ThirdPartyResponse buildErrorResponse(String message) {
        ThirdPartyResponse errorResponse = new ThirdPartyResponse();
        errorResponse.setStatus("error");
        errorResponse.setData(message);
        return errorResponse;
    }
}