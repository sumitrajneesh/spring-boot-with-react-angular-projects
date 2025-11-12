package com.conduit_backend.comment.service.impl;

import com.conduit_backend.article.entity.Article;
import com.conduit_backend.article.repository.ArticleRepository;
import com.conduit_backend.comment.dto.CommentRequest;
import com.conduit_backend.comment.dto.CommentResponse;
import com.conduit_backend.comment.entity.Comment;
import com.conduit_backend.comment.mapper.CommentMapper;
import com.conduit_backend.comment.repository.CommentRepository;
import com.conduit_backend.comment.service.CommentService;
import com.conduit_backend.profile.dto.ProfileDto;
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

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<CommentResponse> getCommentsByArticleSlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = getCurrentUser();

        return commentRepository.findByArticleOrderByCreatedAtDesc(article)
                .stream()
                .map(comment -> {
                    Profile authorProfile = profileRepository.findByUser(comment.getAuthor()).orElse(null);
                    boolean following = authorProfile != null && authorProfile.getFollowers().contains(currentUser);
                    return commentMapper.toDto(comment, following);
                })
                .collect(Collectors.toList());
    }

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

        Profile authorProfile = profileRepository.findByUser(currentUser).orElse(null);

        return commentMapper.toDto(comment, false);
    }

    @Override
    public void deleteComment(String slug, Long commentId) {
        Article article = articleRepository.findBySlug(slug)
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
