package com.example.cube.Service;

import com.example.cube.Payload.ActivityDto;

import java.util.List;

public interface ActivityService {

    void setActivity(ActivityDto activityDto);


    List<ActivityDto> getActivityByUser(String email);

    List<ActivityDto> getActivityByResource(String email, long resourceId);

    List<ActivityDto> getActivityByCatalogue(String email, long catalogueId);
}
