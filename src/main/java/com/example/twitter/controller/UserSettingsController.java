package com.example.twitter.controller;

import com.example.twitter.dto.request.SettingsRequest;
import com.example.twitter.dto.response.AuthenticationResponse;
import com.example.twitter.dto.response.user.UserPhoneResponse;
import com.example.twitter.mapper.UserMapper;
import com.example.twitter.model.BackgroundColorType;
import com.example.twitter.model.ColorSchemeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings")
public class UserSettingsController {

    private final UserMapper userMapper;

    @PutMapping("/update/username")
    public ResponseEntity<String> updateUsername(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateUsername(request));
    }

    @PutMapping("/update/email")
    public ResponseEntity<AuthenticationResponse> updateEmail(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateEmail(request));
    }

    @PutMapping("/update/phone")
    public ResponseEntity<UserPhoneResponse> updatePhone(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updatePhone(request));
    }

    @PutMapping("/update/country")
    public ResponseEntity<String> updateCountry(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateCountry(request));
    }

    @PutMapping("/update/gender")
    public ResponseEntity<String> updateGender(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateGender(request));
    }

    @PutMapping("/update/language")
    public ResponseEntity<String> updateLanguage(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateLanguage(request));
    }

    @PutMapping("/update/direct")
    public ResponseEntity<Boolean> updateDirectMessageRequests(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateDirectMessageRequests(request));
    }

    @PutMapping("/update/private")
    public ResponseEntity<Boolean> updatePrivateProfile(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updatePrivateProfile(request));
    }

    @PutMapping("/update/color_scheme")
    public ResponseEntity<ColorSchemeType> updateColorScheme(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateColorScheme(request));
    }

    @PutMapping("/update/background_color")
    public ResponseEntity<BackgroundColorType> updateBackgroundColor(@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(userMapper.updateBackgroundColor(request));
    }
}