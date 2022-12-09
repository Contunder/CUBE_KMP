package com.example.cube.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Analytics {

    private int activityId;
    private int resourceId;
    private int view;
    private int favorite;
    private int created;
    private int blocked;
    private int share;
}
