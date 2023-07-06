package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.*;
import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Repository.*;
import com.example.cube.Service.ActivityService;
import com.example.cube.Service.ResourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityService activityService;

    @Mock
    private CatalogueRepository catalogueRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private ResourceDto ressourceDto;
    private Resource resource;
    private Catalogue catalogue;
    private User user;
    private User friend;
    private Analytics analytics;

    @Before
    public void setUp() {
        ressourceDto = new ResourceDto();
        ressourceDto.setId(1L);

        resource = new Resource();
        resource.setId(ressourceDto.getId());

        catalogue = new Catalogue();
        catalogue.setId(1L);
        catalogue.setCategory("Electronics");

        user = new User();
        user.setId(1L);

        friend = new User();
        friend.setId(1L);

        analytics = new Analytics();
        analytics.setDate(getSQLDate());
    }

    @Test
    public void createResource() {
        // given
        Long catalogueId = 1L;
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);
        when(catalogueRepository.findById(catalogueId)).thenReturn(Optional.ofNullable(catalogue));
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        // when
        ResourceDto result = resourceService.createResource(ressourceDto, catalogueId,"test@yopmail.com");

        // then
        Assertions.assertAll(
                () -> assertEquals(resource.getId(), result.getId())
        );
    }

    @Test
    public void getAllResources() {
        // given
        List<Resource> resourceList = createMockResourceList();
        Page<Resource> resourcePage = new PageImpl<>(resourceList);
        when(resourceRepository.findAll(any(Pageable.class))).thenReturn(resourcePage);

        // when
        ResourceResponse result = resourceService.getAllResources(0, 10, "name", "ASC");

        // then
        Assertions.assertAll(
                () -> assertEquals(0, result.getPageNo()),
                () -> assertEquals(2, result.getPageSize()),
                () -> assertEquals(resourceList.size(), result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(true, result.isLast()),
                () -> assertEquals(resourceList.size(), result.getContent().size())
        );
    }

    private List<Resource> createMockResourceList() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(Resource.builder().id(1L).value("Resource 1").build());
        resourceList.add(Resource.builder().id(2L).value("Resource 2").build());
        return resourceList;
    }

    @Test
    public void testGetResourceById_ResourceExists() {
        // given
        Resource mockResource = Resource.builder().id(1L).value("Resource 1").build();
        when(resourceRepository.findById(any())).thenReturn(Optional.of(mockResource));



        // when
        ResourceDto result = resourceService.getResourceById(1L);

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(mockResource.getId(), result.getId()),
                () -> assertEquals(mockResource.getValue(), result.getValue())
        );
    }

    @Test
    public void testGetResourceById_ResourceNotFound() {
        // given
        when(resourceRepository.findById(any())).thenReturn(Optional.empty());


        // then
        assertThrows(ResourceNotFoundException.class, () -> resourceService.getResourceById(1L));
    }

    @Test
    public void testGetResourceByUserId() {
        // given
        when(userRepository.findUserById(1L)).thenReturn(user);

        List<Resource> mockResources = createMockResources();
        when(resourceRepository.getResourceByIdOrderById(any())).thenReturn(mockResources.get(0));

        List<Activity> mockActivities = createMockActivities();
        when(activityRepository.getActivitiesByUser(any(User.class))).thenReturn(mockActivities);

        // when
        List<ResourceDto> result = resourceService.getResourceByUserId(1L);

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(mockResources.size(), result.size())
        );
    }

    private List<Activity> createMockActivities() {
        List<Activity> activities = new ArrayList<>();
        activities.add(Activity.builder().id(1L).resource(Resource.builder().id(1L).value("Resource 1").build()).created(true).user(friend).build());
        activities.add(Activity.builder().id(2L).resource(Resource.builder().id(2L).value("Resource 2").build()).created(true).user(friend).build());
        return activities;
    }


    private List<Resource> createMockResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.builder().id(1L).value("Resource 1").build());
        resources.add(Resource.builder().id(2L).value("Resource 2").build());
        return resources;
    }

    @Test
    public void testGetResourceByRelation() {
        // Mock the user repository to return a user based on email
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        List<Friend> mockFriends = createMockFriends();
        when(friendRepository.getFriendsByUser(any(User.class))).thenReturn(mockFriends);

        List<Activity> mockActivities = createMockActivities();
        when(activityRepository.findAll()).thenReturn(mockActivities);


        // Call the method being tested
        List<ResourceDto> result = resourceService.getResourceByRelation("user1@example.com", "relation");

        // Assertions
        assertNotNull(result);
    }

    private List<Friend> createMockFriends() {
        List<Friend> friends = new ArrayList<>();
        friends.add(Friend.builder().user(user).friend(friend).relation("Famille").isActive(true).build());
        friends.add(Friend.builder().user(friend).friend(user).relation("Famille").isActive(true).build());
        return friends;
    }

    @Test
    public void testUpdateResource() {
        // given
        Resource mockResource = Resource.builder().id(1L).value("Resource 1").build();
        when(resourceRepository.findById(any())).thenReturn(Optional.of(mockResource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(mockResource);

        Catalogue mockCatalogue = new Catalogue(1L, "Catalogue 1");
        when(catalogueRepository.findById(any())).thenReturn(Optional.of(mockCatalogue));

        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setAccess("updated access");
        resourceDto.setValue("updated value");

        // when
        ResourceDto result = resourceService.updateResource(resourceDto, 1L, 1L);

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(mockResource.getId(), result.getId()),
                () -> assertEquals(resourceDto.getAccess(), result.getAccess()),
                () -> assertEquals(resourceDto.getValue(), result.getValue())
        );
    }

    @Test
    public void testDeleteResourceById_ResourceExists() {
        // Mock the resource repository to return a resource
        Resource mockResource = Resource.builder().id(1L).value("Resource 1").build();
        when(resourceRepository.findById(any())).thenReturn(Optional.of(mockResource));


        // Call the method being tested
        resourceService.deleteResourceById(1L);

        // Verify that the delete method was called with the correct resource
        Mockito.verify(resourceRepository, Mockito.times(1)).delete(mockResource);
    }

    @Test
    public void testDeleteResourceById_ResourceNotFound() {
        // Mock the resource repository to return an empty optional
        when(resourceRepository.findById(any())).thenReturn(Optional.empty());


        // Call the method being tested and assert that it throws an exception
        assertThrows(ResourceNotFoundException.class, () -> resourceService.deleteResourceById(1L));

        // Verify that the delete method was not called
        Mockito.verify(resourceRepository, Mockito.never()).delete(any(Resource.class));
    }

    @Test
    public void testGetResourcesByCategory() {
        // Mock the resource repository to return a set of resources
        Set<Resource> mockResources = createSetMockResources();
        when(resourceRepository.findAllByCatalogueIdIn(any())).thenReturn(mockResources);

        // Call the method being tested
        List<ResourceDto> result = resourceService.getResourcesByCategory(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(mockResources.size(), result.size());
        // Add additional assertions as needed
    }

    private Set<Resource> createSetMockResources() {
        Set<Resource> resources = new HashSet<>();
        resources.add(Resource.builder().id(1L).value("Resource 1").build());
        resources.add(Resource.builder().id(2L).value("Resource 2").build());
        // Add more mock resources if needed
        return resources;
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