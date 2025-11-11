package com.conduit_backend.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private Author author;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Author {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }

    // For RealWorld API response spec
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentListWrapper {
        private List<CommentResponse> comments;
    }
}