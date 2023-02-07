package com.example.cube.Service;

import com.example.cube.Payload.ActivityDto;

import java.util.List;

public interface ActivityService {

    void setResourceActivity(ActivityDto activityDto);


    void setCatalogueActivity(ActivityDto activityDto);

    List<ActivityDto> getActivityByUser(String email);

    List<ActivityDto> getActivityByResource(String email, long resourceId);

    List<ActivityDto> getActivityByCatalogue(String email, long catalogueId);

    List<ActivityDto> getViewedActivityByUser(String email);

    List<ActivityDto> getLickedActivityByUser(String email);

    List<ActivityDto> getLikedActivityByUserId(long id);

    List<ActivityDto> getShareActivityByUser(String email);

    List<ActivityDto> getBlockedActivityByUser(String email);

    List<ActivityDto> getSharedActivityByUserId(long id);
}
