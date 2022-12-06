package com.example.cube.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Friend {

    private int userId;
    private int friendId;
    private String relation;
}
