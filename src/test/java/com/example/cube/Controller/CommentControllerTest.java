package com.example.cube.Controller;

import com.example.cube.Payload.CommentDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Collections;
import java.util.List;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private CommentController commentController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController(commentService, jwtAuthenticationFilter, jwtTokenProvider);
    }

    @Test
    void testCreateComment() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "token";
        String email = "test@example.com";
        long resourceId = 1L;
        CommentDto commentDto = new CommentDto();
        CommentDto createdCommentDto = new CommentDto();
        Mockito.when(jwtAuthenticationFilter.getTokenFromRequest(request)).thenReturn(token);
        Mockito.when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        Mockito.when(commentService.createComment(email, resourceId, commentDto)).thenReturn(createdCommentDto);

        // Act
        ResponseEntity<CommentDto> response = commentController.createComment(request, resourceId, commentDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(createdCommentDto, response.getBody());
        Mockito.verify(jwtAuthenticationFilter).getTokenFromRequest(request);
        Mockito.verify(jwtTokenProvider).getUsername(token);
        Mockito.verify(commentService).createComment(email, resourceId, commentDto);
    }

    @Test
    void testGetCommentsByPostId() {
        // Arrange
        long resourceId = 1L;
        List<CommentDto> commentDtos = Collections.singletonList(new CommentDto());
        Mockito.when(commentService.getCommentsByResourceId(resourceId)).thenReturn(commentDtos);

        // Act
        List<CommentDto> response = commentController.getCommentsByPostId(resourceId);

        // Assert
        Assertions.assertEquals("test", response);
        Mockito.verify(commentService).getCommentsByResourceId(resourceId);
    }
}
