package com.conduit_backend.article.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleUpdateRequest {
    private String title;
    private String description;
    private String body;
}

