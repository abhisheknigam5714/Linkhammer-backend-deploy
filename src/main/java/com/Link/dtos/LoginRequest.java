package com.Link.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class LoginRequest {
    private String userName;

    private String password;
}