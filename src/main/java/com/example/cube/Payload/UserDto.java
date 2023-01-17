package com.example.cube.Payload;

import lombok.Data;

import java.sql.Date;

@Data
public class UserDto {
    private long id;
    private String name;
    private String lastName;
    private Date birthday;
    private String address;
    private String zipCode;
    private String city;
    private String email;
    private String profilPicture;
}
