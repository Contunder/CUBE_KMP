package com.example.cube.Controller;

import com.example.cube.Payload.UserDto;
import com.example.cube.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, null, null);
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        String email = "test@example.com";
        UserDto userDto = new UserDto();
        Mockito.when(userService.getUserByEmail(email)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.getUserByEmail(email);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userDto, response.getBody());
        Mockito.verify(userService).getUserByEmail(email);
    }

    @Test
    void testGetUserById() {
        // Arrange
        long id = 1L;
        UserDto userDto = new UserDto();
        Mockito.when(userService.getUserById(id)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.getUserById(id);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userDto, response.getBody());
        Mockito.verify(userService).getUserById(id);
    }
}
