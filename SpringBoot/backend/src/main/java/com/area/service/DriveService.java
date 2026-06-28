package com.area.service;

import com.area.service.oauth.GoogleTokenStore;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DriveService implements AreaService {

    private final RestTemplate restTemplate;
    private final GoogleTokenStore tokenStore;

    public DriveService(RestTemplate restTemplate, GoogleTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.tokenStore = tokenStore;
    }

    @Override
    public String getName() {
        return "Drive";
    }

    @Override
    public Object execute(String action, Map<String, String> params) {
        return switch (action) {
            case "checkNewFiles", "checkFileDescription" -> listFiles();
            default -> Map.of("message", "Unknown action: " + action, "code", 0);
        };
    }

    private Object listFiles() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenStore.getAccessToken());
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.exchange(
                "https://www.googleapis.com/drive/v3/files?pageSize=10",
                HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
        int count = response == null ? 0 : ((List<?>) response.getOrDefault("files", List.of())).size();
        return Map.of("message", "Files checked", "count", count, "code", count > 0 ? 1 : 0);
    }
}
