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

import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private ResourceRepository resourceRepository;
    private UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ResourceRepository resourceRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(long resourceId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResourceNotFoundException("Resources", "id", resourceId));
        User user = userRepository.findByEmail("valentin.denavaut@hotmail.fr").orElseThrow(
                () -> new ResourceNotFoundException("Resources", "id", resourceId));

        comment.setResource(resource);
        comment.setUser(user);

        Comment newComment =  commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByResourceId(long resourceId) {
        List<Comment> comments = commentRepository.findByResourceId(resourceId);

        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
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
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setValue(comment.getValue());

        return  commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setValue(commentDto.getValue());

        return  comment;
    }
}
