package com.example.cube.Payload;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Comment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ResourceDto {
    private long id;

    @NotEmpty
    private String access;

    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String value;

    private Set<Comment> comments;

    private Set<Catalogue> catalogue;

}
