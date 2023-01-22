package com.example.cube.Service.impl;

import com.example.cube.Model.Friend;
import com.example.cube.Model.User;
import com.example.cube.Payload.FriendDto;
import com.example.cube.Repository.FriendRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.FriendService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    private FriendRepository friendRepository;
    private UserRepository userRepository;

    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository){
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FriendDto> getFriendsByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        List<Friend> friend = friendRepository.getFriendsByUser(user);

        return friend.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<FriendDto> getActiveFriendsByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        List<Friend> friend = friendRepository.getFriendsByUser(user);

        return friend.stream()
                .map(this::mapToDTO)
                .filter(FriendDto::isActive)
                .toList();
    }

    @Override
    public List<FriendDto> getFriendsByRelation(String email, String relation) {
        User user = userRepository.findUserByEmail(email);
        List<Friend> friends = friendRepository.getFriendsByUser(user);

        return friends.stream()
                .map(this::mapToDTO)
                .filter(friendDto -> friendDto.getRelation().equals(relation))
                .toList();
    }

    private FriendDto mapToDTO(Friend friend){
        friend.getFriend().setPassword(null);
        friend.getFriend().setRoles(null);
        friend.getFriend().setFriends(null);

        FriendDto friendDto = new FriendDto();
        friendDto.setFriend(friend.getFriend());
        friendDto.setRelation(friend.getRelation());
        friendDto.setActive(friend.isActive());

        return friendDto;
    }
}
