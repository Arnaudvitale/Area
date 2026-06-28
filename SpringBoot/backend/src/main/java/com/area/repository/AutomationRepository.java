package com.area.repository;

import com.area.entity.Automation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutomationRepository extends JpaRepository<Automation, Long> {
    List<Automation> findByUserId(Long userId);
}
