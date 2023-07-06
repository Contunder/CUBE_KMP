package com.example.cube.Controller;

import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.ResourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ResourceControllerTest {

    @Mock
    private ResourceService resourceService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ResourceController resourceController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        resourceController = new ResourceController(resourceService, jwtAuthenticationFilter, jwtTokenProvider);
    }

    @Test
    void testCreateResource() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "token";
        String email = "test@example.com";
        long catalogueId = 1l;
        ResourceDto resourceDto = new ResourceDto();
        ResourceDto createdResourceDto = new ResourceDto();
        Mockito.when(jwtAuthenticationFilter.getTokenFromRequest(request)).thenReturn(token);
        Mockito.when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        Mockito.when(resourceService.createResource(resourceDto, catalogueId, email)).thenReturn(createdResourceDto);

        // Act
        ResponseEntity<ResourceDto> response = resourceController.createResource(request, resourceDto, catalogueId);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(createdResourceDto, response.getBody());
        Mockito.verify(jwtAuthenticationFilter).getTokenFromRequest(request);
        Mockito.verify(jwtTokenProvider).getUsername(token);
        Mockito.verify(resourceService).createResource(resourceDto, catalogueId, email);
    }

    @Test
    void testGetAllResources() {
        // Arrange
        int pageNo = 1;
        int pageSize = 10;
        String sortBy = "name";
        String sortDir = "asc";
        ResourceResponse resourceResponse = new ResourceResponse();
        Mockito.when(resourceService.getAllResources(pageNo, pageSize, sortBy, sortDir)).thenReturn(resourceResponse);

        // Act
        ResourceResponse response = resourceController.getAllResources(pageNo, pageSize, sortBy, sortDir);

        // Assert
        Assertions.assertEquals(resourceResponse, response);
        Mockito.verify(resourceService).getAllResources(pageNo, pageSize, sortBy, sortDir);
    }
}
