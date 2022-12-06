package com.example.cube.DAO;

import com.example.cube.Model.User;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO extends DBConnect {

    private PreparedStatement userStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setDate(3, user.getBirthday());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getProfilePicture());
        preparedStatement.setBoolean(7, user.isDisabled());
        return preparedStatement;
    }

    @SneakyThrows
    public void createUser(User user){
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createUser(?,?,?,?,?,?,?)");
        userStatement(user, preparedStatement);
        preparedStatement.execute();
    }

    @SneakyThrows
    public void getUser(User user){
        PreparedStatement preparedStatement = conn().prepareStatement("CALL getUser(?)");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.execute();
    }

    @SneakyThrows
    public void updateUser(User user){
        PreparedStatement preparedStatement = conn().prepareStatement("CALL updateUser(?,?,?,?,?,?,?)");
        userStatement(user, preparedStatement);
        preparedStatement.execute();
    }

    @SneakyThrows
    public void deleteUser(User user){
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteUser(?)");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.execute();
    }

    @SneakyThrows
    public void disableUser(User user){
        PreparedStatement preparedStatement = conn().prepareStatement("CALL disableUser(?,?)");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setBoolean(2, user.isDisabled());

        preparedStatement.execute();
    }
}
