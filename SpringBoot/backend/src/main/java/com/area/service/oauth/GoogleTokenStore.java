package com.area.service.oauth;

import org.springframework.stereotype.Component;

@Component
public class GoogleTokenStore {

    private String accessToken;
    private String refreshToken;

    public void set(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void clear() {
        this.accessToken = null;
        this.refreshToken = null;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public boolean isAuthenticated() { return accessToken != null; }
}
