package com.conduit_backend.article.controller;

import com.conduit_backend.article.dto.ArticleCreateRequest;
import com.conduit_backend.article.dto.ArticleListResponse;
import com.conduit_backend.article.dto.ArticleResponse;
import com.conduit_backend.article.service.ArticleService;
import com.conduit_backend.config.JwtTokenProvider;
import com.conduit_backend.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<ArticleListResponse> getArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(articleService.getAllArticles(tag, limit, offset));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Map<String, ArticleResponse>> getArticleBySlug(@PathVariable String slug) {
        ArticleResponse article = articleService.getArticleBySlug(slug);
        return ResponseEntity.ok(Map.of("article", article));
    }

    @PostMapping
    public ResponseEntity<Map<String, ArticleResponse>> createArticle(
            @RequestBody ArticleCreateRequest articleRequest,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtTokenProvider.getUsernameFromToken(token);

        ArticleResponse articleResponse = articleService.createArticle(articleRequest, username);
        return ResponseEntity.ok(Map.of("article", articleResponse));
    }
}