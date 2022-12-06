package com.example.cube.DAO;

import com.example.cube.Model.User;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private User userResultSet(ResultSet resultSet) throws SQLException {
        User emptyUser = new User();

        emptyUser.setId(resultSet.getInt(1));
        emptyUser.setName(resultSet.getString(2));
        emptyUser.setLastName(resultSet.getString(3));
        emptyUser.setBirthday(resultSet.getDate(4));
        emptyUser.setEmail(resultSet.getString(5));
        emptyUser.setProfilePicture(resultSet.getString(7));
        emptyUser.setDisabled(resultSet.getBoolean(8));

        return emptyUser;
    }

    public void createUser(User user) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createUser(?,?,?,?,?,?,?)");
        userStatement(user, preparedStatement);
        preparedStatement.execute();
    }

    public List<User> getAllEnableUser(User user) throws SQLException{
        List<User> resultUser = new ArrayList<>();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getAllUser(?)");
        preparedStatement.setBoolean(1, false);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            resultUser.add(userResultSet(resultSet));
        }

        return resultUser;
    }

    public List<User> getAllUser(User user) throws SQLException{
        List<User> resultUser = new ArrayList<>();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getAllUser()");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            resultUser.add(userResultSet(resultSet));
        }

        return resultUser;
    }
    public User getUser(User user) throws SQLException{

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getUser(?)");
        preparedStatement.setString(1, user.getEmail());
        ResultSet resultSet = preparedStatement.executeQuery();

        return userResultSet(resultSet);
    }

    public void updateUser(User user) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL updateUser(?,?,?,?,?,?,?)");
        userStatement(user, preparedStatement);
        preparedStatement.execute();
    }

    public void deleteUser(User user) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteUser(?)");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.execute();
    }

    public void disableUser(User user) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL disableUser(?,?)");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setBoolean(2, user.isDisabled());

        preparedStatement.execute();
    }
}
