package com.conduit_backend.comment.repository;

import com.conduit_backend.article.entity.Article;
import com.conduit_backend.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);
}
