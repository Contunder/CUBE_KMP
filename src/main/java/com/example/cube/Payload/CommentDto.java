package com.example.cube.Payload;

import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private long id;
    private User user;
    private Resource resource;

    @NotEmpty
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String value;
}
