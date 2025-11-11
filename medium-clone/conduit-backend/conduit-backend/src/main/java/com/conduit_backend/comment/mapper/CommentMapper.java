package com.conduit_backend.comment.mapper;


import com.conduit_backend.comment.dto.CommentResponse;
import com.conduit_backend.comment.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponse toDto(Comment comment, boolean following) {
        CommentResponse.Author author = new CommentResponse.Author(
                comment.getAuthor().getUsername(),
                comment.getAuthor().getBio(),
                comment.getAuthor().getImage(),
                following
        );

        return new CommentResponse(
                comment.getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getBody(),
                author
        );
    }
}