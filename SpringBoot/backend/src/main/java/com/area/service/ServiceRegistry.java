package com.area.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServiceRegistry {

    private final Map<String, AreaService> services;

    public ServiceRegistry(List<AreaService> serviceBeans) {
        this.services = serviceBeans.stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), s -> s));
    }

    public Optional<AreaService> find(String name) {
        return Optional.ofNullable(services.get(name.toLowerCase()));
    }
}
