package com.example.twitter.dto.request;

import com.example.twitter.model.BackgroundColorType;
import com.example.twitter.model.ColorSchemeType;
import lombok.Data;

@Data
public class SettingsRequest {
    private String username;
    private String email;
    private String countryCode;
    private Long phone;
    private String country;
    private String gender;
    private String language;
    private boolean mutedDirectMessages;
    private boolean privateProfile;
    private BackgroundColorType backgroundColor;
    private ColorSchemeType colorScheme;
}