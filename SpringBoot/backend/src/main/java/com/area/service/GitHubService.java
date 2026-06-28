package com.area.service;

import com.area.entity.User;
import com.area.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GitHubService implements AreaService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public GitHubService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public String getName() {
        return "GitHub";
    }

    @Override
    public Object execute(String action, Map<String, String> params) {
        String token = resolveToken(params.get("userEmail"));
        return switch (action) {
            case "checkNewCommits" -> latest(token, "commits");
            case "checkNewPullRequests" -> latest(token, "pulls");
            case "checkNewIssues" -> latest(token, "issues");
            case "postAutomatedComment" -> postComment(token, params);
            default -> Map.of("message", "Unknown action: " + action, "code", 0);
        };
    }

    private String resolveToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (user.getGitToken() == null) {
            throw new IllegalStateException("GitHub token not found for user");
        }
        return user.getGitToken();
    }

    private HttpEntity<Void> auth(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + token);
        return new HttpEntity<>(headers);
    }

    @SuppressWarnings("unchecked")
    private Object latest(String token, String resource) {
        List<Map<String, Object>> repos = restTemplate.exchange(
                "https://api.github.com/user/repos",
                HttpMethod.GET, auth(token), List.class).getBody();

        int found = 0;
        if (repos != null) {
            for (Map<String, Object> repo : repos) {
                Map<String, Object> owner = (Map<String, Object>) repo.get("owner");
                String url = "https://api.github.com/repos/" + owner.get("login")
                        + "/" + repo.get("name") + "/" + resource;
                List<?> items = restTemplate.exchange(url, HttpMethod.GET, auth(token), List.class).getBody();
                if (items != null) {
                    found += items.size();
                }
            }
        }
        return Map.of("message", resource + " checked", "count", found, "code", found > 0 ? 1 : 0);
    }

    private Object postComment(String token, Map<String, String> params) {
        String owner = params.get("owner");
        String repo = params.get("repo");
        String issueNumber = params.get("issueNumber");
        String url = "https://api.github.com/repos/" + owner + "/" + repo
                + "/issues/" + issueNumber + "/comments";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + token);
        Map<String, String> body = Map.of("body", "Automated comment from AREA.");
        restTemplate.postForObject(url, new HttpEntity<>(body, headers), Map.class);
        return Map.of("message", "Comment posted", "code", 1);
    }
}
