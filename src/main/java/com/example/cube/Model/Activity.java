package com.example.cube.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Activity {

    private int id;
    private int userId;
    private int resourceId;
    private boolean view;
    private boolean favorite;
    private boolean created;
    private boolean blocked;
    private boolean share;
}
