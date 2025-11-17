package com.conduit_backend.tag.controller;

import com.conduit_backend.tag.dto.TagResponse;
import com.conduit_backend.tag.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(
        name = "Tags API",
        description = "Endpoints for retrieving all available tags"
)
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "Get all tags",
            description = "Returns the list of all tags available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tags fetched successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TagResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<TagResponse> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }
}