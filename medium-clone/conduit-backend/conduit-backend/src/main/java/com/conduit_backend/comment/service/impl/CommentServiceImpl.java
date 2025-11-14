package com.conduit_backend.comment.service.impl;

import com.conduit_backend.article.entity.Article;
import com.conduit_backend.article.repository.ArticleRepository;
import com.conduit_backend.comment.dto.CommentRequest;
import com.conduit_backend.comment.dto.CommentResponse;
import com.conduit_backend.comment.entity.Comment;
import com.conduit_backend.comment.mapper.CommentMapper;
import com.conduit_backend.comment.repository.CommentRepository;
import com.conduit_backend.comment.service.CommentService;
import com.conduit_backend.profile.entity.Profile;
import com.conduit_backend.profile.respository.ProfileRepository;
import com.conduit_backend.user.entity.User;
import com.conduit_backend.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CommentMapper commentMapper;

    // ============================================================
    // ✔ CORRECT METHOD TO FETCH LOGGED-IN USER FROM JWT
    // ============================================================
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ============================================================
    // ✔ GET ALL COMMENTS FOR ARTICLE
    // ============================================================
    @Override
    public List<CommentResponse> getCommentsByArticleSlug(String slug) {

        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = getCurrentUser();

        return commentRepository.findByArticleOrderByCreatedAtDesc(article)
                .stream()
                .map(comment -> {

                    boolean following = profileRepository
                            .findByUser(comment.getAuthor())
                            .map(p -> p.getFollowers().contains(currentUser))
                            .orElse(false);

                    return commentMapper.toDto(comment, following);
                })
                .collect(Collectors.toList());
    }

    // ============================================================
    // ✔ CREATE COMMENT (AUTH REQUIRED)
    // ============================================================
    @Override
    public CommentResponse addComment(String slug, CommentRequest request) {

        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = getCurrentUser();

        Comment comment = Comment.builder()
                .article(article)
                .author(currentUser)
                .body(request.getBody())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return commentMapper.toDto(comment, false); // following=false since you posted it
    }

    // ============================================================
    // ✔ DELETE COMMENT — ONLY COMMENT AUTHOR CAN DELETE
    // ============================================================
    @Override
    public void deleteComment(String slug, Long commentId) {

        articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User currentUser = getCurrentUser();

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can delete only your own comments");
        }

        commentRepository.delete(comment);
    }
}
