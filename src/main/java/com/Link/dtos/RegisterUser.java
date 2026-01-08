package com.Link.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterUser {
    private String userName;
    private String email;
    private Set<String> role;
    private String password;
}
