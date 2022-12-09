package com.example.cube.DAO;

import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Service.DBConnect;
import org.springframework.data.relational.core.sql.Like;
import org.springframework.data.relational.core.sql.SQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ResourceDAO extends DBConnect {

    public void createResource(User user, String visibility, String value, String joint) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createResource(?,?,?,?)");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, visibility);
        preparedStatement.setString(3, value);
        preparedStatement.setString(4, joint);

        preparedStatement.execute();
    }

    public List<Resource> getAllResource(){

        return null;
    }

    public Resource getResource(Resource resource){

        return null;
    }

    public List<Resource> getUserResource(User user, Resource resource){

        return null;
    }

    public void updateResource(User user, Resource resource, String visibility, String value, String joint) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL updateResource(?,?,?,?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, user.getId());
        preparedStatement.setString(3, visibility);
        preparedStatement.setString(4, value);
        preparedStatement.setString(5, joint);

        preparedStatement.execute();
    }

    public void deleteResource(Resource resource) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteResource(?)");
        preparedStatement.setInt(1, resource.getId());

        preparedStatement.execute();
    }
}
