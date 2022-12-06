package com.example.cube.Service;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    static String url="jdbc:mysql://127.0.0.1:8889/";
    static String dbName = "lamiseauvert";
    static String userName = "valentin";
    static String password = "kilabilon";

    @SneakyThrows
    public Connection conn (){
        return (Connection) DriverManager.getConnection(url + dbName , userName, password);
    }
}
