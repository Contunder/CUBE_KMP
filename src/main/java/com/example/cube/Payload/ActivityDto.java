package com.example.cube.Payload;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import lombok.Data;

import java.util.Optional;

@Data
public class ActivityDto {

    private long id;
    private User user;
    private Optional<Resource> resource;
    private Optional<Catalogue> catalogue;
    private boolean view;
    private boolean favorite;
    private boolean created;
    private boolean blocked;
    private boolean share;

}
