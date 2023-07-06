package com.example.cube.Controller;

import com.example.cube.Payload.FriendDto;
import com.example.cube.Payload.FriendRequest;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FriendControllerTest {

    @Mock
    private FriendService friendService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private FriendController friendController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        friendController = new FriendController(friendService, jwtAuthenticationFilter, jwtTokenProvider);
    }

    @Test
    void testGetFriendsById() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "token";
        String email = "test@example.com";
        List<FriendDto> friendDtos = Collections.singletonList(new FriendDto());
        when(jwtAuthenticationFilter.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        when(friendService.getFriendsByUserEmail(email)).thenReturn(friendDtos);

        // Act
        ResponseEntity<List<FriendDto>> response = friendController.getFriendsById(request);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(friendDtos, response.getBody());
        verify(jwtAuthenticationFilter).getTokenFromRequest(request);
        verify(jwtTokenProvider).getUsername(token);
        verify(friendService).getFriendsByUserEmail(email);
    }

    @Test
    void testGetFriendsRequestById() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "token";
        String email = "test@example.com";
        List<FriendRequest> friendRequests = Collections.singletonList(new FriendRequest());
        when(jwtAuthenticationFilter.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        when(friendService.getRequestFriendsByUserEmail(email)).thenReturn(friendRequests);

        // Act
        ResponseEntity<List<FriendRequest>> response = friendController.getFriendsRequestById(request);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(friendRequests, response.getBody());
        verify(jwtAuthenticationFilter).getTokenFromRequest(request);
        verify(jwtTokenProvider).getUsername(token);
        verify(friendService).getRequestFriendsByUserEmail(email);
    }
}
