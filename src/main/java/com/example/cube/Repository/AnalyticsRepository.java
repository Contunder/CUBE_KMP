package com.example.cube.Repository;

import com.example.cube.Model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    Optional<Analytics> getAnalyticsByDate(Date date);

}
