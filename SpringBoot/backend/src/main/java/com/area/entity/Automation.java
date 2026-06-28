package com.area.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "automations")
public class Automation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String label;

    @Column(length = 2000)
    private String description;

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "text")
    private List<Map<String, Object>> actions = new ArrayList<>();

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "text")
    private List<Map<String, Object>> reactions = new ArrayList<>();

    private boolean activated = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Map<String, Object>> getActions() { return actions; }
    public void setActions(List<Map<String, Object>> actions) { this.actions = actions; }

    public List<Map<String, Object>> getReactions() { return reactions; }
    public void setReactions(List<Map<String, Object>> reactions) { this.reactions = reactions; }

    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }
}
