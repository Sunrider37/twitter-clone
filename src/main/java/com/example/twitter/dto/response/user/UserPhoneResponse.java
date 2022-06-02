package com.example.twitter.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPhoneResponse {
    private String countryCode;
    private Long phone;
}