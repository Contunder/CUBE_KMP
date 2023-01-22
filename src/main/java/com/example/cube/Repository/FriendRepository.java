package com.example.cube.Repository;

import com.example.cube.Model.Friend;
import com.example.cube.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, User> {
    List<Friend> getFriendsByUser(User user);
    List<Friend> getFriendsRequestByUser(User user);
    Friend getUserRequestByFriend(User user);

}
