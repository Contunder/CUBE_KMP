package com.example.cube.Controller;

import com.example.cube.Payload.CommentDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resource/")
public class CommentController {

    private CommentService commentService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public CommentController(CommentService commentService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider) {
        this.commentService = commentService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/{resourceId}/comments")
    public ResponseEntity<CommentDto> createComment(HttpServletRequest request,
                                                    @PathVariable(value = "resourceId") long resourceId,
                                                    @Valid @RequestBody CommentDto commentDto){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(commentService.createComment(email, resourceId, commentDto), HttpStatus.CREATED);
    }

    @PostMapping("/{resourceId}/comments/response/{id}")
    public ResponseEntity<CommentDto> createResponseComment(HttpServletRequest request,
                                                    @PathVariable(value = "resourceId") long resourceId,
                                                    @PathVariable(value = "id") long commentId,
                                                    @Valid @RequestBody CommentDto commentDto){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(commentService.createResponseComment(email, resourceId, commentId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/{resourceId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "resourceId") Long resourceId){
        return commentService.getCommentsByResourceId(resourceId);
    }

    @GetMapping("/{resourceId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "resourceId") Long resourceId,
                                                     @PathVariable(value = "id") Long commentId){
        CommentDto commentDto = commentService.getCommentById(resourceId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/{resourceId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "resourceId") Long resourceId,
                                                    @PathVariable(value = "id") Long commentId,
                                                    @Valid @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(resourceId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{resourceId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "resourceId") Long resourceId,
                                                @PathVariable(value = "id") Long commentId){
        commentService.deleteComment(resourceId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
