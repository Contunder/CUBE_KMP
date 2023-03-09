package com.example.cube.Service;

import com.example.cube.Model.Analytics;

import java.sql.Date;

public interface AnalyticsService {
    Analytics getAnalyticsByDate(Date date);
}
