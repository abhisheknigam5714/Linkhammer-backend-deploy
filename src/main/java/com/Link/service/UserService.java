package com.Link.service;


import com.Link.dtos.LoginRequest;
import com.Link.dtos.UrlMappingDto;
import com.Link.models.URLMapping;
import com.Link.models.User;
import com.Link.repository.UrlMappingRepository;
import com.Link.repository.UserRepository;
import com.Link.security.jwt.JwtAuthenticationResponse;
import com.Link.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtutils;
    private UrlMappingRepository urlMappingRepository;

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
         return userRepository.save(user);
    }

    public JwtAuthenticationResponse autheticatedLoginUser(LoginRequest logRequest){
        Authentication authetication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logRequest.getUserName(),
                logRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authetication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authetication.getPrincipal();
        String jwt = jwtutils.generateToken(userDetails);
     return new JwtAuthenticationResponse(jwt);



    }

    public UrlMappingDto creatShortUrl(String originalUrl,User user) {

        String shortUrl=generateShortUrl();
        URLMapping urlMapping = new URLMapping();


        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCreatedDate(LocalDateTime.now());
        URLMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    public UrlMappingDto convertToDto(URLMapping urlMapping){
        UrlMappingDto urlMappingDto = new UrlMappingDto();
        urlMappingDto.setId(urlMapping.getId());
        urlMappingDto.setShortUrl(urlMapping.getShortUrl());
        urlMappingDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDto.setClickCount(urlMapping.getClickCount());
        urlMappingDto.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDto.setUserName(urlMapping.getUser().getUsername());


        return urlMappingDto;
    }

    private String generateShortUrl() {
        String ch="ABCDEFGHIJKLMNOPQRSTUVWXYZabhcdefghijklmnopqrstuvwxyz0123456789";
        int length=10;
        Random random= new Random();
        StringBuilder shortUrl = new StringBuilder(length);
        for(int i=0;i<=length-1;i++){
            shortUrl.append(ch.charAt(random.nextInt(ch.length())));
        }
        return shortUrl.toString();
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(()-> new UsernameNotFoundException("Username not Found"));
    }

    public List<UrlMappingDto> getUrlByUser(User user) {
        return urlMappingRepository.findByUser(user).stream().map(this::convertToDto).toList();
    }
}
