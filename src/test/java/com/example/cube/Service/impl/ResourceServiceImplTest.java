package com.example.cube.Service.impl;

import com.example.cube.Model.Activity;
import com.example.cube.Model.Analytics;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Repository.ActivityRepository;
import com.example.cube.Repository.AnalyticsRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private ResourceDto ressourceDto;

    private Resource resource;

    private Analytics analytics;

    @Before
    public void setUp() {
        ressourceDto = new ResourceDto();
        ressourceDto.setId(1L);

        resource = new Resource();
        resource.setId(ressourceDto.getId());

        analytics = new Analytics();
        analytics.setDate(getSQLDate());
    }

    @Test
    public void SetView() {
        // given
        Long ressourceId = 1L;
        String email = "test@test.fr";

        when(resourceRepository.findById(ressourceId))
                .thenReturn(Optional.ofNullable(resource));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByResource(Optional.ofNullable(resource)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate()))
                .thenReturn(Optional.of(new Analytics()));

        // when
        String view = resourceService.setView(email, 1, true);

        // then
        Assertions.assertAll(
                () -> assertThat(view).isEqualTo("resource view : true")
        );
    }

    @Test
    public void SetLike() {
        // given
        Long ressourceId = 1L;
        String email = "test@test.fr";

        when(resourceRepository.findById(ressourceId))
                .thenReturn(Optional.ofNullable(resource));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByResource(Optional.ofNullable(resource)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate())).thenReturn(Optional.of(analytics));

        // when
        String view = resourceService.setLike(email, 1, true);

        // then
        Assertions.assertAll(
                () -> assertThat(view).isEqualTo("resource liked : true")
        );
    }

    @Test
    public void SetShare() {
        // given
        Long ressourceId = 1L;
        String email = "test@test.fr";

        when(resourceRepository.findById(ressourceId))
                .thenReturn(Optional.ofNullable(resource));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByResource(Optional.ofNullable(resource)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate())).thenReturn(Optional.of(analytics));

        // when
        String view = resourceService.setShare(email, 1, true);

        // then
        Assertions.assertAll(
                () -> assertThat(view).isEqualTo("resource share : true")
        );
    }

    @Test
    public void SetBlocked() {
        // given
        Long ressourceId = 1L;
        String email = "test@test.fr";

        when(resourceRepository.findById(ressourceId))
                .thenReturn(Optional.ofNullable(resource));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByResource(Optional.ofNullable(resource)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate())).thenReturn(Optional.of(analytics));

        // when
        String view = resourceService.setBlocked(email, 1, true);

        // then
        Assertions.assertAll(
                () -> assertThat(view).isEqualTo("resource blocked : true")
        );
    }

    private static Date getSQLDate(){
        java.util.Date todayJava = new java.util.Date();
        return new Date(todayJava.getDate());
    }
}