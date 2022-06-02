package com.example.twitter.repository.projection.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipalProjection {
    private Long id;
    private String email;
    private String password;
    private String activationCode;
}