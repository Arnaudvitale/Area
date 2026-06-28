package com.area.service;

import com.area.service.oauth.GoogleTokenStore;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class GmailService implements AreaService {

    private final RestTemplate restTemplate;
    private final GoogleTokenStore tokenStore;

    public GmailService(RestTemplate restTemplate, GoogleTokenStore tokenStore) {
        this.restTemplate = restTemplate;
        this.tokenStore = tokenStore;
    }

    @Override
    public String getName() {
        return "Gmail";
    }

    @Override
    public Object execute(String action, Map<String, String> params) {
        return switch (action) {
            case "checkNewEmails" -> checkNewEmails();
            case "sendEmail" -> sendEmail(params);
            default -> Map.of("message", "Unknown action: " + action, "code", 0);
        };
    }

    private HttpEntity<Void> auth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenStore.getAccessToken());
        return new HttpEntity<>(headers);
    }

    private Object checkNewEmails() {
        Map<?, ?> response = restTemplate.exchange(
                "https://gmail.googleapis.com/gmail/v1/users/me/messages?q=in:inbox",
                HttpMethod.GET, auth(), Map.class).getBody();
        boolean hasMessages = response != null && response.get("messages") != null;
        return Map.of("message", hasMessages ? "Inbox checked" : "No emails",
                "code", hasMessages ? 1 : 0);
    }

    private Object sendEmail(Map<String, String> params) {
        String receiver = params.get("receiver");
        String message = params.getOrDefault("message", "");
        String raw = "To: " + receiver + "\r\n"
                + "Subject: AREA automation\r\n"
                + "Content-Type: text/plain; charset=\"UTF-8\"\r\n\r\n"
                + message;
        String encoded = Base64.getUrlEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenStore.getAccessToken());
        Map<String, String> body = Map.of("raw", encoded);
        restTemplate.postForObject(
                "https://gmail.googleapis.com/gmail/v1/users/me/messages/send",
                new HttpEntity<>(body, headers), Map.class);
        return Map.of("message", "Email sent", "code", 1);
    }
}
