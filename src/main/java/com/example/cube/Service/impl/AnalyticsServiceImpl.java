package com.example.cube.Service.impl;

import com.example.cube.Model.Analytics;
import com.example.cube.Repository.AnalyticsRepository;
import com.example.cube.Service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private AnalyticsRepository analyticsRepository;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository){
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public Optional<Analytics> getAnalyticsByDate(Date date){
        return analyticsRepository.getAnalyticsByDate(date);
    }
}
