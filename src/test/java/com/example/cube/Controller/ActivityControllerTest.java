package com.example.cube.Controller;

import com.example.cube.Payload.ActivityDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class ActivityControllerTest {

    @Mock
    private ActivityService activityService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ActivityController activityController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        activityController = new ActivityController(activityService, jwtAuthenticationFilter, jwtTokenProvider);
    }

    @Test
    void testGetActivityByUser() {
        // Arrange
        String email = "test@example.com";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "test-token";
        Mockito.when(jwtAuthenticationFilter.getTokenFromRequest(request)).thenReturn(token);
        Mockito.when(jwtTokenProvider.getUsername(token)).thenReturn(email);

        List<ActivityDto> activityList = new ArrayList<>();
        activityList.add(new ActivityDto());
        Mockito.when(activityService.getActivityByUser(email)).thenReturn(activityList);

        // Act
        ResponseEntity<List<ActivityDto>> response = activityController.getActivityByUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activityList, response.getBody());
        verify(jwtAuthenticationFilter).getTokenFromRequest(request);
        verify(jwtTokenProvider).getUsername(token);
        verify(activityService).getActivityByUser(email);
    }

    @Test
    void testGetActivityByResource() {
        // Arrange
        String email = "test@example.com";
        long resourceId = 123;
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "test-token";
        Mockito.when(jwtAuthenticationFilter.getTokenFromRequest(request)).thenReturn(token);
        Mockito.when(jwtTokenProvider.getUsername(token)).thenReturn(email);

        List<ActivityDto> activityList = new ArrayList<>();
        activityList.add(new ActivityDto());
        Mockito.when(activityService.getActivityByResource(email, resourceId)).thenReturn(activityList);

        // Act
        ResponseEntity<List<ActivityDto>> response = activityController.getActivityByResource(resourceId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activityList, response.getBody());
        verify(jwtAuthenticationFilter).getTokenFromRequest(request);
        verify(jwtTokenProvider).getUsername(token);
        verify(activityService).getActivityByResource(email, resourceId);
    }

}
