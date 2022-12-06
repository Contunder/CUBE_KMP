package com.example.cube.DAO;

import com.example.cube.Model.User;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;

public class RoleDAO extends DBConnect {

    @SneakyThrows
    public void createRole(User user, String role, boolean verified){
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createRole(?,?,?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, role);
        preparedStatement.setBoolean(3, verified);
        preparedStatement.execute();
    }

    public void getRole(User user){

    }

    public void updateRole(User user, String role, boolean verified){

    }

    public void deleteRole(User user){

    }

}
