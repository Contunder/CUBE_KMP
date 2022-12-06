package com.example.cube.DAO;

import com.example.cube.Model.User;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoleDAO extends DBConnect {

    public void createRole(User user, String role, boolean verified) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createRole(?,?,?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, role);
        preparedStatement.setBoolean(3, verified);
        preparedStatement.execute();
    }

    public void getRole(User user) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL getRole(?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.execute();
    }

    public void updateRole(User user, String role, boolean verified) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL updateRole(?,?,?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, role);
        preparedStatement.setBoolean(3, verified);
        preparedStatement.execute();
    }

    public void deleteRole(User user) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteRole(?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.execute();
    }

}
