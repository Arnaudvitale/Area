package com.area.controller;

import com.area.entity.Automation;
import com.area.entity.User;
import com.area.repository.AutomationRepository;
import com.area.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AutomationController {

    private final AutomationRepository automationRepository;
    private final UserRepository userRepository;

    public AutomationController(AutomationRepository automationRepository, UserRepository userRepository) {
        this.automationRepository = automationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/automations")
    public List<Automation> all() {
        return automationRepository.findAll();
    }

    @GetMapping("/automations/{userEmail}")
    public List<Automation> byUser(@PathVariable String userEmail) {
        return userRepository.findByEmail(userEmail)
                .map(user -> automationRepository.findByUserId(user.getId()))
                .orElseGet(List::of);
    }

    @GetMapping("/automation/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return automationRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(Map.of("status", "error", "message", "No automation found")));
    }

    @PostMapping("/automation/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Automation payload) {
        return automationRepository.findById(id).map(automation -> {
            automation.setLabel(payload.getLabel());
            automation.setDescription(payload.getDescription());
            automation.setActions(payload.getActions());
            automation.setReactions(payload.getReactions());
            automation.setActivated(payload.isActivated());
            automationRepository.save(automation);
            return ResponseEntity.ok(Map.of("automation", "automation updated successfully"));
        }).orElseGet(() -> ResponseEntity.badRequest().body(Map.of("error", "Update not possible")));
    }

    @DeleteMapping("/automation/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!automationRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Automation not found.");
        }
        automationRepository.deleteById(id);
        return ResponseEntity.ok("Automation deleted.");
    }

    @PostMapping("/automations/add")
    public ResponseEntity<?> add(@RequestParam String userEmail, @RequestBody Automation payload) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("adding new automation failed");
        }
        payload.setId(null);
        payload.setUserId(user.getId());
        automationRepository.save(payload);
        return ResponseEntity.ok(Map.of("automation", "automation added successfully"));
    }

    @GetMapping("/automations/actions")
    public List<Map<String, Object>> actions(@RequestParam String userEmail) {
        List<Map<String, Object>> actions = new ArrayList<>();
        userRepository.findByEmail(userEmail).ifPresent(user ->
                automationRepository.findByUserId(user.getId())
                        .forEach(automation -> actions.addAll(automation.getActions())));
        return actions;
    }
}
