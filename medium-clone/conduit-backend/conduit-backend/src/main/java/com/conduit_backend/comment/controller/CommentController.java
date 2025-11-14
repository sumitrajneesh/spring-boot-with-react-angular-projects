package com.conduit_backend.comment.controller;

import com.conduit_backend.comment.dto.CommentRequest;
import com.conduit_backend.comment.dto.CommentResponse;
import com.conduit_backend.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {

    private final CommentService commentService;

    // ======================
    // GET COMMENTS FOR ARTICLE
    // ======================
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String slug) {
        List<CommentResponse> comments = commentService.getCommentsByArticleSlug(slug);
        return ResponseEntity.ok(comments);
    }

    // ======================
    // ADD COMMENT (AUTH REQUIRED)
    // ======================
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable String slug,
            @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.addComment(slug, request);
        return ResponseEntity.ok(response);
    }

    // ======================
    // DELETE COMMENT (ONLY OWNER)
    // ======================
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String slug,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(slug, commentId);
        return ResponseEntity.noContent().build();
    }
}
