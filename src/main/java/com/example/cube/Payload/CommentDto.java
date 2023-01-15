package com.example.cube.Payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private long id;

    @NotEmpty
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String value;
}
