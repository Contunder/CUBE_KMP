package com.example.cube.Repository;

import com.example.cube.Model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    Analytics getAnalyticsByDate(Date date);

}
