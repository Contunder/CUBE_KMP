package com.example.cube.Repository;

import com.example.cube.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByResourceId(long resourceId);
}
