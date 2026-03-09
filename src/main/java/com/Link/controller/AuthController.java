package com.Link.controller;


import com.Link.dtos.LoginRequest;
import com.Link.dtos.RegisterUser;
import com.Link.models.User;
import com.Link.security.jwt.JwtAuthenticationResponse;
import com.Link.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
  private UserService userService;


    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest logRequest){
        JwtAuthenticationResponse jwtAuthenticationResponse = userService.autheticatedLoginUser(logRequest);
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }


    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser registerUser){
        System.out.println(registerUser.toString());
        User user= new User();
        user.setUsername(registerUser.getUserName());
        user.setEmail(registerUser.getEmail());
        user.setRole("ROLE_USER");
        user.setPassword(registerUser.getPassword());
        userService.registerUser(user);
        return ResponseEntity.ok("user registered successfully");
    }

}
