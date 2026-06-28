package com.area.service.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class DeezerOAuthService {

    private final RestTemplate restTemplate;

    @Value("${deezer.app-id}")
    private String appId;
    @Value("${deezer.secret-key}")
    private String secretKey;
    @Value("${deezer.redirect-uri}")
    private String redirectUri;

    public DeezerOAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String buildAuthUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://connect.deezer.com/oauth/auth.php")
                .queryParam("app_id", appId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("perms", "basic_access,email,manage_library")
                .build()
                .encode()
                .toUriString();
    }

    public String exchangeCode(String code) {
        String url = UriComponentsBuilder.fromHttpUrl("https://connect.deezer.com/oauth/access_token.php")
                .queryParam("app_id", appId)
                .queryParam("secret", secretKey)
                .queryParam("code", code)
                .queryParam("output", "json")
                .build()
                .encode()
                .toUriString();

        Map<?, ?> response = restTemplate.getForObject(url, Map.class);
        return response == null ? null : (String) response.get("access_token");
    }
}
