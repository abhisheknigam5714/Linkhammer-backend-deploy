package com.Link.controller;


import com.Link.dtos.ClickEventDto;
import com.Link.dtos.UrlMappingDto;
import com.Link.models.User;
import com.Link.repository.UserRepository;
import com.Link.service.UrlMappingService;
import com.Link.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {

     private UrlMappingService urlMappingService;

     private UserService userService;
    private UserRepository userRepository;


     //"originalUrl: to short url redirection logic we have also write
     //we are adding principle because it will get the id of user to know who is creation which url for that
     @PostMapping("/shorten")
     @PreAuthorize("hasRole('USER')")// for another authentication
    public ResponseEntity<?> createShortenUrl(@RequestBody Map<String ,String> request, Principal principal){
         String originUrl= request.get("originalUrl");
         User user = urlMappingService.findByUsername(principal.getName());
        // call Userservice
         UrlMappingDto urlMappingDto = userService.creatShortUrl(originUrl,user);
         return ResponseEntity.ok(urlMappingDto);

     }


    @GetMapping ("/myurls")
    @PreAuthorize("hasRole('USER')")
    // get the user details from security context thats principal class is used
    public ResponseEntity<List<UrlMappingDto>> fetchUrls(Principal principal ){
        User user = userService.findByUsername(principal.getName());
       List<UrlMappingDto> urls= userService.getUrlByUser(user);
       return ResponseEntity.ok(urls);
    }// get the user details from security context


    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDto>> getUrlAnalytics(@PathVariable String shortUrl,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate){


        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start= LocalDateTime.parse(startDate,formatter);
        LocalDateTime end= LocalDateTime.parse(endDate,formatter);
        List<ClickEventDto> clickEventdtos = urlMappingService.getClickEventByDate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventdtos);
    }


    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")

   // we want the Data of user with whom user account  we are fetching
    public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate(Principal principal,
                                                                    @RequestParam("startDate") String startDate,
                                                                    @RequestParam("endDate") String endDate){



        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start= LocalDate.parse(startDate,formatter);
        LocalDate end= LocalDate.parse(endDate,formatter);
        User user= userRepository.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
        Map<LocalDate,Long> totalClicks=urlMappingService.getTotalClicksByUserAndDate(user,start,end);

    return ResponseEntity.ok(totalClicks);
    }




}

