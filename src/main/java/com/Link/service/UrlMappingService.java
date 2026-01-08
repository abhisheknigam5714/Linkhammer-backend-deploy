package com.Link.service;

import com.Link.dtos.ClickEventDto;
import com.Link.models.ClickEvent;
import com.Link.models.URLMapping;
import com.Link.models.User;
import com.Link.repository.ClickEventRepository;
import com.Link.repository.UrlMappingRepository;
import com.Link.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {
    private UserRepository userRepository;
    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;
    public User findByUsername(String name) {

        return  userRepository.findByUsername(name)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found Exception"));
    }


    public List<ClickEventDto> getClickEventByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        URLMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping!=null){
            return    clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping,start,end).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate(),Collectors.counting()))
                    .entrySet().stream().map(entry->{
                        ClickEventDto clickEventDto= new ClickEventDto();
                        clickEventDto.setClickDate(entry.getKey());
                        clickEventDto.setCount(entry.getValue());
                        return clickEventDto;
                    }).collect(Collectors.toList());
        }
        return null;
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {

        List<URLMapping> urlMapping = urlMappingRepository.findByUser(user);

        List<ClickEventDto> clickEvents = clickEventRepository.findByUrlMappingAndClickDateBetween((URLMapping) urlMapping, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream().collect(Collectors.groupingBy(click-> click.getClickDate(),Collectors.counting()));
    }

    public URLMapping getOriginalUrl(String shortUrl) {
        URLMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if (urlMapping!=null){
            urlMapping.setClickCount(urlMapping.getClickCount()+1);
            urlMappingRepository.save(urlMapping);



            //Recode the ClickEvents
            ClickEvent clickEvent= new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }
        return urlMapping;
    }
}
