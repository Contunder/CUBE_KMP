package com.example.cube.Service;

import com.example.cube.Payload.LoginDto;
import com.example.cube.Payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
