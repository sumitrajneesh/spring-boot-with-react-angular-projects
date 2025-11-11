package com.conduit_backend.article.dto;

import com.conduit_backend.user.dto.UserResponse;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponse {
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private UserResponse author;
}
