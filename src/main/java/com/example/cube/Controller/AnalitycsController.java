package com.example.cube.Controller;

import com.example.cube.Model.Analytics;
import com.example.cube.Service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping("/api/analytics")
public class AnalitycsController {

    private AnalyticsService analyticsService;

    public AnalitycsController(AnalyticsService analyticsService){
        this.analyticsService = analyticsService;
    }

    @GetMapping(value = {"/today"})
    public ResponseEntity<Analytics> getTodayAnalitycs() {

        return ResponseEntity.ok(analyticsService.getAnalyticsByDate(getSQLDate()));
    }

    @GetMapping(value = {"/{date}"})
    public ResponseEntity<Analytics> getAnalticsAtDate(@PathVariable(value = "date") Date date) {

        return ResponseEntity.ok(analyticsService.getAnalyticsByDate(date));
    }

    private Date getSQLDate(){
        java.util.Date todayJava = new java.util.Date();
        return new Date(todayJava.getDate());
    }
}
