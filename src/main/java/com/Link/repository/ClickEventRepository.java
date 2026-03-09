package com.Link.repository;

import com.Link.models.ClickEvent;
import com.Link.models.URLMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    // ✅ Returns List<ClickEvent> not List<ClickEventDto>
    List<ClickEvent> findByUrlMappingAndClickDateBetween(
            URLMapping urlMapping,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // ✅ NEW METHOD - for totalClicks
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(
            List<URLMapping> urlMappings,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}