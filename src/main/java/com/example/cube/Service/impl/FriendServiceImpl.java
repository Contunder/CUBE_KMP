package com.example.cube.Service.impl;

import com.example.cube.Model.Friend;
import com.example.cube.Model.User;
import com.example.cube.Payload.FriendDto;
import com.example.cube.Payload.FriendRequest;
import com.example.cube.Repository.FriendRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.FriendService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
    public List<FriendDto> getActiveFriendsByFriendId(String email, long friendId) {
        User user = userRepository.findUserByEmail(email);
        List<Friend> friend = friendRepository.getFriendsByUser(user);

        return friend.stream()
                .map(this::mapToDTO)
                .filter(friendDto -> friendDto.getFriend().getId() == friendId)
                .filter(FriendDto::isActive)
                .toList();
    }

    @Override
    public List<FriendRequest> getRequestFriendsByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        List<Friend> friendRequest = friendRepository.getFriendsRequestByFriend(user);

        return friendRequest.stream()
                .map(this::mapRequestToDTO)
                .filter(FriendRequest -> !FriendRequest.isActive())
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

    @Override
    public String setFriendsById(String userEmail, long friendId, String relation) {
        User user = userRepository.findUserByEmail(userEmail);
        User friend = userRepository.findUserById(friendId);

        Friend newFriend = new Friend();
        newFriend.setUser(user);
        newFriend.setFriend(friend);
        newFriend.setRelation(relation);
        newFriend.setActive(false);

        friendRepository.save(newFriend);

        return "Friend registered successfully!.";
    }

    @Override
    public String setActiveFriendsById(String userEmail, long friendId, String relation) {

        User user = userRepository.findUserByEmail(userEmail);
        List<Friend> friendRequest = friendRepository.getFriendsRequestByFriend(user);
        Optional<Friend> providerFriend = friendRequest.stream().filter(friend -> friend.getUser().getId() == friendId).findFirst();

        User friend = userRepository.findUserById(friendId);

        Friend newFriend = new Friend();
        newFriend.setUser(user);
        newFriend.setFriend(friend);
        newFriend.setRelation(relation);
        newFriend.setActive(providerFriend.isPresent());

        friendRepository.save(newFriend);

        providerFriend.ifPresent(friendRequester -> friendRequester.setActive(true));
        providerFriend.ifPresent(friendRequester -> friendRepository.save(friendRequester));

        return "Friend successfully add!.";
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

    private FriendRequest mapRequestToDTO(Friend user){
        user.getUser().setPassword(null);
        user.getUser().setRoles(null);
        user.getUser().setFriends(null);

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUser(user.getUser());
        friendRequest.setRelation(user.getRelation());
        friendRequest.setActive(user.isActive());

        return friendRequest;
    }

}
