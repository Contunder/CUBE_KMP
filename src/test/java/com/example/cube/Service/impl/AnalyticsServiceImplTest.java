package com.example.cube.Service.impl;

import com.example.cube.Model.Analytics;
import com.example.cube.Repository.AnalyticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void testGetAnalyticsByDate() {
        // Arrange
        Date date = new Date(2023,7,6);
        Analytics expectedAnalytics = new Analytics();

        when(analyticsRepository.getAnalyticsByDate(date)).thenReturn(Optional.of(expectedAnalytics));

        // Act
        Optional<Analytics> result = analyticsService.getAnalyticsByDate(date);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expectedAnalytics, result.get());
    }

}