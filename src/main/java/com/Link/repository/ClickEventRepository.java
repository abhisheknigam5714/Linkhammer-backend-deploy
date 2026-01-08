package com.Link.repository;

import com.Link.dtos.ClickEventDto;
import com.Link.models.ClickEvent;
import com.Link.models.URLMapping;
import com.Link.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent,Long> {

   List<ClickEventDto>  findByUrlMappingAndClickDateBetween(URLMapping urlMapping, LocalDateTime start, LocalDateTime end);
}
