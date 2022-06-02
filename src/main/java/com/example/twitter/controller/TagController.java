package com.example.twitter.controller;

import com.example.twitter.dto.response.TweetResponse;
import com.example.twitter.dto.response.tag.TagResponse;
import com.example.twitter.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getTags() {
        return ResponseEntity.ok(tagMapper.getTags());
    }

    @GetMapping("/trends")
    public ResponseEntity<List<TagResponse>> getTrends() {
        return ResponseEntity.ok(tagMapper.getTrends());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TweetResponse>> getTweetsByTag(@RequestParam String tagName) {
        return ResponseEntity.ok(tagMapper.getTweetsByTag(tagName));
    }
}