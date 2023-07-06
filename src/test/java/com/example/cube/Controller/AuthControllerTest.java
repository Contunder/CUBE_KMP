package com.example.cube.Controller;

import com.example.cube.Payload.JWTAuthResponse;
import com.example.cube.Payload.LoginDto;
import com.example.cube.Payload.RegisterDto;
import com.example.cube.Service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService);
    }

    @Test
    void testAuthenticateUser() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        String token = "dummyToken";
        Mockito.when(authService.login(loginDto)).thenReturn(token);

        // Act
        ResponseEntity<JWTAuthResponse> response = authController.authenticateUser(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().getAccessToken());
        verify(authService).login(loginDto);
    }

    @Test
    void testRegister() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        String responseMessage = "User registered successfully!.";
        Mockito.when(authService.register(registerDto)).thenReturn(responseMessage);

        // Act
        ResponseEntity<String> response = authController.register(registerDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
        verify(authService).register(registerDto);
    }

}
