package com.conduit_backend.tag.controller;

import com.conduit_backend.tag.dto.TagResponse;
import com.conduit_backend.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<TagResponse> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }
}

