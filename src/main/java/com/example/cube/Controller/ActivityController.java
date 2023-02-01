package com.example.cube.Controller;

import com.example.cube.Payload.ActivityDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private ActivityService activityService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public ActivityController(ActivityService activityService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider){
        this.activityService = activityService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = {"/user/"})
    public ResponseEntity<List<ActivityDto>> getActivityByUser(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(activityService.getActivityByUser(email));
    }

    @GetMapping(value = {"/resource/{id}"})
    public ResponseEntity<List<ActivityDto>> getActivityByResource(@PathVariable(value = "id") long resourceId, HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(activityService.getActivityByResource(email, resourceId));
    }

    @GetMapping(value = {"/catalogue/{id}"})
    public ResponseEntity<List<ActivityDto>> getActivityByCatalogue(@PathVariable(value = "id") long catalogueId, HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok(activityService.getActivityByCatalogue(email, catalogueId));
    }
}