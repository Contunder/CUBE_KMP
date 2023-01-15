package com.example.cube.Service;

import com.example.cube.Payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long resourceId, CommentDto commentDto);

    List<CommentDto> getCommentsByResourceId(long resourceId);

    CommentDto getCommentById(Long resourceId, Long commentId);

    CommentDto updateComment(Long resourceId, long commentId, CommentDto commentRequest);

    void deleteComment(Long resourceId, Long commentId);
}
