package com.conduit_backend.article.service.impl;

import com.conduit_backend.article.dto.*;
import com.conduit_backend.article.entity.Article;
import com.conduit_backend.article.mapper.ArticleMapper;
import com.conduit_backend.article.repository.ArticleRepository;
import com.conduit_backend.tag.entity.Tag;
import com.conduit_backend.tag.repository.TagRepository;
import com.conduit_backend.user.entity.User;
import com.conduit_backend.user.repository.UserRepository;
import com.conduit_backend.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleResponse createArticle(ArticleCreateRequest request, String authorUsername) {
        User author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Tag> tags = new HashSet<>();
        if (request.getTagList() != null) {
            for (String tagName : request.getTagList()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
                tags.add(tag);
            }
        }

        Article article = Article.builder()
                .slug(generateSlug(request.getTitle()))
                .title(request.getTitle())
                .description(request.getDescription())
                .body(request.getBody())
                .tags(tags)
                .author(author)
                .favorited(false)
                .favoritesCount(0)
                .build();

        return articleMapper.toArticleResponse(articleRepository.save(article));
    }

    @Override
    public ArticleResponse getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return articleMapper.toArticleResponse(article);
    }

    @Override
    public ArticleListResponse getAllArticles(String tag, int limit, int offset) {
        List<Article> articles = (tag != null)
                ? articleRepository.findByTagName(tag)
                : articleRepository.findAll();

        List<ArticleResponse> articleResponses = articles.stream()
                .skip(offset)
                .limit(limit)
                .map(articleMapper::toArticleResponse)
                .collect(Collectors.toList());

        return ArticleListResponse.builder()
                .articles(articleResponses)
                .articlesCount(articles.size())
                .build();
    }

    private String generateSlug(String title) {
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        return normalized.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}