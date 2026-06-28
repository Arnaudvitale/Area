package com.area.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AboutController {

    @GetMapping("/about.json")
    public Map<String, Object> about(HttpServletRequest request) {
        return Map.of(
                "client", Map.of("host", clientIp(request)),
                "server", Map.of(
                        "current_time", System.currentTimeMillis(),
                        "services", services()));
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("x-forwarded-for");
        String ip = forwarded != null ? forwarded : request.getRemoteAddr();
        return ip != null && ip.startsWith("::ffff:") ? ip.substring(7) : ip;
    }

    private Object action(String name, String description) {
        return Map.of("name", name, "description", description);
    }

    private List<Object> services() {
        return List.of(
                Map.of("name", "Calendar", "actions", List.of(
                        action("checkNewEvent", "Check if a new event has been added to a calendar"))),
                Map.of("name", "Drive", "actions", List.of(
                        action("checkNewFiles", "Check if new files have been added to a folder"),
                        action("checkFileDescription", "Check if a file description has been modified"))),
                Map.of("name", "Gmail",
                        "actions", List.of(action("checkNewEmails", "Check if new emails have been received")),
                        "reactions", List.of(action("sendEmail", "Send an email"))),
                Map.of("name", "GitHub",
                        "actions", List.of(
                                action("checkNewCommits", "Check if new commits have been pushed"),
                                action("checkNewPullRequests", "Check if new pull requests have been created"),
                                action("checkNewIssues", "Check if new issues have been created")),
                        "reactions", List.of(action("postAutomatedComment", "Post an automated comment"))),
                Map.of("name", "Youtube", "actions", List.of(
                        action("checkNewSubcribers", "Check if new subscribers have subscribed"),
                        action("checkLostSubcribers", "Check if subscribers have unsubscribed"),
                        action("checkMostPopularVideo", "Check if a video has become the most popular"))),
                Map.of("name", "Deezer",
                        "actions", List.of(
                                action("checkNewMusicInPlaylist", "Check if new music has been added to a playlist"),
                                action("checkRemovedMusicInPlaylist", "Check if music has been removed from a playlist")),
                        "reactions", List.of(action("addMusicToPlaylist", "Add music to a playlist"))));
    }
}
