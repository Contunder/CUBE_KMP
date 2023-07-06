package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Activity;
import com.example.cube.Model.Analytics;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Friend;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Repository.ActivityRepository;
import com.example.cube.Repository.AnalyticsRepository;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.FriendRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.ActivityService;
import com.example.cube.Service.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class ResourceServiceImpl implements ResourceService {

    private ResourceRepository resourceRepository;

    private CatalogueRepository catalogueRepository;
    private ActivityService activityService;
    private UserRepository userRepository;
    private FriendRepository friendRepository;
    private AnalyticsRepository analyticsRepository;
    private ActivityRepository activityRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               CatalogueRepository catalogueRepository,
                               ActivityService activityService,
                               UserRepository userRepository,
                               FriendRepository friendRepository,
                               AnalyticsRepository analyticsRepository,
                               ActivityRepository activityRepository) {
          this.resourceRepository = resourceRepository;
          this.catalogueRepository = catalogueRepository;
          this.activityService = activityService;
          this.userRepository = userRepository;
          this.friendRepository = friendRepository;
          this.analyticsRepository = analyticsRepository;
          this.activityRepository = activityRepository;
    }

    @Override
    public ResourceDto createResource(ResourceDto resourceDto, long catalogueId, String email) {

        Resource resource = mapToEntity(resourceDto);

        Set<Catalogue> catalogue = new HashSet<>();
        Optional<Catalogue> resourceCatalogue = catalogueRepository.findById(catalogueId);
        resourceCatalogue.ifPresent(catalogue::add);
        resource.setCatalogue(catalogue);
        Resource newResource = resourceRepository.save(resource);

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setCreated(analytics.getCreated() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setCreated(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        User user = userRepository.findUserByEmail(email);

        ActivityDto activityDto = new ActivityDto();
        activityDto.setResource(Optional.of(newResource));
        activityDto.setCatalogue(Optional.empty());
        activityDto.setUser(user);
        activityDto.setCreated(true);
        activityService.setResourceActivity(activityDto);

        return mapToDTO(newResource);
    }

    @Override
    public ResourceResponse getAllResources(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Resource> resource = resourceRepository.findAll(pageable);

        List<Resource> listOfResources = resource.getContent();

        List<ResourceDto> content= listOfResources.stream().map(this::mapToDTO).collect(Collectors.toList());

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setContent(content);
        resourceResponse.setPageNo(resource.getNumber());
        resourceResponse.setPageSize(resource.getSize());
        resourceResponse.setTotalElements(resource.getTotalElements());
        resourceResponse.setTotalPages(resource.getTotalPages());
        resourceResponse.setLast(resource.isLast());

        return resourceResponse;
    }

    @Override
    public ResourceDto getResourceById(long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id));
        return mapToDTO(resource);
    }

    @Override
    public List<ResourceDto> getResourceByUserId(long id) {
        User user = userRepository.findUserById(id);
        List<Resource> resources = new ArrayList<>();
        List<Activity> activitys = activityRepository.getActivitiesByUser(user);

        activitys.stream()
                .filter(Activity::isCreated)
                .filter(activity -> nonNull(activity.getResource()))
                .forEach(activity -> resources.add(resourceRepository.getResourceByIdOrderById(activity.getResource().getId())));


        return resources.stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<ResourceDto> getResourceByRelation(String email, String relation) {
        List<Resource> resources = new ArrayList<>();
        User user = userRepository.findUserByEmail(email);
        List<Friend> friends = friendRepository.getFriendsByUser(user);
        List<Activity> activitys = activityRepository.findAll();

        List<Activity> finalActivitys = activitys;
        activitys = friends.stream()
                .filter(friend -> friend.getRelation().equals(relation))
                .filter(friend -> friend.getUser().equals(user))
                .map(friend -> friend.getFriend().getId())
                .flatMap(id -> finalActivitys.stream().filter(activity -> Long.valueOf(activity.getUser().getId()).equals(id)))
                .filter(Activity::isCreated)
                .toList();

        activitys.forEach(activity -> resources.add(resourceRepository.getResourceByIdOrderById(activity.getResource().getId())));

        return resources.stream().map(this::mapToDTO).toList();
    }

    @Override
    public ResourceDto updateResource(ResourceDto resourceDto, long id, long catalogueId) {

        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id));

        resource.setAccess(resourceDto.getAccess());
        resource.setValue(resourceDto.getValue());

        Set<Catalogue> catalogue = new HashSet<>();
        Catalogue resourceCatalogue = catalogueRepository.findById(catalogueId).get();
        catalogue.add(resourceCatalogue);
        resource.setCatalogue(catalogue);

        Resource updatedResource = resourceRepository.save(resource);
        return mapToDTO(updatedResource);
    }

    @Override
    public void deleteResourceById(long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id));
        resourceRepository.delete(resource);
    }

    @Override
    public List<ResourceDto> getResourcesByCategory(Long catalogueId) {

        Set<Resource> resources = resourceRepository.findAllByCatalogueIdIn(Collections.singleton(catalogueId));

        return resources.stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String setView(String email, long id, boolean view){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByResource(resource);
        if (optionalActivity.isPresent()){

            optionalActivity.ifPresent(
                    activity -> activity.setView(view)
            );
            optionalActivity.ifPresent(
                    activity -> activityRepository.save(activity)
            );
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setView(view);
            activityService.setResourceActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setView(analytics.getView() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setView(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource view : " + view;
    }
    @Override
    public String setLike(String email, long id, boolean like){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByResource(resource);
        if (optionalActivity.isPresent()){
            optionalActivity.ifPresent(activity -> activity.setFavorite(like));
            optionalActivity.ifPresent(activity -> activityRepository.save(activity));
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setFavorite(like);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setFavorite(analytics.getFavorite() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setFavorite(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource liked : " + like;
    }

    @Override
    public String setShare(String email, long id, boolean share){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByResource(resource);
        if (optionalActivity.isPresent()){
            optionalActivity.ifPresent(activity -> activity.setShare(share));
            optionalActivity.ifPresent(activity -> activityRepository.save(activity));
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setShare(share);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setShare(analytics.getShare() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setShare(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource share : " + share;
    }

    @Override
    public String setBlocked(String email, long id, boolean blocked){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByResource(resource);
        if (optionalActivity.isPresent()){
            optionalActivity.ifPresent(activity -> activity.setBlocked(blocked));
            optionalActivity.ifPresent(activity -> activityRepository.save(activity));
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setBlocked(blocked);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setBlocked(analytics.getBlocked() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setBlocked(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource blocked : " + blocked;
    }

    private ResourceDto mapToDTO(Resource resource){
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId());
        resourceDto.setAccess(resource.getAccess());
        resourceDto.setValue(resource.getValue());
        resourceDto.setCatalogue(resource.getCatalogue());
        return resourceDto;
    }

    private Resource mapToEntity(ResourceDto resourceDto){
        Resource resource = new Resource();
        resource.setId(resourceDto.getId());
        resource.setAccess(resourceDto.getAccess());
        resource.setValue(resourceDto.getValue());
        resource.setCatalogue(resourceDto.getCatalogue());
        return resource;
    }

    private Date getSQLDate(){
        java.util.Date todayJava = new java.util.Date();
        return new Date(todayJava.getDate());
    }
}
