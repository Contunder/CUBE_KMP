package com.example.cube.Service;

import com.example.cube.Payload.FriendDto;
import com.example.cube.Payload.FriendRequest;

import java.util.List;

public interface FriendService {
    List<FriendDto> getFriendsByUserEmail(String email);

    List<FriendDto> getActiveFriendsByUserEmail(String email);

    List<FriendDto> getActiveFriendsByFriendId(String email, long friendId);

    List<FriendRequest> getRequestFriendsByUserEmail(String email);

    List<FriendDto> getFriendsByRelation(String email, String relation);

    String setFriendsById(String userEmail, long friendId, String relation);

    String setActiveFriendsById(String userEmail, long friendId, String relation);
}
