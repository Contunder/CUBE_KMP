package com.example.cube.Service;

import com.example.cube.Payload.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserByEmail(String email);

    UserDto getUserById(long id);

    List<UserDto> getAllUser();
}
