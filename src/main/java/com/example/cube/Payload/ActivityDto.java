package com.example.cube.Payload;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import lombok.Data;

@Data
public class ActivityDto {

    private long id;
    private User user;
    private Resource resource;
    private Catalogue catalogue;
    private boolean view;
    private boolean favorite;
    private boolean created;
    private boolean blocked;
    private boolean share;

}
