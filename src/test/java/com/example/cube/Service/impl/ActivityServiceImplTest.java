package com.example.cube.Service.impl;

import com.example.cube.Model.Activity;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Repository.ActivityRepository;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private CatalogueRepository catalogueRepository;
    @InjectMocks
    private ActivityServiceImpl activityService;

    private User user;
    private List<Activity> activities;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");

        activities = new ArrayList<>();
        Activity activity1 = new Activity();
        activity1.setId(1);
        activity1.setUser(user);
        activity1.setResource(Resource.builder().id(1L).build());
        activity1.setCatalogue(new Catalogue());
        activity1.setView(true);
        Activity activity2 = new Activity();
        activity2.setId(2);
        activity2.setUser(user);
        activity2.setResource(Resource.builder().id(1L).build());
        activity2.setCreated(true);
        activities.add(activity1);
        activities.add(activity2);
    }

    @Test
    void setResourceActivity_existingActivity_shouldUpdateActivity() {
        // Arrange
        ActivityDto activityDto = new ActivityDto();
        activityDto.setResource(Optional.ofNullable(Resource.builder().id(1L).value("Resource 1").build()));

        Activity existingActivity = new Activity();
        existingActivity.setResource(Resource.builder().id(1L).value("Resource 1").build());
        existingActivity.setCatalogue(Catalogue.builder().category("My categorie").build());

        when(activityRepository.getActivityByResource(any())).thenReturn(Optional.of(existingActivity));

        // Act
        activityService.setResourceActivity(activityDto);

        // Assert
        verify(activityRepository).save(any(Activity.class));
        verify(activityRepository).save(existingActivity);
    }

    @Test
    void setResourceActivity_newActivity_shouldSaveNewActivity() {
        // Arrange
        ActivityDto activityDto = new ActivityDto();
        activityDto.setResource(Optional.ofNullable(Resource.builder().id(1L).value("Resource 1").build()));
        activityDto.setCatalogue(Optional.ofNullable(Catalogue.builder().category("My categorie").build()));

        when(activityRepository.getActivityByResource(any())).thenReturn(Optional.empty());

        // Act
        activityService.setResourceActivity(activityDto);

        // Assert
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void setCatalogueActivity_existingActivity_shouldUpdateActivity() {
        // Arrange
        ActivityDto activityDto = new ActivityDto();
        activityDto.setResource(Optional.ofNullable(Resource.builder().id(1L).value("Resource 1").build()));
        activityDto.setCatalogue(Optional.ofNullable(Catalogue.builder().category("My categorie").build()));

        Activity existingActivity = new Activity();
        existingActivity.setCatalogue(Catalogue.builder().category("My categorie").build());
        existingActivity.setId(1L);

        when(activityRepository.getActivityByCatalogue(any())).thenReturn(Optional.of(existingActivity));

        // Act
        activityService.setCatalogueActivity(activityDto);

        // Assert
        verify(activityRepository).save(existingActivity);
    }

    @Test
    void setCatalogueActivity_newActivity_shouldSaveNewActivity() {
        // Arrange
        ActivityDto activityDto = new ActivityDto();
        activityDto.setResource(Optional.ofNullable(Resource.builder().id(1L).value("Resource 1").build()));
        activityDto.setCatalogue(Optional.ofNullable(Catalogue.builder().category("My categorie").build()));

        when(activityRepository.getActivityByCatalogue(any())).thenReturn(Optional.empty());

        // Act
        activityService.setCatalogueActivity(activityDto);

        // Assert
        verify(activityRepository, times(1)).save(any(Activity.class));
    }

    @Test
    void getActivityByUser_shouldReturnActivities() {
        // Arrange
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        when(activityRepository.getActivitiesByUser(user)).thenReturn(activities);


        // Act
        List<ActivityDto> result = activityService.getActivityByUser("johndoe@example.com");

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    void getActivityByResource_shouldReturnActivitiesForUserAndResource() {
        // Arrange
        String email = "test@example.com";
        long resourceId = 1L;

        Resource resource = new Resource();
        resource.setId(resourceId);

        when(userRepository.findUserByEmail(any())).thenReturn(user);
        when(resourceRepository.getResourceById(any())).thenReturn(Optional.of(resource));
        when(activityRepository.getActivitiesByResource(any())).thenReturn(activities);

        // Act
        List<ActivityDto> result = activityService.getActivityByResource(email, resourceId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    void getActivityByResourceCreate_shouldReturnCreatedActivitiesForResource() {
        // Arrange
        long resourceId = 123;
        Resource resource = new Resource();
        resource.setId(resourceId);


        when(resourceRepository.getResourceById(any())).thenReturn(Optional.of(resource));

        when(activityRepository.getActivitiesByResource(any())).thenReturn(activities);

        // Act
        List<ActivityDto> result = activityService.getActivityByResourceCreate(resourceId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @Test
    void getActivityByCatalogue_shouldReturnActivitiesForUserAndCatalogue() {
        // Arrange
        String email = "test@example.com";
        long catalogueId = 123;

        Catalogue catalogue = new Catalogue();
        catalogue.setId(catalogueId);

        when(userRepository.findUserByEmail(any())).thenReturn(user);

        when(catalogueRepository.getCatalogueById(catalogueId)).thenReturn(catalogue);

        when(activityRepository.getActivitiesByCatalogue(any())).thenReturn(activities);

        // Act
        List<ActivityDto> result = activityService.getActivityByCatalogue(email, catalogueId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }
}