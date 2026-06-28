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
public class CalendarService implements AreaService {

    private final RestTemplate restTemplate;
    private final GoogleTokenStore tokenStore;

    public CalendarService(RestTemplate restTemplate, GoogleTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.tokenStore = tokenStore;
    }

    @Override
    public String getName() {
        return "Calendar";
    }

    @Override
    public Object execute(String action, Map<String, String> params) {
        if (!"checkNewEvent".equals(action)) {
            return Map.of("message", "Unknown action: " + action, "code", 0);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenStore.getAccessToken());
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.exchange(
                "https://www.googleapis.com/calendar/v3/calendars/primary/events?maxResults=1&orderBy=startTime&singleEvents=true",
                HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
        boolean hasEvent = response != null && ((List<?>) response.getOrDefault("items", List.of())).size() > 0;
        return Map.of("message", hasEvent ? "Event found" : "No event", "code", hasEvent ? 1 : 0);
    }
}
