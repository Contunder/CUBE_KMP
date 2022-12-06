package com.example.cube.DAO;

import com.example.cube.Model.Role;
import com.example.cube.Model.User;
import com.example.cube.Service.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO extends DBConnect {

    public void createRole(User user, String role, boolean verified) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createRole(?,?,?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, role);
        preparedStatement.setBoolean(3, verified);
        preparedStatement.execute();
    }

    public Role getRole(User user) throws SQLException{
        Role role = new Role();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getRole(?)");
        preparedStatement.setInt(1, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        role.setUserId(resultSet.getInt(1));
        role.setRole(resultSet.getString(2));
        role.setVerified(resultSet.getBoolean(3));

        return role;
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
