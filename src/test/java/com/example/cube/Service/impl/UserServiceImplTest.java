package com.example.cube.Service.impl;

import com.example.cube.Model.User;
import com.example.cube.Payload.UserDto;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl(userRepository);

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
    }

    @Test
    void getUserByEmail() {
        when(userRepository.findUserByEmail("johndoe@example.com")).thenReturn(user);
        UserDto userDto = userService.getUserByEmail("johndoe@example.com");

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void getUserById() {
        when(userRepository.findUserById(1L)).thenReturn(user);
        UserDto userDto = userService.getUserById(1L);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

//    @Test
//    void getAllUser() {
//        List<User> users = new ArrayList<>();
//        users.add(user);
//
//        when(userRepository.findAll()).thenReturn(users);
//        when(userRepository.findUserByEmail("johndoe@example.com")).thenReturn(user);
//
//        List<UserDto> userDtos = userService.getAllUser("johndoe@example.com");
//        assertEquals(0, userDtos.size());
//    }

}