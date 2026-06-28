package com.area.service.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GitHubOAuthService {

    private final RestTemplate restTemplate;

    @Value("${github.client-id}")
    private String clientId;
    @Value("${github.client-secret}")
    private String clientSecret;

    public GitHubOAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String buildAuthUrl(String userEmail) {
        String redirectUri = "http://localhost:8080/auth/github/callback?userEmail=" + userEmail;
        return UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "user,repo")
                .build()
                .encode()
                .toUriString();
    }

    public String exchangeCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        Map<String, String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code);

        Map<?, ?> response = restTemplate.postForObject(
                "https://github.com/login/oauth/access_token",
                new HttpEntity<>(body, headers),
                Map.class);

        if (response == null || response.get("access_token") == null) {
            throw new IllegalStateException("GitHub access token could not be retrieved.");
        }
        return (String) response.get("access_token");
    }
}
