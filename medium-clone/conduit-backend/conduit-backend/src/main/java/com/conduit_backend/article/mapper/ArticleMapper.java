package com.conduit_backend.article.mapper;

import com.conduit_backend.article.dto.ArticleResponse;
import com.conduit_backend.article.entity.Article;
import com.conduit_backend.tag.entity.Tag;
import com.conduit_backend.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    private final UserMapper userMapper;

    public ArticleMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ArticleResponse toArticleResponse(Article article) {
        List<String> tagNames = article.getTags()
                .stream()
                .map(Tag::getName)
                .toList();

        // ✅ Safe handling for possible nulls
        Instant createdAt = Optional.ofNullable(article.getCreatedAt())
                .map(Date::toInstant)
                .orElse(Instant.now());

        Instant updatedAt = Optional.ofNullable(article.getUpdatedAt())
                .map(Date::toInstant)
                .orElse(Instant.now());

        return ArticleResponse.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(tagNames)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .favorited(article.isFavorited())
                .favoritesCount(article.getFavoritesCount())
                .author(UserMapper.toUserResponse(article.getAuthor()))
                .build();
    }
}
