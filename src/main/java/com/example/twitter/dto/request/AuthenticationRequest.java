package com.example.twitter.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthenticationRequest {

    @Email( message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;
}