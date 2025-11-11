package com.conduit_backend.comment.service;


import com.conduit_backend.comment.dto.CommentRequest;
import com.conduit_backend.comment.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByArticleSlug(String slug);
    CommentResponse addComment(String slug, CommentRequest request);
    void deleteComment(String slug, Long commentId);
}