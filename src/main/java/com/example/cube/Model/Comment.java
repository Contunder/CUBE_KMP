package com.example.cube.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Comment {

    private int id;
    private int userId;
    private int resourceId;
    private int commentId;
    private String value;
    private boolean moderated;
}
