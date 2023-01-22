package com.example.cube.Payload;

import com.example.cube.Model.User;
import lombok.Data;

@Data
public class FriendRequest {

    private long id;
    private User user;
    private String relation;
    private boolean isActive;
}
