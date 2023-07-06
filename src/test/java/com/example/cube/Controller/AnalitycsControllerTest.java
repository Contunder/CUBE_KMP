package com.example.cube.Controller;

import com.example.cube.Model.Analytics;
import com.example.cube.Service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class AnalitycsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    private AnalitycsController analitycsController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        analitycsController = new AnalitycsController(analyticsService);
    }

    @Test
    void testGetTodayAnalitycs() {
        // Arrange
        Date today = Date.valueOf(LocalDate.now());
        Optional<Analytics> analytics = Optional.of(new Analytics());
        Mockito.when(analyticsService.getAnalyticsByDate(today)).thenReturn(analytics);

        // Act
        ResponseEntity<Optional<Analytics>> response = analitycsController.getTodayAnalitycs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAnalticsAtDate() {
        // Arrange
        Date date = Date.valueOf(LocalDate.of(2023, 7, 6));
        Optional<Analytics> analytics = Optional.of(new Analytics());
        Mockito.when(analyticsService.getAnalyticsByDate(date)).thenReturn(analytics);

        // Act
        ResponseEntity<Optional<Analytics>> response = analitycsController.getAnalticsAtDate(date);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(analytics, response.getBody());
        verify(analyticsService).getAnalyticsByDate(date);
    }

}
