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
public class YoutubeService implements AreaService {

    private final RestTemplate restTemplate;
    private final GoogleTokenStore tokenStore;

    public YoutubeService(RestTemplate restTemplate, GoogleTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.tokenStore = tokenStore;
    }

    @Override
    public String getName() {
        return "Youtube";
    }

    @Override
    public Object execute(String action, Map<String, String> params) {
        return switch (action) {
            case "checkNewSubcribers", "checkLostSubcribers" -> stats("subscriberCount");
            case "checkMostPopularVideo" -> stats("viewCount");
            default -> Map.of("message", "Unknown action: " + action, "code", 0);
        };
    }

    @SuppressWarnings("unchecked")
    private Object stats(String metric) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenStore.getAccessToken());
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.exchange(
                "https://www.googleapis.com/youtube/v3/channels?part=statistics&mine=true",
                HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();

        Object value = null;
        if (response != null) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getOrDefault("items", List.of());
            if (!items.isEmpty()) {
                Map<String, Object> stats = (Map<String, Object>) items.get(0).get("statistics");
                value = stats == null ? null : stats.get(metric);
            }
        }
        return Map.of("message", metric, "value", value == null ? "" : value, "code", value != null ? 1 : 0);
    }
}
