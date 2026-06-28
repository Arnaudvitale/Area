package com.area.service;

import com.area.entity.User;
import com.area.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DeezerService implements AreaService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public DeezerService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public String getName() {
        return "Deezer";
    }

    @Override
    public Object execute(String action, Map<String, String> params) {
        String token = resolveToken(params.get("userEmail"));
        return switch (action) {
            case "checkNewMusicInPlaylist", "checkRemovedMusicInPlaylist" -> playlists(token);
            case "addMusicToPlaylist" -> addMusic(token, params);
            default -> Map.of("message", "Unknown action: " + action, "code", 0);
        };
    }

    private String resolveToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (user.getDeezerToken() == null) {
            throw new IllegalStateException("Deezer token not found for user");
        }
        return user.getDeezerToken();
    }

    private Object playlists(String token) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(
                "https://api.deezer.com/user/me/playlists?access_token=" + token, Map.class);
        int count = response == null ? 0 : ((List<?>) response.getOrDefault("data", List.of())).size();
        return Map.of("message", "Playlists checked", "count", count, "code", count > 0 ? 1 : 0);
    }

    private Object addMusic(String token, Map<String, String> params) {
        String playlistId = params.get("playlistId");
        String songs = params.get("songs");
        String url = "https://api.deezer.com/playlist/" + playlistId
                + "/tracks?songs=" + songs + "&access_token=" + token;
        restTemplate.postForObject(url, null, Map.class);
        return Map.of("message", "Music added", "code", 1);
    }
}
