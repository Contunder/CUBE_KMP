package com.example.cube.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    static String url="jdbc:mysql://127.0.0.1:8889/";
    static String dbName = "CUBE";
    static String userName = "valentin";
    static String password = "kilabilon";

    public Connection conn () throws SQLException {
        return (Connection) DriverManager.getConnection(url + dbName , userName, password);
    }
}
