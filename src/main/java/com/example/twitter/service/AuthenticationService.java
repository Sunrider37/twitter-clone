package com.example.twitter.service;

import com.example.twitter.model.User;
import com.example.twitter.repository.projection.user.AuthUserProjection;

import javax.mail.MessagingException;
import java.util.Map;

public interface AuthenticationService {
    Long getAuthenticatedUserId();

    User getAuthenticatedUser();

    Map<String, Object> login(String email, String password);

    String registration(String email, String username, String birthday);

    String sendRegistrationCode(String email) throws MessagingException;

    String activateUser(String code);

    Map<String, Object> endRegistration(String email, String password);

    Map<String, Object> getUserByToken();

    String findEmail(String email);

    AuthUserProjection findByPasswordResetCode(String code);

    String sendPasswordResetCode(String email) throws MessagingException;

    String passwordReset(String email, String password, String password2);

    String currentPasswordReset(String currentPassword, String password, String password2);
}