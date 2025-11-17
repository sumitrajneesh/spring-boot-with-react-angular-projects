package com.conduit_backend.article.controller;

import com.conduit_backend.article.dto.ArticleCreateRequest;
import com.conduit_backend.article.dto.ArticleListResponse;
import com.conduit_backend.article.dto.ArticleResponse;
import com.conduit_backend.article.service.ArticleService;
import com.conduit_backend.config.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Endpoints for creating, reading, listing articles")
public class ArticleController {

    private final ArticleService articleService;
    private final JwtTokenProvider jwtTokenProvider;

    // =========================
    // GET ALL ARTICLES
    // =========================
    @Operation(
            summary = "Get all articles",
            description = "Returns a list of articles. Supports filtering by tag and pagination.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of articles",
                            content = @Content(schema = @Schema(implementation = ArticleListResponse.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ArticleListResponse> getArticles(
            @Parameter(description = "Filter by tag") @RequestParam(required = false) String tag,
            @Parameter(description = "Limit number of results", example = "10")
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0") int offset) {

        return ResponseEntity.ok(articleService.getAllArticles(tag, limit, offset));
    }

    // =========================
    // GET ARTICLE BY SLUG
    // =========================
    @Operation(
            summary = "Get article by slug",
            description = "Returns a single article identified by its slug.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Article found",
                            content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Article not found")
            }
    )
    @GetMapping("/{slug}")
    public ResponseEntity<Map<String, ArticleResponse>> getArticleBySlug(
            @Parameter(description = "Unique article slug", required = true)
            @PathVariable String slug) {

        ArticleResponse article = articleService.getArticleBySlug(slug);
        return ResponseEntity.ok(Map.of("article", article));
    }

    // =========================
    // CREATE ARTICLE (AUTH REQUIRED)
    // =========================
    @Operation(
            summary = "Create a new article",
            description = "Creates a new article. Requires JWT Bearer token in the Authorization header.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Article created",
                            content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping
    public ResponseEntity<Map<String, ArticleResponse>> createArticle(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Article create request",
                    content = @Content(schema = @Schema(implementation = ArticleCreateRequest.class)))
            @RequestBody ArticleCreateRequest articleRequest,

            @Parameter(description = "JWT Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtTokenProvider.getUsernameFromToken(token);

        ArticleResponse articleResponse = articleService.createArticle(articleRequest, username);
        return ResponseEntity.ok(Map.of("article", articleResponse));
    }
}