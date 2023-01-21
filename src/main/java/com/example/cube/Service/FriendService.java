package com.example.cube.Service;

import com.example.cube.Payload.FriendDto;

import java.util.List;

public interface FriendService {
    List<FriendDto> getFriendsByUserEmail(String email);

    List<FriendDto> getActiveFriendsByUserEmail(String email);

    List<FriendDto> getFriendsByRelation(String email, String relation);
}
