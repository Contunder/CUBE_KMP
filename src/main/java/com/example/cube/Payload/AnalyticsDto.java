package com.example.cube.Payload;

import com.example.cube.Model.Activity;
import com.example.cube.Model.Resource;
import lombok.Data;

import java.util.Optional;

@Data
public class AnalyticsDto {

    private long id;
    private Optional<Activity> activity;
    private Optional<Resource> resource;
    private int view;
    private int favorite;
    private int created;
    private int blocked;
    private int share;

}
