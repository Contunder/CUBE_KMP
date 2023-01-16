package com.example.cube.Controller;

import com.example.cube.Payload.CommentDto;
import com.example.cube.Service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{resourceId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "resourceId") long resourceId,
                                                    @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(resourceId, commentDto), HttpStatus.CREATED);
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
