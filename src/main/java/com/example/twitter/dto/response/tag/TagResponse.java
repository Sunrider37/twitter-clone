package com.example.twitter.dto.response.tag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagResponse {
    private Long id;
    private String tagName;
    private Long tweetsQuantity;
}