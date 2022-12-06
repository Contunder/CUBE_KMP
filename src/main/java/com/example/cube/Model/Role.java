package com.example.cube.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Role{

    private int userId;
    private String role;
    private boolean verified;

}
