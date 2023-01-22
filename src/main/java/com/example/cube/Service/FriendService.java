package com.example.cube.Service;

import com.example.cube.Payload.FriendDto;
import com.example.cube.Payload.FriendRequest;

import java.util.List;

public interface FriendService {
    List<FriendDto> getFriendsByUserEmail(String email);

    List<FriendDto> getActiveFriendsByUserEmail(String email);

    List<FriendRequest> getRequestFriendsByUserEmail(String email);

    List<FriendDto> getFriendsByRelation(String email, String relation);

    String setFriendsByEmail(String userEmail, String friendEmail, String relation);

    String setActiveFriendsByEmail(String friendEmail);
}
