package com.example.cube.Service.impl;

import com.example.cube.Model.*;
import com.example.cube.Payload.CommentDto;
import com.example.cube.Repository.CommentRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private CommentDto commentDto;
    private Comment comment;
    private Resource resource;

    @BeforeEach
    void setUp() {
        Set<Role>roles = new HashSet<>();
        roles.add(new Role());
        Set<Friend>friends = new HashSet<>();
        friends.add(new Friend());

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("");
        user.setRoles(roles);
        user.setFriends(friends);

        resource = new Resource();
        resource.setId(1L);

        commentDto = new CommentDto();
        commentDto.setId(1l);
        commentDto.setUser(user);
        commentDto.setValue("comment");
        comment = new Comment();
        comment.setId(1l);
        comment.setUser(user);
        comment.setValue("comment");
        comment.setResource(resource);
    }

    @Test
    void testCreateComment() {
        // Arrange
        String email = "test@example.com";
        long resourceId = 1L;
        Resource resource = new Resource();
        resource.setId(resourceId);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(userRepository.findUserByEmail(any())).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDto result = commentService.createComment(email, resourceId, commentDto);

        // Assert
        assertSame(commentDto.getValue(), result.getValue());
        assertSame(commentDto.getId(), result.getId());
        assertSame(commentDto.getUser(), result.getUser());
    }

    @Test
    void testCreateResponseComment() {
        // Arrange
        String email = "test@example.com";
        long resourceId = 1L;
        long commentId = 2L;
        Resource resource = new Resource();
        Comment relatedComment = new Comment();

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(relatedComment));
        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDto result = commentService.createResponseComment(email, resourceId, commentId, commentDto);

        // Assert
        assertSame(commentDto.getValue(), result.getValue());
        assertSame(commentDto.getId(), result.getId());
        assertSame(commentDto.getUser(), result.getUser());
    }

    @Test
    void testGetCommentsByResourceId() {
        // Arrange
        long resourceId = 1L;
        Comment comment1 = new Comment();
        comment1.setUser(user);
        Comment comment2 = new Comment();
        comment2.setUser(user);
        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByResourceId(resourceId)).thenReturn(comments);

        // Act
        List<CommentDto> result = commentService.getCommentsByResourceId(resourceId);

        // Assert
        assertEquals(2, result.size());
        assertSame(comment1.getUser(), result.get(0).getUser());
        assertSame(comment2.getUser(), result.get(1).getUser());
    }

    @Test
    void testGetCommentById() {
        // Arrange
        long resourceId = 1L;
        long commentId = 2L;

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        CommentDto result = commentService.getCommentById(resourceId, commentId);

        // Assert
        assertSame(commentDto.getValue(), result.getValue());
        assertSame(commentDto.getId(), result.getId());
        assertSame(commentDto.getUser(), result.getUser());
    }

    @Test
    void testUpdateComment() {
        // Arrange
        long resourceId = 1L;
        long commentId = 2L;

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDto result = commentService.updateComment(resourceId, commentId, commentDto);

        // Assert
        assertSame(commentDto.getValue(), result.getValue());
        assertSame(commentDto.getId(), result.getId());
        assertSame(commentDto.getUser(), result.getUser());
    }

    @Test
    void testDeleteComment() {
        // Arrange
        long resourceId = 1L;
        long commentId = 1L;

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(resourceId, commentId);

        // Assert
        verify(commentRepository).delete(comment);
    }
}