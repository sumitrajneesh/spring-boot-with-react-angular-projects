package com.conduit_backend.article.service;

import com.conduit_backend.article.dto.ArticleCreateRequest;
import com.conduit_backend.article.dto.ArticleListResponse;
import com.conduit_backend.article.dto.ArticleResponse;

public interface ArticleService {
    ArticleResponse createArticle(ArticleCreateRequest request, String authorUsername);
    ArticleResponse getArticleBySlug(String slug);
    ArticleListResponse getAllArticles(String tag, int limit, int offset);
}

