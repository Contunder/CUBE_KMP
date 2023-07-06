package com.example.cube.Service.impl;

import com.example.cube.Model.*;
import com.example.cube.Payload.CommentDto;
import com.example.cube.Payload.FriendDto;
import com.example.cube.Payload.FriendRequest;
import com.example.cube.Repository.FriendRepository;
import com.example.cube.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendServiceImpl friendService;

    private User user;


    @BeforeEach
    void setUp() {
        Friend friend1 = new Friend();
        friend1.setFriend(user);
        friend1.setActive(true);
        friend1.setUser(user);
        friend1.setRelation("Famille");
        Friend friend2 = new Friend();
        friend2.setFriend(user);
        friend1.setActive(true);
        friend1.setUser(user);
        friend2.setRelation("Famille");
        List<Friend> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(friend2);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role());
        Set<Friend> friendsHash = new HashSet<>();
        friendsHash.add(new Friend());
        friendsHash.add(new Friend());

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("");
        user.setRoles(roles);
        user.setFriends(friendsHash);


    }

    @Test
    void testGetFriendsByUserEmail() {
        // Arrange
        String email = "test@example.com";
        Friend friend1 = new Friend();
        friend1.setFriend(user);
        friend1.setActive(true);
        friend1.setUser(user);
        friend1.setRelation("Famille");
        Friend friend2 = new Friend();
        friend2.setFriend(user);
        friend1.setActive(true);
        friend1.setUser(user);
        friend2.setRelation("Famille");
        List<Friend> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(friend2);

        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(friendRepository.getFriendsByUser(user)).thenReturn(friends);

        // Act
        List<FriendDto> result = friendService.getFriendsByUserEmail(email);

        // Assert
        assertEquals(2, result.size());
        assertSame(friend1.getFriend(), result.get(0).getFriend());
        assertSame(friend2.getFriend(), result.get(1).getFriend());
    }

    @Test
    void testGetActiveFriendsByUserEmail() {
        // Arrange
        String email = "test@example.com";
        Friend friend1 = new Friend();
        Friend friend2 = new Friend();
        friend1.setFriend(user);
        friend1.setActive(true);
        friend2.setFriend(user);
        friend2.setActive(false);
        List<Friend> friends = Arrays.asList(friend1, friend2);

        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(friendRepository.getFriendsByUser(user)).thenReturn(friends);

        // Act
        List<FriendDto> result = friendService.getActiveFriendsByUserEmail(email);

        // Assert
        assertEquals(1, result.size());
        assertSame(friend1.getFriend(), result.get(0).getFriend());
    }

    @Test
    void testGetActiveFriendsByFriendId() {
        // Arrange
        String email = "test@example.com";
        long friendId = 1L;
        Friend friend1 = new Friend();
        Friend friend2 = new Friend();
        friend1.setFriend(user);
        friend1.setActive(true);
        friend2.setFriend(user);
        friend2.setActive(false);
        List<Friend> friends = Arrays.asList(friend1, friend2);

        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(friendRepository.getFriendsByUser(user)).thenReturn(friends);

        // Act
        List<FriendDto> result = friendService.getActiveFriendsByFriendId(email, friendId);

        // Assert
        assertEquals(1, result.size());
        assertSame(friend1.getFriend(), result.get(0).getFriend());
    }

    @Test
    void testGetRequestFriendsByUserEmail() {
        // Arrange
        String email = "test@example.com";
        Friend friend1 = new Friend();
        Friend friend2 = new Friend();
        friend1.setUser(user);
        friend1.setActive(true);
        friend2.setUser(user);
        friend2.setActive(false);
        List<Friend> friendRequests = Arrays.asList(friend1, friend2);

        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(friendRepository.getFriendsRequestByFriend(user)).thenReturn(friendRequests);

        // Act
        List<FriendRequest> result = friendService.getRequestFriendsByUserEmail(email);

        // Assert
        assertEquals(1, result.size());
        assertSame(friend2.getUser(), result.get(0).getUser());
    }

    @Test
    void testGetFriendsByRelation() {
        // Arrange
        String email = "test@example.com";
        String relation = "Family";
        Friend friend1 = new Friend();
        Friend friend2 = new Friend();
        friend1.setRelation("Family");
        friend1.setUser(user);
        friend1.setFriend(user);
        friend2.setRelation("Friend");
        friend2.setUser(user);
        friend2.setFriend(user);
        List<Friend> friends = Arrays.asList(friend1, friend2);

        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(friendRepository.getFriendsByUser(user)).thenReturn(friends);

        // Act
        List<FriendDto> result = friendService.getFriendsByRelation(email, relation);

        // Assert
        assertEquals(1, result.size());
        assertSame(friend1.getFriend(), result.get(0).getFriend());
    }

    @Test
    void testSetFriendsById() {
        // Arrange
        String userEmail = "test@example.com";
        long friendId = 1L;
        String relation = "Family";
        User user = new User();
        User friend = new User();

        when(userRepository.findUserByEmail(userEmail)).thenReturn(user);
        when(userRepository.findUserById(friendId)).thenReturn(friend);

        // Act
        String result = friendService.setFriendsById(userEmail, friendId, relation);

        // Assert
        assertEquals("Friend registered successfully!.", result);
        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    void testSetActiveFriendsById() {
        // Arrange
        String userEmail = "test@example.com";
        long friendId = 1L;

        String relation = "Family";
        Friend friendRequester = new Friend();
        friendRequester.setUser(user);
        friendRequester.setFriend(user);
        friendRequester.setActive(false);
        List<Friend> friendRequests = Arrays.asList(friendRequester);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(user);
        when(friendRepository.getFriendsRequestByFriend(user)).thenReturn(friendRequests);
        when(userRepository.findUserById(friendId)).thenReturn(user);
        when(friendRepository.save(any(Friend.class))).thenReturn(friendRequester);

        // Act
        String result = friendService.setActiveFriendsById(userEmail, friendId, relation);

        // Assert
        assertEquals("Friend successfully add!.", result);
        verify(friendRepository,times(2)).save(any(Friend.class));
        verify(friendRepository,times(1)).save(friendRequester);
        assertTrue(friendRequester.isActive());
    }
}