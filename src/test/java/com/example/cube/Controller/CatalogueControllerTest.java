package com.example.cube.Controller;

import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.CatalogueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class CatalogueControllerTest {

    @Mock
    private CatalogueService catalogueService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private CatalogueController catalogueController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        catalogueController = new CatalogueController(catalogueService, jwtAuthenticationFilter, jwtTokenProvider);
    }

    @Test
    void testAddCatalogue() {
        // Arrange
        CatalogueDto catalogueDto = new CatalogueDto();
        CatalogueDto savedCatalogueDto = new CatalogueDto();
        Mockito.when(catalogueService.addCatalogue(catalogueDto)).thenReturn(savedCatalogueDto);

        // Act
        ResponseEntity<CatalogueDto> response = catalogueController.addCatalogue(catalogueDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedCatalogueDto, response.getBody());
        verify(catalogueService).addCatalogue(catalogueDto);
    }

    @Test
    void testGetCatalogue() {
        // Arrange
        long catalogueId = 1L;
        CatalogueDto catalogueDto = new CatalogueDto();
        Mockito.when(catalogueService.getCatalogue(catalogueId)).thenReturn(catalogueDto);

        // Act
        ResponseEntity<CatalogueDto> response = catalogueController.getCatalogue(catalogueId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(catalogueDto, response.getBody());
        verify(catalogueService).getCatalogue(catalogueId);
    }

}
