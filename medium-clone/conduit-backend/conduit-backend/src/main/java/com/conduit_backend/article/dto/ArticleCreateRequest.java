package com.conduit_backend.article.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCreateRequest {
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
}

