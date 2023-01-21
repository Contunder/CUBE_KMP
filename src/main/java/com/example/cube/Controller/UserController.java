package com.example.cube.Controller;

import com.example.cube.Payload.UserDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = {"/email/{email}"})
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping(value = {"/id/{id}"})
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping(value = {"/actual"})
    public ResponseEntity<UserDto> getUser(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

}
