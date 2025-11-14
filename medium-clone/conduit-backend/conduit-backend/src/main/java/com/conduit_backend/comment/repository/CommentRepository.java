package com.conduit_backend.comment.repository;

import com.conduit_backend.article.entity.Article;
import com.conduit_backend.comment.entity.Comment;
import com.conduit_backend.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 🔥 Get all comments for an article
    @EntityGraph(attributePaths = {"author"})
    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);

    // 🔥 Get single comment by id and article (safety for deletion)
    Comment findByIdAndArticle(Long id, Article article);

    // 🔥 Delete comment only if author matches
    void deleteByIdAndAuthor(Long id, User author);
}
