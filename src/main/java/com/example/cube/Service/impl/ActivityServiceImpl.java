package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Activity;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Repository.ActivityRepository;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {

    private ActivityRepository activityRepository;
    private UserRepository userRepository;
    private ResourceRepository resourceRepository;
    private CatalogueRepository catalogueRepository;


    public ActivityServiceImpl(ActivityRepository activityRepository, UserRepository userRepository, ResourceRepository resourceRepository, CatalogueRepository catalogueRepository){
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.catalogueRepository = catalogueRepository;
    }

    @Override
    public void setResourceActivity(ActivityDto activityDto) {
        Activity resourceActivity = activityRepository.getActivityByResource(activityDto.getResource());

        if (Objects.nonNull(resourceActivity)){
            updateActivity(resourceActivity, activityDto);
        } else {
            Activity newActivity = mapToEntity(activityDto);
            activityRepository.save(newActivity);
        }
    }

    @Override
    public void setCatalogueActivity(ActivityDto activityDto) {
        Activity catalogueActivity = activityRepository.getActivityByCatalogue(activityDto.getCatalogue());

        if (Objects.nonNull(catalogueActivity.getCatalogue())){
            updateActivity(catalogueActivity, activityDto);
        } else {
            Activity newActivity = mapToEntity(activityDto);
            activityRepository.save(newActivity);
        }
    }

    private Activity updateActivity(Activity activity, ActivityDto activityDto) {

        activity.setView(activityDto.isView());
        activity.setFavorite(activityDto.isFavorite());
        activity.setBlocked(activityDto.isBlocked());
        activity.setShare(activityDto.isShare());

        return activityRepository.save(activity);
    }

    @Override
    public List<ActivityDto> getActivityByUser(String email){
        User user = userRepository.findUserByEmail(email);

        return activityRepository.getActivitiesByUser(user).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getActivityByResource(String email, long resourceId){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = resourceRepository.getResourceById(resourceId);

        return activityRepository.getActivitiesByResource(resource).stream()
                .filter(activity -> activity.getUser().equals(user))
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getActivityByCatalogue(String email, long catalogueId){
        User user = userRepository.findUserByEmail(email);
        Catalogue catalogue = catalogueRepository.getCatalogueById(catalogueId);

        return activityRepository.getActivitiesByCatalogue(Optional.ofNullable(catalogue)).stream()
                .filter(activity -> activity.getUser().equals(user))
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getViewedActivityByUser(String email){
        User user = userRepository.findUserByEmail(email);

        return activityRepository.getActivitiesByUser(user).stream()
                .filter(Activity::isView)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getLickedActivityByUser(String email){
        User user = userRepository.findUserByEmail(email);

        return activityRepository.getActivitiesByUser(user).stream()
                .filter(Activity::isFavorite)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getShareActivityByUser(String email){
        User user = userRepository.findUserByEmail(email);

        return activityRepository.getActivitiesByUser(user).stream()
                .filter(Activity::isShare)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getBlockedActivityByUser(String email){
        User user = userRepository.findUserByEmail(email);

        return activityRepository.getActivitiesByUser(user).stream()
                .filter(Activity::isBlocked)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getLikedActivityByUserId(long id){
        User user = userRepository.findUserById(id);

        return activityRepository.getActivitiesByUser(user).stream()
                .filter(Activity::isFavorite)
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ActivityDto> getSharedActivityByUserId(long id){
        User user = userRepository.findUserById(id);

        return activityRepository.getActivitiesByUser(user).stream()
                .filter(Activity::isShare)
                .map(this::mapToDTO)
                .toList();
    }

    private ActivityDto mapToDTO(Activity activity){
        activity.getUser().setPassword(null);
        activity.getUser().setRoles(null);
        activity.getUser().setFriends(null);

        ActivityDto activityDto = new ActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setUser(activity.getUser());
        activityDto.setResource(Optional.ofNullable(activity.getResource()));
        activityDto.setCatalogue(Optional.ofNullable(activity.getCatalogue()));
        activityDto.setView(activity.isView());
        activityDto.setFavorite(activity.isFavorite());
        activityDto.setCreated(activity.isCreated());
        activityDto.setBlocked(activity.isBlocked());
        activityDto.setShare(activity.isShare());

        return activityDto;
    }

    private Activity mapToEntity(ActivityDto activityDto){
        Activity activity = new Activity();
        activity.setId(activityDto.getId());
        activity.setUser(activityDto.getUser());
        activity.setResource(activityDto.getResource().orElse(null));
        activity.setCatalogue(activityDto.getCatalogue().orElse(null));
        activity.setView(activityDto.isView());
        activity.setFavorite(activityDto.isFavorite());
        activity.setCreated(activityDto.isCreated());
        activity.setBlocked(activityDto.isBlocked());
        activity.setShare(activityDto.isShare());

        return activity;
    }
}
