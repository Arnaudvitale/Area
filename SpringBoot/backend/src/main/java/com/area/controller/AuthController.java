package com.area.controller;

import com.area.entity.User;
import com.area.repository.UserRepository;
import com.area.service.oauth.DeezerOAuthService;
import com.area.service.oauth.GitHubOAuthService;
import com.area.service.oauth.GoogleOAuthService;
import com.area.service.oauth.GoogleTokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
public class AuthController {

    private final GoogleOAuthService googleOAuth;
    private final GitHubOAuthService gitHubOAuth;
    private final DeezerOAuthService deezerOAuth;
    private final GoogleTokenStore tokenStore;
    private final UserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    private String pendingDeezerEmail = "";

    public AuthController(GoogleOAuthService googleOAuth, GitHubOAuthService gitHubOAuth,
                          DeezerOAuthService deezerOAuth, GoogleTokenStore tokenStore,
                          UserRepository userRepository) {
        this.googleOAuth = googleOAuth;
        this.gitHubOAuth = gitHubOAuth;
        this.deezerOAuth = deezerOAuth;
        this.tokenStore = tokenStore;
        this.userRepository = userRepository;
    }

    @GetMapping("/auth")
    public Map<String, String> googleAuth() {
        return Map.of("url", googleOAuth.buildAuthUrl());
    }

    @GetMapping("/auth/google/callback")
    public RedirectView googleCallback(@RequestParam String code) {
        Map<String, Object> tokens = googleOAuth.exchangeCode(code);
        String accessToken = (String) tokens.get("access_token");
        tokenStore.set(accessToken, (String) tokens.get("refresh_token"));
        return new RedirectView(frontendUrl + "?token=" + accessToken);
    }

    @GetMapping("/auth/check")
    public Map<String, Object> check() {
        if (tokenStore.isAuthenticated()) {
            return Map.of("status", "success", "message", "User is authenticated",
                    "token", tokenStore.getAccessToken());
        }
        return Map.of("status", "error", "message", "User is not authenticated");
    }

    @GetMapping("/auth/logout")
    public Map<String, String> logout() {
        tokenStore.clear();
        return Map.of("status", "success", "message", "User logged out");
    }

    @GetMapping("/auth/github")
    public RedirectView githubAuth(@RequestParam String userEmail) {
        return new RedirectView(gitHubOAuth.buildAuthUrl(userEmail));
    }

    @GetMapping("/auth/github/callback")
    public RedirectView githubCallback(@RequestParam String code, @RequestParam String userEmail) {
        String token = gitHubOAuth.exchangeCode(code);
        userRepository.findByEmail(userEmail).ifPresent(user -> {
            user.setGitToken(token);
            userRepository.save(user);
        });
        return new RedirectView(frontendUrl + "/home?token=" + token);
    }

    @GetMapping("/auth/deezer")
    public Map<String, String> deezerAuth(@RequestParam String userEmail) {
        pendingDeezerEmail = userEmail;
        return Map.of("url", deezerOAuth.buildAuthUrl());
    }

    @GetMapping("/auth/deezer/callback")
    public RedirectView deezerCallback(@RequestParam String code) {
        String token = deezerOAuth.exchangeCode(code);
        if (!pendingDeezerEmail.isEmpty()) {
            userRepository.findByEmail(pendingDeezerEmail).ifPresent(user -> {
                user.setDeezerToken(token);
                userRepository.save(user);
            });
            pendingDeezerEmail = "";
        }
        return new RedirectView(frontendUrl + "/home?token=" + token);
    }
}
