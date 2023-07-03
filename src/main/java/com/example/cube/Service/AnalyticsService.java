package com.example.cube.Service;

import com.example.cube.Model.Analytics;

import java.sql.Date;
import java.util.Optional;

public interface AnalyticsService {
    Optional<Analytics> getAnalyticsByDate(Date date);
}
