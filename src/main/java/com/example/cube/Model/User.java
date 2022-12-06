package com.example.cube.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class User {

    private int id;
    private String name;
    private String lastName;
    private Date birthday;
    private String address;
    private String zipCode;
    private String city;
    private String email;
    private String password;
    private String profilePicture;
    private boolean disabled;


}
