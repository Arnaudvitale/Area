package com.area.controller;

import com.area.service.ServiceRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceController {

    private final ServiceRegistry registry;

    public ServiceController(ServiceRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/api/{service}/{action}")
    public ResponseEntity<?> call(@PathVariable String service,
                                  @PathVariable String action,
                                  @RequestParam Map<String, String> params) {
        return registry.find(service)
                .<ResponseEntity<?>>map(s -> ResponseEntity.ok(s.execute(action, params)))
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(Map.of("status", "error", "message", "Unknown service: " + service)));
    }
}
