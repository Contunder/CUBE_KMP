package com.example.cube.DAO;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceToCatalogueDAO extends DBConnect {

    public void createReference(Resource resource, Catalogue catalogue) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createReference(?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, catalogue.getId());
        preparedStatement.execute();
    }

    public List<Catalogue> getCatalogueByResource(Resource resource) throws SQLException{
        List<Catalogue> catalogues = new ArrayList<>();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getCatalogueByResource(?)");
        preparedStatement.setInt(1, resource.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){

        }

        return catalogues;
    }

    public List<Resource> getReferenceByCatalogue(Catalogue catalogue) throws SQLException{
        List<Resource> resources = new ArrayList<>();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getResourceByCatalogue(?)");
        preparedStatement.setInt(1, catalogue.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){

        }

        return resources;

    }

    public void deleteReference(Resource resource, Catalogue catalogue) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteReference(?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, catalogue.getId());
        preparedStatement.execute();
    }
}
