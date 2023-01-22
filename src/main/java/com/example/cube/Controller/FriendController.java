package com.example.cube.Controller;

import com.example.cube.Payload.FriendDto;
import com.example.cube.Payload.FriendRequest;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private FriendService friendService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public FriendController(FriendService friendService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider){
        this.friendService = friendService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = {"/user/"})
    public ResponseEntity<List<FriendDto>> getFriendsById(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(friendService.getFriendsByUserEmail(email));
    }

    @GetMapping(value = {"/request/"})
    public ResponseEntity<List<FriendRequest>> getFriendsRequestById(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(friendService.getRequestFriendsByUserEmail(email));
    }

    @GetMapping(value = {"/user/active"})
    public ResponseEntity<List<FriendDto>> getFriendsActiveById(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(friendService.getActiveFriendsByUserEmail(email));
    }

    @GetMapping(value = {"/relation/{relation}"})
    public ResponseEntity<List<FriendDto>> getFriendById(HttpServletRequest request, @PathVariable("relation") String relation) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(friendService.getFriendsByRelation(email, relation));
    }

    @PostMapping(value = {"/add/email/{email}/{relation}"})
    public ResponseEntity<String> setFriendsByEmail(HttpServletRequest request, @PathVariable("email") String friendEmail, @PathVariable("relation") String relation) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(friendService.setFriendsByEmail(email, friendEmail, relation));
    }

}
