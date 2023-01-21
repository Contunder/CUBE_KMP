package com.example.cube.Payload;

import com.example.cube.Model.User;
import lombok.Data;

@Data
public class FriendDto {
    private long id;
    private User friend;
    private String relation;
    private boolean isActive;
}
