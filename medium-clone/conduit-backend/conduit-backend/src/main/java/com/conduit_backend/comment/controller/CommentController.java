package com.conduit_backend.comment.controller;

import com.conduit_backend.comment.dto.CommentRequest;
import com.conduit_backend.comment.dto.CommentResponse;
import com.conduit_backend.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{slug}/comments")
@Tag(name = "Comments", description = "Endpoints for managing article comments")
public class CommentController {

    private final CommentService commentService;

    // ======================
    // GET COMMENTS FOR ARTICLE
    // ======================
    @Operation(
            summary = "Get all comments for an article",
            description = "Returns a list of comments belonging to the given article slug.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Article Not Found")
            }
    )
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "Unique article slug", required = true)
            @PathVariable String slug
    ) {
        List<CommentResponse> comments = commentService.getCommentsByArticleSlug(slug);
        return ResponseEntity.ok(comments);
    }

    // ======================
    // ADD COMMENT (AUTH REQUIRED)
    // ======================
    @Operation(
            summary = "Add comment to article",
            description = "Adds a comment to an article. Requires authentication (JWT Bearer token).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment Added",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Article Not Found")
            }
    )
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @Parameter(description = "Article slug", required = true)
            @PathVariable String slug,

            @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.addComment(slug, request);
        return ResponseEntity.ok(response);
    }

    // ======================
    // DELETE COMMENT (ONLY OWNER)
    // ======================
    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment by ID. Only the owner of the comment can delete it.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Comment Deleted"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Only owner allowed"),
                    @ApiResponse(responseCode = "404", description = "Comment or Article Not Found")
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Article slug", required = true)
            @PathVariable String slug,

            @Parameter(description = "Unique comment ID", required = true)
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(slug, commentId);
        return ResponseEntity.noContent().build();
    }
}
