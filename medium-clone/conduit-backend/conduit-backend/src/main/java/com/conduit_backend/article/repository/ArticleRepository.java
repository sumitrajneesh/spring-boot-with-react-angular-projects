package com.conduit_backend.article.repository;

import com.conduit_backend.article.entity.Article;
import com.conduit_backend.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE (:tag IS NULL OR t.name = :tag)")
    List<Article> findByTagName(String tag);
}
