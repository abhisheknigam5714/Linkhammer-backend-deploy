package com.Link.repository;

import com.Link.models.URLMapping;
import com.Link.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<URLMapping,Long> {

    URLMapping findByShortUrl(String shortUrl);
    List<URLMapping> findByUser(User user);
}
