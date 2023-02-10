package com.example.cube.Service.impl;

import com.example.cube.Exception.CubeAPIException;
import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Comment;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Payload.CommentDto;
import com.example.cube.Repository.CommentRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ResourceRepository resourceRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(String email, long resourceId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResourceNotFoundException("Resources", "id", resourceId));
        User user = userRepository.findUserByEmail(email);

        comment.setResource(resource);
        comment.setUser(user);

        Comment newComment =  commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public CommentDto createResponseComment(String email, long resourceId, long commentId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResourceNotFoundException("Resources", "id", resourceId));
        Comment relatedComment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));
        User user = userRepository.findUserByEmail(email);

        comment.setResource(resource);
        comment.setComment(relatedComment);
        comment.setUser(user);

        Comment newComment =  commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByResourceId(long resourceId) {
        List<Comment> comments = commentRepository.findByResourceId(resourceId);

        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long resourceId, Long commentId) {
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResourceNotFoundException("Resource", "id", resourceId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(comment.getResource().getId() != resource.getId()){
            throw new CubeAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to resource");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long resourceId, long commentId, CommentDto commentRequest) {

        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResourceNotFoundException("Resource", "id", resourceId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(comment.getResource().getId() != resource.getId()){
            throw new CubeAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to resource");
        }

        comment.setValue(commentRequest.getValue());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long resourceId, Long commentId) {
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResourceNotFoundException("Resource", "id", resourceId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(comment.getResource().getId() != resource.getId()){
            throw new CubeAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to resource");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment){
        comment.getUser().setPassword(null);
        comment.getUser().setRoles(null);
        comment.getUser().setFriends(null);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUser(comment.getUser());
        commentDto.setValue(comment.getValue());

        return  commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setUser(commentDto.getUser());
        comment.setValue(commentDto.getValue());

        return  comment;
    }
}
