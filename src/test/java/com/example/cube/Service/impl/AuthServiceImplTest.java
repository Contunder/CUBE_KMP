package com.example.cube.Service.impl;

import com.example.cube.Model.Role;
import com.example.cube.Model.User;
import com.example.cube.Payload.LoginDto;
import com.example.cube.Payload.RegisterDto;
import com.example.cube.Repository.RoleRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testLogin() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        String expectedToken = "test_token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(expectedToken);

        // Act
        String token = authService.login(loginDto);

        // Assert
        assertEquals(expectedToken, token);
    }

    @Test
    void testRegister() {
        // Arrange
        RegisterDto signUpDto = new RegisterDto();
        signUpDto.setEmail("john.doe@example.com");
        signUpDto.setName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setPassword("password");
        signUpDto.setBirthday(new Date(2023,7,6));
        signUpDto.setZipCode("12345");
        signUpDto.setCity("City");
        signUpDto.setAddress("Adress");
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");
        User savedUser = new User();
        savedUser.setId(1L);

        MockitoAnnotations.openMocks(this);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        // Act
        String result = authService.register(signUpDto);

        // Assert
        assertEquals("User registered successfully!.", result);
    }

}