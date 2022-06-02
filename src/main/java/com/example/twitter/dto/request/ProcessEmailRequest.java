package com.example.twitter.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ProcessEmailRequest {
    @Email(message = "Please enter a valid email address.")
    private String email;
}